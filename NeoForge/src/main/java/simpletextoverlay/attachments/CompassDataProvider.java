package simpletextoverlay.attachments;

import java.util.Optional;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.common.util.ValueIOSerializable;

import simpletextoverlay.overlay.compass.DataManager;

public class CompassDataProvider {

    public static Optional<DataManager> getData(final Player player) {
        return Optional.of(player.getData(AttachmentDataManager.COMPASS_DATA.get()));
    }

    public static class DataManagerProvider extends DataManager implements ValueIOSerializable {

        private static DataManager instance;

        public DataManagerProvider() {
            instance = new DataManager();
        }

        @Override
        public void serialize(ValueOutput writeView) {
            writeView.storeNullable("Data", CompoundTag.CODEC, instance.getSyncData());
        }

        @Override
        public void deserialize(ValueInput readView) {
            instance.readSyncData(readView.read("Data", CompoundTag.CODEC));
        }

    }

}
