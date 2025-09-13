package simpletextoverlay.platform;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.EclipticSeasonsHelper;
import simpletextoverlay.util.SereneSeasonsNeoForgeHelper;
import simpletextoverlay.util.SubSeason;

public class NeoForgeSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<Component, SubSeason> getSeasonName(Level level, BlockPos pos) {
        boolean hasSeasonDimension = false;
        SubSeason subSeason = SubSeason.MID_SPRING;

        if (technology.roughness.whitenoise.platform.Services.PLATFORM.isModLoaded("sereneseasons")) {
            subSeason = SereneSeasonsNeoForgeHelper.getSubSeason(level);
            hasSeasonDimension = SereneSeasonsNeoForgeHelper.isDimensionWhitelisted(level.dimension());
        }
        else if (technology.roughness.whitenoise.platform.Services.PLATFORM.isModLoaded("eclipticseasons")) {
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
