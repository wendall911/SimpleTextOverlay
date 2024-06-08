package simpletextoverlay.capability;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.overlay.compass.DataManager;

public class CapabilityDataManager {

    private static final ResourceLocation ATTACHMENT = new ResourceLocation(SimpleTextOverlay.MODID, "sto_provider");
    public static final EntityCapability<DataManagerProvider, Void> DATA_MANAGER_CAPABILITY = EntityCapability.createVoid(
        ATTACHMENT,
        DataManagerProvider.class
    );

    public static Optional<DataManager> getCapability(final Player player) {
        return Optional.ofNullable(player.getCapability(DATA_MANAGER_CAPABILITY));
    }

    public static class DataManagerProvider extends DataManager implements INBTSerializable<ListTag> {

        @NotNull
        private final DataManager instance;

        public DataManagerProvider(Player player) {
            instance = new DataManager(player);
            instance.setPlayer(player);
        }

        @Override
        public ListTag serializeNBT() {
            return DataManager.write(instance.getPlayer().getUUID());
        }

        @Override
        public void deserializeNBT(ListTag nbt) {
            DataManager.read(instance.getPlayer(), nbt);
        }

    }

}
