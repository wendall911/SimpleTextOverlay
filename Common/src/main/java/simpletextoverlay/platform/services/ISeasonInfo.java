package simpletextoverlay.platform.services;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import simpletextoverlay.util.SubSeason;

public interface ISeasonInfo {

    Pair<Component, SubSeason> getSeasonName(Level level, BlockPos pos);

}
