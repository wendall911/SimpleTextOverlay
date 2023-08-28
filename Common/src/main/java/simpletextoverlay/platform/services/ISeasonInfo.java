package simpletextoverlay.platform.services;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import simpletextoverlay.util.SubSeason;

public interface ISeasonInfo {

    Pair<TranslatableComponent, SubSeason> getSeasonName(Level level, BlockPos pos);

}
