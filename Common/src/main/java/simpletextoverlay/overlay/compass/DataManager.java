package simpletextoverlay.overlay.compass;

import java.util.HashMap;
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

    private final Map<ResourceKey<Level>, Pins> worldPins = new HashMap<>();

    public DataManager() {}

    public CompoundTag getSyncData() {
        CompoundTag syncData = new CompoundTag();

        syncData.put(STO_DATA, write());

        return syncData;
    }

    public void readSyncData(CompoundTag nbt) {
        read(nbt.getList(STO_DATA, Tag.TAG_COMPOUND));
    }

    public void read(ListTag nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            CompoundTag tag = nbt.getCompound(i);
            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, ResourceLocation.bySeparator(tag.getString("World"), ':'));
            ResourceKey<DimensionType> dimType = null;

            if (tag.contains("DimensionKey", Tag.TAG_STRING)) {
                dimType = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.bySeparator(tag.getString("DimensionKey"), ':'));
            }

            Pins pins = get(key, dimType);

            pins.read(tag.getList("PINS", Tag.TAG_COMPOUND));
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

    public Pins get(Player player) {
        return getInternal(player.level().dimension(), () -> getDimensionTypeKey(player.level(), null));
    }

    public Pins get(Player player, ResourceKey<Level> worldKey) {
        return get(player, worldKey, null);
    }

    public Pins get(ResourceKey<Level> worldKey, @Nullable ResourceKey<DimensionType> dimensionTypeKey) {
        return getInternal(worldKey, () -> dimensionTypeKey);
    }

    public Pins get(Player player, ResourceKey<Level> worldKey, @Nullable ResourceKey<DimensionType> dimensionTypeKey) {
        return getInternal(worldKey, () -> {
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

    private Pins getInternal(ResourceKey<Level> worldKey, Supplier<ResourceKey<DimensionType>> dimensionTypeKey) {
        return worldPins.computeIfAbsent(Objects.requireNonNull(worldKey), worldKey1 -> new Pins(dimensionTypeKey.get()));
    }

    @Nullable
    private ResourceKey<DimensionType> getDimensionTypeKey(Level world, @Nullable ResourceKey<DimensionType> fallback) {
        DimensionType dimType = world.dimensionType();
        ResourceLocation key = world.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).getKey(dimType);

        if (key == null) {
            return fallback;
        }

        return ResourceKey.create(Registries.DIMENSION_TYPE, key);
    }

}
