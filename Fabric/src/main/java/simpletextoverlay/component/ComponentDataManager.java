package simpletextoverlay.component;

import dev.onyxstudios.cca.api.v3.component.Component;

import net.fabricmc.fabric.api.util.NbtType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import simpletextoverlay.overlay.compass.DataManager;

public class ComponentDataManager extends DataManager implements Component {

    private final Player player;
    private static DataManager instance;

    public ComponentDataManager(Player player) {
        this.player = player;
        instance = new DataManager();
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.put("Data", instance.write(player.getUUID()));
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        ListTag nbt = tag.getList("Data", NbtType.COMPOUND);

        instance.read(player, nbt);
    }

}
