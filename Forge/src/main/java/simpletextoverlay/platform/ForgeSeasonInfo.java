package simpletextoverlay.platform;

import java.util.Objects;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;

import sereneseasons.config.BiomeConfig;
import sereneseasons.api.season.SeasonHelper;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.SubSeason;

public class ForgeSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<TranslatableComponent, SubSeason> getSeasonName(Minecraft mc, BlockPos pos) {
        if (BiomeConfig.enablesSeasonalEffects(Objects.requireNonNull(mc.level).getBiome(pos))) {

            SubSeason subSeason = SubSeason.valueOf(SeasonHelper.getSeasonState(mc.level).getSubSeason().name());

            if (BiomeConfig.enablesSeasonalEffects(mc.level.getBiome(pos))) {
                TranslatableComponent seasonName = new TranslatableComponent("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

                return Pair.of(seasonName, subSeason);
            }
        }

        return null;
    }

}
