package simpletextoverlay.overlay.compass;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.Map;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
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

    public void readSyncData(Optional<CompoundTag> nbt) {
        nbt.ifPresent(this::readSyncData);
    }

    public void readSyncData(CompoundTag nbt) {
        Optional<ListTag> optionalListTag = nbt.getList(STO_DATA);

        optionalListTag.ifPresent(this::read);
    }

    public void read(ListTag nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            Optional<CompoundTag> optionalCompoundTag = nbt.getCompound(i);
            if (optionalCompoundTag.isPresent()) {
                CompoundTag tag = optionalCompoundTag.get();

                if (tag.getString("World").isEmpty()) {
                    continue; // Skip if no world is specified
                }

                ResourceKey<Level> key = ResourceKey.create(
                    Registries.DIMENSION,
                    Identifier.bySeparator(tag.getString("World").get(), ':')
                );
                ResourceKey<DimensionType> dimType = null;

                if (tag.contains("DimensionKey") && tag.getString("DimensionKey").isPresent()) {
                    dimType = ResourceKey.create(
                        Registries.DIMENSION_TYPE,
                        Identifier.bySeparator(tag.getString("DimensionKey").get(), ':')
                    );
                }

                Pins pins = get(key, dimType);

                if (tag.getList("PINS").isPresent()) {
                    pins.read(tag.getList("PINS").get());
                }
            }
        }
    }

    public ListTag write() {
        ListTag list = new ListTag();

        for (Map.Entry<ResourceKey<Level>, Pins> entry : worldPins.entrySet()) {
            CompoundTag tag = new CompoundTag();

            tag.putString("World", entry.getKey().identifier().toString());

            if (entry.getValue().getDimensionTypeKey() != null) {
                tag.putString("DimensionKey", entry.getValue().getDimensionTypeKey().identifier().toString());
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
        Optional<Holder.Reference<Registry<DimensionType>>> optionalRegistry = world.registryAccess().get(Registries.DIMENSION_TYPE);

        if (optionalRegistry.isPresent()) {
            Identifier key = optionalRegistry.get().value().getKey(dimType);

            if (key == null) {
                return fallback;
            }

            return ResourceKey.create(Registries.DIMENSION_TYPE, key);
        }

        return fallback;
    }

}
