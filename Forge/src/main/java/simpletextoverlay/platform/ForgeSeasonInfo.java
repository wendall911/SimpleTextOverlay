package simpletextoverlay.platform;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.EclipticSeasonsHelper;
import simpletextoverlay.util.SereneSeasonsForgeHelper;
import simpletextoverlay.util.SubSeason;

public class ForgeSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<Component, SubSeason> getSeasonName(Level level, BlockPos pos) {
        boolean hasSeasonDimension = false;
        SubSeason subSeason = SubSeason.MID_SPRING;

        if (Services.PLATFORM.isModLoaded("sereneseasons")) {
            subSeason = SereneSeasonsForgeHelper.getSubSeason(level);
            hasSeasonDimension = SereneSeasonsForgeHelper.isDimensionWhitelisted(level.dimension());
        }
        else if (Services.PLATFORM.isModLoaded("eclipticseasons")) {
            hasSeasonDimension = EclipticSeasonsHelper.isSeasonDimension(level);

            if (hasSeasonDimension) {
                subSeason = EclipticSeasonsHelper.getSubSeason(level);
            }
        }

        if (hasSeasonDimension) {
            Component seasonName = Component.translatable("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

            return Pair.of(seasonName, subSeason);
        }

        return null;
    }

}
