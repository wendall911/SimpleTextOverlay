package simpletextoverlay.platform;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.SubSeason;

public class ForgeSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<Component, SubSeason> getSeasonName(Level level, BlockPos pos) {
        if (ServerConfig.isDimensionWhitelisted(level.dimension())) {
            SubSeason subSeason = SubSeason.getSubSeason(level, SeasonHelper.getSeasonState(level).getSeasonDuration());
            Component seasonName = Component.translatable("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

            return Pair.of(seasonName, subSeason);
        }

        return null;
    }

}
