package simpletextoverlay.platform;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.SubSeason;

public class ForgeSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<Component, SubSeason> getSeasonName(Minecraft mc, BlockPos pos) {
        if (ServerConfig.isDimensionWhitelisted(mc.level.dimension())) {
            SubSeason subSeason = SubSeason.valueOf(SeasonHelper.getSeasonState(mc.level).getSubSeason().name());
            Component seasonName = Component.translatable("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

            return Pair.of(seasonName, subSeason);
        }

        return null;
    }

}
