package simpletextoverlay.platform.services;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;

import simpletextoverlay.util.SubSeason;

public interface ISeasonInfo {

    Pair<TranslatableComponent, SubSeason> getSeasonName(Minecraft mc, BlockPos pos);

}
