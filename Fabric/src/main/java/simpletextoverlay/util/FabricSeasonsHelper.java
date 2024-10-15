package simpletextoverlay.util;

import static io.github.lucaargolo.seasons.FabricSeasons.CONFIG;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class FabricSeasonsHelper {

    public static int getSeasonDuration() {
        return CONFIG.getSpringLength();
    }

    public static boolean isDimensionWhitelisted(ResourceKey<Level> dimension) {
        return CONFIG.isValidInDimension(dimension);
    }

}
