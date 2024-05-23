package simpletextoverlay.component;

import dev.onyxstudios.cca.api.v3.component.Component;

import net.fabricmc.fabric.api.util.NbtType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import simpletextoverlay.overlay.compass.DataManager;

public class ComponentDataManager extends DataManager implements Component {

    private final Player player;

    public ComponentDataManager(Player player) {
        this.player = player;
        DataManager instance = new DataManager();
        instance.setPlayer(player);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.put("Data", DataManager.write(player.getUUID()));
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        ListTag nbt = tag.getList("Data", NbtType.COMPOUND);

        DataManager.read(player, nbt);
    }

}
