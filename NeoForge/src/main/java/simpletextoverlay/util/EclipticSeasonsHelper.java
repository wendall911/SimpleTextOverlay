package simpletextoverlay.util;

/*
 * Temporarily disabled due to Ecliptic Seasons not being updated to 1.21.2+
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
 */

import net.minecraft.world.level.Level;

public class EclipticSeasonsHelper {

    public static boolean isSeasonDimension(Level level) {
        /*
        SolarTerm solarTerm = EclipticUtil.INSTANCE.getSolarTerm(level);

        return solarTerm != SolarTerm.NONE;
         */
        return false;
    }

    public static SubSeason getSubSeason(Level level) {
        /*
        SolarTerm solarTerm = EclipticUtil.INSTANCE.getSolarTerm(level);

        return SubSeason.values()[(solarTerm.ordinal() / 2)];
         */
        return SubSeason.MID_SPRING;
    }

}
