package simpletextoverlay.capability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import simpletextoverlay.overlay.compass.DataManager;

public class CapabilityDataManager {

    public static final Capability<DataManager> DATA_MANAGER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public static final Map<UUID, LazyOptional<DataManager>> SERVER_CACHE = new HashMap<>();
    public static final Map<UUID, LazyOptional<DataManager>> CLIENT_CACHE = new HashMap<>();

    public static LazyOptional<DataManager> getCapability(final Player player) {
        UUID uuid = player.getUUID();
        Map<UUID, LazyOptional<DataManager>> cache = player.level().isClientSide() ? CLIENT_CACHE : SERVER_CACHE;

        return cache.computeIfAbsent(uuid, (data) -> {
            LazyOptional<DataManager> cap = player.getCapability(DATA_MANAGER_CAPABILITY);

            cap.addListener((a) -> cache.remove(uuid));

            return cap;
        });
    }

    public static class Provider implements ICapabilitySerializable<ListTag> {

        @NotNull
        private final DataManager instance;

        private final LazyOptional<DataManager> handler;

        public Provider(Player player) {
            instance = new DataManager();
            handler = LazyOptional.of(this::getInstance);
            instance.setPlayer(player);
        }

        public @NotNull DataManager getInstance() {
            return instance;
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return DATA_MANAGER_CAPABILITY.orEmpty(cap, handler);
        }

        @Override
        public ListTag serializeNBT() {
            return DataManager.write(instance);
        }

        @Override
        public void deserializeNBT(ListTag nbt) {
            DataManager.read(instance, nbt);
        }

    }

}
