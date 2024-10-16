package simpletextoverlay.platform;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.FabricSeasonsHelper;
import simpletextoverlay.util.SereneSeasonsFabricHelper;
import simpletextoverlay.util.SubSeason;

public class FabricSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<Component, SubSeason> getSeasonName(Level level, BlockPos pos) {
        boolean hasSeasonDimension = false;
        SubSeason subSeason = SubSeason.MID_SPRING;

        if (Services.PLATFORM.isModLoaded("sereneseasons")) {
            subSeason = SereneSeasonsFabricHelper.getSubSeason(level);
            hasSeasonDimension = SereneSeasonsFabricHelper.isDimensionWhitelisted(level.dimension());
        }
        else if (Services.PLATFORM.isModLoaded("seasons")) {
            subSeason = SubSeason.getSubSeason(level, FabricSeasonsHelper.getSeasonDuration());
            hasSeasonDimension = FabricSeasonsHelper.isDimensionWhitelisted(level.dimension());
        }

        if (hasSeasonDimension) {
            Component seasonName = Component.translatable("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

            return Pair.of(seasonName, subSeason);
        }

        return null;
    }

}
