package simpletextoverlay.component;

import java.util.Optional;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    public void writeData(ValueOutput writeView) {
        tag.put("Data", instance.write());
    }

    @Override
    public void readData(ValueInput readView) {
        Optional<ListTag> nbt = tag.getList("Data", NbtType.COMPOUND);

        instance.read(nbt);
    }

}
