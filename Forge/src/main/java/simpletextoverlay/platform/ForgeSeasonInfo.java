package simpletextoverlay.platform;

import java.util.Objects;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import sereneseasons.config.BiomeConfig;
import sereneseasons.api.season.SeasonHelper;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.SubSeason;

public class ForgeSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<TranslatableComponent, SubSeason> getSeasonName(Level level, BlockPos pos) {
        if (BiomeConfig.enablesSeasonalEffects(Objects.requireNonNull(level).getBiome(pos))) {
            SubSeason subSeason = SubSeason.values()[SeasonHelper.getSeasonState(level).getSubSeason().ordinal()];
            TranslatableComponent seasonName = new TranslatableComponent("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

            return Pair.of(seasonName, subSeason);
        }

        return null;
    }

}
