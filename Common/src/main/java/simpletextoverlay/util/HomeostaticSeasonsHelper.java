package simpletextoverlay.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import homeostaticseasons.api.HomeostaticSeasonsAPI;

public class HomeostaticSeasonsHelper {

    public static SubSeason getSubSeason(Level level) {
        return SubSeason.values()[HomeostaticSeasonsAPI.getCurrentSeason(level).ordinal()];
    }

    public static boolean isDimensionWhitelisted(ResourceKey<Level> dimension) {
        return HomeostaticSeasonsAPI.isSeasonalDimension(dimension);
    }

}
