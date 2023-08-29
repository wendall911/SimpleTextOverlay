package simpletextoverlay.platform;

import java.util.Objects;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import sereneseasons.config.BiomeConfig;
import sereneseasons.api.season.SeasonHelper;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.SubSeason;

public class ForgeSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<Component, SubSeason> getSeasonName(Level level, BlockPos pos) {
        if (BiomeConfig.enablesSeasonalEffects(Objects.requireNonNull(level).getBiome(pos))) {
            SubSeason subSeason = SubSeason.getSubSeason(level, SeasonHelper.getSeasonState(level).getSeasonDuration());
            Component seasonName = Component.translatable("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

            return Pair.of(seasonName, subSeason);
        }

        return null;
    }

}
