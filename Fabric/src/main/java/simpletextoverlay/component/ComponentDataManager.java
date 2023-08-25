package simpletextoverlay.component;

import dev.onyxstudios.cca.api.v3.component.Component;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import simpletextoverlay.overlay.compass.DataManager;

public class ComponentDataManager extends DataManager implements Component {

    private final DataManager instance = new DataManager();

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.put("Data", DataManager.write(instance));
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        ListTag nbt = tag.getList("Data", NbtType.COMPOUND);

        DataManager.read(instance, nbt);
    }

}
