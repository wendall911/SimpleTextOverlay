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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.Level;

public class DataManager {

    public static final String STO_DATA = "sto_data";

    private static final Map<UUID, Map<ResourceKey<Level>, Pins>> worldPins = new HashMap<>();
    private final Map<String, Object> pinData = new HashMap<>();

    public DataManager() {}

    @SuppressWarnings("unchecked")
    public <T> T getOrCreatePinData(String pinId, Supplier<T> factory) {
        return (T) pinData.computeIfAbsent(pinId, key -> factory.get());
    }

    public static CompoundTag getSyncData(Player player) {
        CompoundTag syncData = new CompoundTag();

        syncData.put(STO_DATA, write(player.getUUID()));

        return syncData;
    }

    public static void readSyncData(Player player, CompoundTag nbt) {
        read(player, nbt.getList(STO_DATA, Tag.TAG_COMPOUND));
    }

    public static void resetCache() {
        worldPins.clear();
    }

    public static void read(ListTag nbt) {
        CompoundTag tag = nbt.getCompound(0);
        UUID uuid = tag.getUUID("UUID");

        worldPins.remove(uuid);

        for (int i = 0; i < nbt.size(); i++) {
            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("World")));
            ResourceKey<DimensionType> dimType = null;

            if (tag.contains("DimensionKey", Tag.TAG_STRING)) {
                dimType = ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(tag.getString("DimensionKey")));
            }

            Pins pins = get(uuid, key, dimType);

            pins.read(uuid, tag.getList("PINS", Tag.TAG_COMPOUND));
        }
    }

    public static void read(Player player, ListTag nbt) {
        CompoundTag tag = nbt.getCompound(0);
        UUID uuid;

        // Hopefully this can catch bad data from older versions... needs testing
        try {
            uuid = tag.getUUID("UUID");
        }
        catch(Exception e) {
            uuid = player.getUUID();
            tag.putUUID("UUID", uuid);
            nbt.setTag(0, tag);
        }

        read(nbt);
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

    public static Pins get(UUID uuid, ResourceKey<Level> worldKey, @Nullable ResourceKey<DimensionType> dimensionTypeKey) {
        return getInternal(uuid, worldKey, () -> dimensionTypeKey);
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

    private static Pins getInternal(UUID uuid, ResourceKey<Level> worldKey, Supplier<ResourceKey<DimensionType>> dimensionTypeKey) {
        return worldPins.computeIfAbsent(uuid, k -> new HashMap<>()).computeIfAbsent(Objects.requireNonNull(worldKey), worldKey1 -> new Pins(dimensionTypeKey.get()));
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

        public ListTag write(UUID uuid) {
            ListTag tag = new ListTag();

            for (PinInfo<?> pin : pins.computeIfAbsent(uuid, k -> new HashMap<>()).values()) {
                tag.add(PinInfoRegistry.serializePin(pin));
            }

            return tag;
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
