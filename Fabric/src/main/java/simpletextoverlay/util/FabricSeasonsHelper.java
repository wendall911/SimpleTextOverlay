package simpletextoverlay.util;

//import static io.github.lucaargolo.seasons.FabricSeasons.CONFIG;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import simpletextoverlay.config.OverlayConfig;

public class FabricSeasonsHelper {
    public static int getSeasonDuration() {
        // Hardcoding for now so this  will work if they update
        //return CONFIG.getSeasonLength();
        return 672000;

    }

    public static boolean isDimensionWhitelisted(ResourceKey<Level> dimension) {
        return OverlayConfig.hasSeasonDimension(dimension.location().toString());
    }

}
