package simpletextoverlay.overlay.compass;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.Level;

public class DataManager {

    private static final Map<UUID, Map<ResourceKey<Level>, Pins>> worldPins = new HashMap<>();
    private final Map<String, Object> pinData = new HashMap<>();

    public DataManager() {}

    @SuppressWarnings("unchecked")
    public <T> T getOrCreatePinData(String pinId, Supplier<T> factory) {
        return (T) pinData.computeIfAbsent(pinId, key -> factory.get());
    }

    public static void resetCache() {
        worldPins.clear();
    }

    public void read(Player player, FriendlyByteBuf buffer) {
        int numWorlds = buffer.readVarInt();
        UUID uuid = buffer.readUUID();

        for (int i = 0; i < numWorlds; i++) {
            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, buffer.readResourceLocation());
            boolean hasDimensionType = buffer.readBoolean();
            ResourceKey<DimensionType> dimType = hasDimensionType
                    ? ResourceKey.create(Registries.DIMENSION_TYPE, buffer.readResourceLocation())
                    : null;
            Pins pins = get(player, key, dimType);
            pins.read(uuid, buffer);
        }
    }

    public static void read(Player player, ListTag nbt) {
        worldPins.remove(player.getUUID());

        for (int i = 0; i < nbt.size(); i++) {
            CompoundTag tag = nbt.getCompound(i);
            UUID uuid;

            try {
                uuid = tag.getUUID("UUID");
            }
            catch(Exception e) {
                uuid = player.getUUID();
            }
            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("World")));
            ResourceKey<DimensionType> dimType = null;

            if (tag.contains("DimensionKey", Tag.TAG_STRING)) {
                dimType = ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(tag.getString("DimensionKey")));
            }

            Pins pins = get(player, key, dimType);

            pins.read(uuid, tag.getList("PINS", Tag.TAG_COMPOUND));
        }

    }

    public static void write(UUID uuid, FriendlyByteBuf buffer) {
        buffer.writeVarInt(worldPins.computeIfAbsent(uuid, k -> new HashMap<>()).size());
        buffer.writeUUID(uuid);

        for (Map.Entry<ResourceKey<Level>, Pins> entry : worldPins.get(uuid).entrySet()) {
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

            value.write(uuid, buffer);
        }
    }

    public static ListTag write(UUID uuid) {
        ListTag list = new ListTag();

        for (Map.Entry<ResourceKey<Level>, Pins> entry : worldPins.computeIfAbsent(uuid, k -> new HashMap<>()).entrySet()) {
            CompoundTag tag = new CompoundTag();

            tag.putUUID("UUID", uuid);
            tag.putString("World", entry.getKey().location().toString());

            if (entry.getValue().getDimensionTypeKey() != null) {
                tag.putString("DimensionKey", entry.getValue().getDimensionTypeKey().location().toString());
            }

            tag.put("PINS", entry.getValue().write(uuid));
            list.add(tag);
        }

        return list;
    }

    public Pins get(Player player) {
        return getInternal(player, player.level().dimension(), () -> getDimensionTypeKey(player.level(), null));
    }

    public Pins get(Player player, ResourceKey<Level> worldKey) {
        return get(player, worldKey, null);
    }

    public static Pins get(Player player, ResourceKey<Level> worldKey, @Nullable ResourceKey<DimensionType> dimensionTypeKey) {
        return getInternal(player, worldKey, () -> {
            if (player.level().dimension() == worldKey) {
                return getDimensionTypeKey(player.level(), dimensionTypeKey);
            }

            MinecraftServer server = player.level().getServer();
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

    private static Pins getInternal(Player player, ResourceKey<Level> worldKey, Supplier<ResourceKey<DimensionType>> dimensionTypeKey) {
        return worldPins.computeIfAbsent(player.getUUID(), k -> new HashMap<>()).computeIfAbsent(Objects.requireNonNull(worldKey), worldKey1 -> new Pins(dimensionTypeKey.get()));
    }

    @Nullable
    private static ResourceKey<DimensionType> getDimensionTypeKey(Level world, @Nullable ResourceKey<DimensionType> fallback) {
        DimensionType dimType = world.dimensionType();
        ResourceLocation key = world.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getKey(dimType);

        if (key == null) {
            return fallback;
        }

        return ResourceKey.create(Registries.DIMENSION_TYPE, key);
    }

    public static class Pins {

        @Nullable
        private final ResourceKey<DimensionType> dimensionTypeKey;
        private final Map<UUID, Map<String, PinInfo<?>>> pins = new HashMap<>();

        public Pins(@Nullable ResourceKey<DimensionType> dimensionTypeKey) {
            this.dimensionTypeKey = dimensionTypeKey;
        }

        public Map<String, PinInfo<?>> getPins(UUID uuid) {
            pins.computeIfAbsent(uuid, k -> new HashMap<>());

            return pins.get(uuid);
        }

        public void read(UUID uuid, ListTag nbt) {
            pins.clear();

            for (int i = 0; i < nbt.size(); i++) {
                CompoundTag pinTag = nbt.getCompound(i);
                PinInfo<?> pin = PinInfoRegistry.deserializePin(pinTag);

                pins.computeIfAbsent(uuid, k -> new HashMap<>()).put(pin.getInternalId(), pin);
            }
        }

        public void read(UUID uuid, FriendlyByteBuf buffer) {
            int numPins = buffer.readVarInt();
            pins.clear();

            for (int i = 0; i < numPins; i++) {
                PinInfo<?> pin = PinInfoRegistry.deserializePin(buffer);

                pins.computeIfAbsent(uuid, k -> new HashMap<>()).put(pin.getInternalId(), pin);
            }
        }

        public ListTag write(UUID uuid) {
            ListTag tag = new ListTag();

            for (PinInfo<?> pin : pins.computeIfAbsent(uuid, k -> new HashMap<>()).values()) {
                tag.add(PinInfoRegistry.serializePin(pin));
            }

            return tag;
        }

        public void write(UUID uuid, FriendlyByteBuf buffer) {
            buffer.writeVarInt(pins.computeIfAbsent(uuid, k -> new HashMap<>()).size());

            for (PinInfo<?> pin : pins.computeIfAbsent(uuid, k -> new HashMap<>()).values()) {
                PinInfoRegistry.serializePin(pin, buffer);
            }
        }

        public void addPin(UUID uuid, PinInfo<?> pin) {
            pins.computeIfAbsent(uuid, k -> new HashMap<>()).put(pin.getInternalId(), pin);
        }

        public void removePin(UUID uuid, PinInfo<?> pin) {
            removePin(uuid, pin.getInternalId());
        }

        public void removePin(UUID uuid, String id) {
            PinInfo<?> pin = pins.computeIfAbsent(uuid, k -> new HashMap<>()).get(id);

            if (pin != null) {
                pins.remove(uuid, pin.getInternalId());
            }
        }

        @Nullable
        public ResourceKey<DimensionType> getDimensionTypeKey() {
            return dimensionTypeKey;
        }

    }

}
