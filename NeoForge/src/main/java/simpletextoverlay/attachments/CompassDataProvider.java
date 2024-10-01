package simpletextoverlay.attachments;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.common.util.INBTSerializable;

import simpletextoverlay.overlay.compass.DataManager;

public class CompassDataProvider {

    public static Optional<DataManager> getData(final Player player) {
        return Optional.of(player.getData(AttachmentDataManager.COMPASS_DATA.get()));
    }

    public static class DataManagerProvider extends DataManager implements INBTSerializable<ListTag> {

        public DataManagerProvider() {}

        @Override
        public ListTag serializeNBT(HolderLookup.@NotNull Provider provider) {
            return write();
        }

        @Override
        public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull ListTag nbt) {
            read(nbt);
        }

    }

}
