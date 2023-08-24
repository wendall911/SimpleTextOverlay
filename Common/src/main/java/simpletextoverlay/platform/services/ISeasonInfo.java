package simpletextoverlay.platform.services;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import simpletextoverlay.util.SubSeason;

public interface ISeasonInfo {

    Pair<Component, SubSeason> getSeasonName(Minecraft mc, BlockPos pos);

}
