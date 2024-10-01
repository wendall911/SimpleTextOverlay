package simpletextoverlay.component;

import org.jetbrains.annotations.NotNull;

import net.fabricmc.fabric.api.util.NbtType;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import org.ladysnake.cca.api.v3.component.Component;

import simpletextoverlay.overlay.compass.DataManager;

public class ComponentDataManager extends DataManager implements Component {

    private static DataManager instance;

    public ComponentDataManager(Player player) {
        instance = new DataManager();
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.@NotNull Provider registryLookup) {
        tag.put("Data", instance.write());
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.@NotNull Provider registryLookup) {
        ListTag nbt = tag.getList("Data", NbtType.COMPOUND);

        instance.read(nbt);
    }

}
