package simpletextoverlay.overlay.compass;

import com.google.common.collect.Maps;

import io.netty.buffer.Unpooled;

import java.util.function.Supplier;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.network.PacketDistributor;

import simpletextoverlay.network.NetworkManager;
import simpletextoverlay.network.SyncData;
import simpletextoverlay.SimpleTextOverlay;

public class DataManager {

    public static Capability<DataManager> INSTANCE = CapabilityManager.get(new CapabilityToken<>(){});

    private static final ResourceLocation PROVIDER = new ResourceLocation(SimpleTextOverlay.MODID, "sto_provider");
    private Player player;
    private final Map<ResourceKey<Level>, Pins> worldPins = Maps.newHashMap();
    private final Map<String, Object> pinData = Maps.newHashMap();

    public DataManager() {}

    public static void init(RegisterCapabilitiesEvent event) {
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, DataManager::attachCapabilities);
    }

    public <T> T getOrCreatePinData(String pinId, Supplier<T> factory) {
        return (T) pinData.computeIfAbsent(pinId, key -> factory.get());
    }

    private static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Player cPlayer = event.getObject() instanceof Player ? (Player)event.getObject() : null;

        if (cPlayer != null) {
            event.addCapability(PROVIDER, new ICapabilitySerializable<ListTag>() {
                private final DataManager dataManager = new DataManager();
                private final LazyOptional<DataManager> dataSupplier = LazyOptional.of(() -> dataManager);

                {
                    dataManager.setPlayer(cPlayer);
                }

                @Override
                public ListTag serializeNBT() {
                    return dataManager.write();
                }


                @Override
                public void deserializeNBT(ListTag nbt) {
                    dataManager.read(nbt);
                }

                @Nonnull
                @Override
                public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
                    if (capability == INSTANCE) {
                        return dataSupplier.cast();
                    }

                    return LazyOptional.empty();
                }
            });
        }
    }

    public static void handleSync(Player hPlayer, byte[] packet) {
        hPlayer.getCapability(DataManager.INSTANCE).ifPresent(data -> {
            data.read(new FriendlyByteBuf(Unpooled.wrappedBuffer(packet)));
        });
    }

    public void read(FriendlyByteBuf buffer) {
        int numWorlds = buffer.readVarInt();

        for (int i = 0; i < numWorlds; i++) {
            ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, buffer.readResourceLocation());

            boolean hasDimensionType = buffer.readBoolean();
            ResourceKey<DimensionType> dimType = hasDimensionType
                    ? ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, buffer.readResourceLocation())
                    : null;
            Pins pins = get(key, dimType);
            pins.read(buffer);
        }
    }

    public void read(ListTag nbt) {
        worldPins.clear();

        for (int i = 0; i < nbt.size(); i++) {
            CompoundTag tag = nbt.getCompound(i);
            ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag.getString("World")));
            ResourceKey<DimensionType> dimType = null;

            if (tag.contains("DimensionKey", Tag.TAG_STRING)) {
                dimType = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(tag.getString("DimensionKey")));
            }

            Pins pins = get(key, dimType);

            pins.read(tag.getList("PINS", Tag.TAG_COMPOUND));
        }

    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(worldPins.size());

        for (Map.Entry<ResourceKey<Level>, Pins> entry : worldPins.entrySet()) {
            ResourceKey<Level> key = entry.getKey();
            Pins value = entry.getValue();

            buffer.writeResourceLocation(key.location());

            if (value.getDimensionTypeKey() != null) {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(value.getDimensionTypeKey().location());
            }
            else {
                buffer.writeBoolean(false);
            }

            value.write(buffer);
        }
    }

    public ListTag write() {
        ListTag list = new ListTag();

        for (Map.Entry<ResourceKey<Level>, Pins> entry : worldPins.entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putString("World", entry.getKey().location().toString());

            if (entry.getValue().getDimensionTypeKey() != null) {
                tag.putString("DimensionKey", entry.getValue().getDimensionTypeKey().location().toString());
            }

            tag.put("PINS", entry.getValue().write());
            list.add(tag);
        }

        return list;
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    public Pins get(Level world) {
        return getInternal(world.dimension(), () -> getDimensionTypeKey(world, null));
    }

    public Pins get(ResourceKey<Level> worldKey) {
        return get(worldKey, null);
    }

    public Pins get(ResourceKey<Level> worldKey, @Nullable ResourceKey<DimensionType> dimensionTypeKey) {
        return getInternal(worldKey, () -> {
            if (player.level.dimension() == worldKey) {
                return getDimensionTypeKey(player.level, dimensionTypeKey);
            }

            MinecraftServer server = player.level.getServer();
            if (server == null) {
                return dimensionTypeKey;
            }

            Level world = server.getLevel(worldKey);
            if (world == null) {
                return dimensionTypeKey;
            }

            return getDimensionTypeKey(world, dimensionTypeKey);
        });
    }

    private Pins getInternal(ResourceKey<Level> worldKey, Supplier<ResourceKey<DimensionType>> dimensionTypeKey) {
        return worldPins.computeIfAbsent(Objects.requireNonNull(worldKey), worldKey1 -> new Pins(dimensionTypeKey.get()));
    }

    @Nullable
    private static ResourceKey<DimensionType> getDimensionTypeKey(Level world, @Nullable ResourceKey<DimensionType> fallback) {
        DimensionType dimType = world.dimensionType();
        ResourceLocation key = world.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getKey(dimType);

        if (key == null) {
            return fallback;
        }

        return ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, key);
    }

    private void sync() {
        NetworkManager.INSTANCE.send(
            PacketDistributor.PLAYER.with(() -> (ServerPlayer)player),
            new SyncData(this)
        );
    }

    public class Pins {

        @Nullable
        private final ResourceKey<DimensionType> dimensionTypeKey;
        private final Map<String, PinInfo<?>> pins = Maps.newHashMap();

        public Pins(@Nullable ResourceKey<DimensionType> dimensionTypeKey) {
            this.dimensionTypeKey = dimensionTypeKey;
        }

        public Map<String, PinInfo<?>> getPins() {
            return pins;
        }

        public void read(ListTag nbt) {
            pins.clear();

            for (int i = 0; i < nbt.size(); i++) {
                CompoundTag pinTag = nbt.getCompound(i);
                PinInfo<?> pin = PinInfoRegistry.deserializePin(pinTag);

                pins.put(pin.getInternalId(), pin);
            }
        }

        public void read(FriendlyByteBuf buffer) {
            int numPins = buffer.readVarInt();

            pins.clear();

            for (int i = 0; i < numPins; i++) {
                PinInfo<?> pin = PinInfoRegistry.deserializePin(buffer);

                pins.put(pin.getInternalId(), pin);
            }
        }

        public ListTag write() {
            ListTag tag = new ListTag();

            for (PinInfo<?> pin : pins.values()) {
                tag.add(PinInfoRegistry.serializePin(pin));
            }

            return tag;
        }

        public void write(FriendlyByteBuf buffer) {
            buffer.writeVarInt(pins.size());

            for (PinInfo<?> pin : pins.values()) {
                PinInfoRegistry.serializePin(pin, buffer);
            }
        }

        public void addPin() {
            sync();
        }

        public void removePin(PinInfo<?> pin) {
            removePin(pin.getInternalId());
        }

        public void removePin(String id) {
            PinInfo<?> pin = pins.get(id);

            if (pin != null) {
                pin.setPins();
                pins.remove(pin.getInternalId());
            }

            sync();
        }

        @Nullable
        public ResourceKey<DimensionType> getDimensionTypeKey() {
            return dimensionTypeKey;
        }

    }

}
