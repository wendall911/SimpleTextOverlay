package simpletextoverlay.platform;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.SubSeason;

public class FabricSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<TranslatableComponent, SubSeason> getSeasonName(Level level, BlockPos pos) {
        return null;
    }

}
