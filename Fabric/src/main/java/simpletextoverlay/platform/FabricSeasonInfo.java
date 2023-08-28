package simpletextoverlay.platform;

import com.mojang.datafixers.util.Pair;

import io.github.lucaargolo.seasons.FabricSeasons;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.SubSeason;

public class FabricSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<TranslatableComponent, SubSeason> getSeasonName(Level level, BlockPos pos) {
        if (OverlayConfig.hasSeasonDimension(level.dimension().location().toString())) {
            try {
                SubSeason subSeason = SubSeason.valueOf(FabricSeasons.getCurrentSeason().name());
                TranslatableComponent seasonName = new TranslatableComponent("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

                return Pair.of(seasonName, subSeason);
            }
            catch (IllegalArgumentException e) {
                SimpleTextOverlay.LOGGER.debug("Unable to convert subseason {}", e);
            }
        }

        return null;
    }

}
