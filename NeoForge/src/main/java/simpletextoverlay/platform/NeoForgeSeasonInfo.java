package simpletextoverlay.platform;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.SereneSeasonsNeoForgeHelper;
import simpletextoverlay.util.SubSeason;

public class NeoForgeSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<Component, SubSeason> getSeasonName(Level level, BlockPos pos) {
        if (SereneSeasonsNeoForgeHelper.isDimensionWhitelisted(level.dimension())) {
            SubSeason subSeason = SubSeason.getSubSeason(level, SereneSeasonsNeoForgeHelper.getSeasonDuration(level));
            Component seasonName = Component.translatable("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

            return Pair.of(seasonName, subSeason);
        }

        return null;
    }

}
