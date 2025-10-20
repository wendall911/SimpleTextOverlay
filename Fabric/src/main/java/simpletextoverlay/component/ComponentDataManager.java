package simpletextoverlay.component;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import org.ladysnake.cca.api.v3.component.Component;

import simpletextoverlay.overlay.compass.DataManager;

public class ComponentDataManager extends DataManager implements Component {

    private static DataManager instance;

    public ComponentDataManager(Player player) {
        instance = new DataManager();
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.storeNullable("Data", CompoundTag.CODEC, instance.getSyncData());
    }

    @Override
    public void readData(ValueInput readView) {
        instance.readSyncData(readView.read("Data", CompoundTag.CODEC));
    }

}
