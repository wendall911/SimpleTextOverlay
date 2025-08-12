package simpletextoverlay.attachments;

import java.util.Optional;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.common.util.ValueIOSerializable;

import simpletextoverlay.overlay.compass.DataManager;

public class CompassDataProvider {

    public static Optional<DataManager> getData(final Player player) {
        return Optional.of(player.getData(AttachmentDataManager.COMPASS_DATA.get()));
    }

    public static class DataManagerProvider extends DataManager implements ValueIOSerializable<ListTag> {

        public DataManagerProvider() {}

        @Override
        public ListTag (ValueOutput output) {
            return write();
        }

        @Override
        public void deserialize(ValueInput input) {
            read(input);
        }

    }

}
