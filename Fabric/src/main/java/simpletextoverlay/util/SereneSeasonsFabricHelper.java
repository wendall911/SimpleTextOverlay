package simpletextoverlay.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import sereneseasons.api.season.SeasonHelper;
import static sereneseasons.init.ModConfig.seasons;

// TODO Re-enable when serene seasons is available for 26.1

public class SereneSeasonsFabricHelper {

    public static boolean isDimensionWhitelisted(ResourceKey<Level> levelResourceKey) {
        //return seasons.isDimensionWhitelisted(levelResourceKey);
        return false;
    }

    public static int getSeasonDuration(Level level) {
        //return SeasonHelper.getSeasonState(level).getSeasonDuration();
        return 0;
    }

    public static SubSeason getSubSeason(Level level) {
        //return SubSeason.values()[SeasonHelper.getSeasonState(level).getSubSeason().ordinal()];
        return SubSeason.EARLY_SPRING;
    }

}
