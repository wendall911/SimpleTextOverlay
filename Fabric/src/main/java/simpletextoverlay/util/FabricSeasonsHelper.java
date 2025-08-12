package simpletextoverlay.util;

/*
 * Wait until Fabric Seasons is available
 */
//import static io.github.lucaargolo.seasons.FabricSeasons.CONFIG;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class FabricSeasonsHelper {

    public static int getSeasonDuration() {
        //return CONFIG.getSpringLength();
        return 0; // Placeholder, as Fabric Seasons is not implemented yet
    }

    public static boolean isDimensionWhitelisted(ResourceKey<Level> dimension) {
        //return CONFIG.isValidInDimension(dimension);
        return false; // Placeholder, as Fabric Seasons is not implemented yet
    }

}
