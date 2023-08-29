package simpletextoverlay.platform;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.util.SubSeason;

import static io.github.lucaargolo.seasons.FabricSeasons.CONFIG;

public class FabricSeasonInfo implements ISeasonInfo {

    @Override
    public Pair<TranslatableComponent, SubSeason> getSeasonName(Level level, BlockPos pos) {
        if (OverlayConfig.hasSeasonDimension(level.dimension().location().toString())) {
            SubSeason subSeason = SubSeason.getSubSeason(level, CONFIG.getSeasonLength());
            TranslatableComponent seasonName = new TranslatableComponent("desc." + SimpleTextOverlay.MODID + "." + subSeason.name().toLowerCase());

            return Pair.of(seasonName, subSeason);
        }

        return null;
    }

}
