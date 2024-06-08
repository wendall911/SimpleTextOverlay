package simpletextoverlay.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import sereneseasons.api.season.SeasonHelper;

import static sereneseasons.init.ModConfig.seasons;

public class SereneSeasonsNeoForgeHelper {

    public static boolean isDimensionWhitelisted(ResourceKey<Level> levelResourceKey) {
        return seasons.isDimensionWhitelisted(levelResourceKey);
    }

    public static int getSeasonDuration(Level level) {
        return SeasonHelper.getSeasonState(level).getSeasonDuration();
    }

}
