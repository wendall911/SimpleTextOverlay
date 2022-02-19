package simpletextoverlay.util;

import java.awt.Color;

import simpletextoverlay.config.OverlayConfig;

public class ColorHelper {

    public static int rgb(int r, int g, int b, int a) {
        return new Color(r, g, b, a).getRGB();
    }

    public static int getLightColor(int step) {
        Color bright = OverlayConfig.lightColorBright();
        Color dark = OverlayConfig.lightColorDark();

        return getRangeColor(dark, bright, 16, step + 1);
    }

    public static int getTimeColor(long hour, long minute) {
        Color bright = OverlayConfig.timeColorBright();
        Color dark = OverlayConfig.timeColorDark();

        //Sunrise/Sunset
        if (hour == 5 || hour == 18) {
            if (hour == 5) {
                return getRangeColor(dark, bright, 60, (int)minute + 1);
            }
            else {
                return getRangeColor(bright, dark, 60, (int)minute + 1);
            }
        }
        //Night
        else if (hour < 5 || hour > 18) {
            return dark.getRGB();
        }

        //Day
        return bright.getRGB();
    }

    public static Color decode(String color) {
        return Color.decode(color);
    }

    private static int getRangeColor(Color from, Color to, int steps, int step) {
        int diffRed = to.getRed() - from.getRed();
        int diffGreen = to.getGreen() - from.getGreen();
        int diffBlue = to.getBlue() - from.getBlue();

        return new Color(
            from.getRed() + ((diffRed * step) / steps),
            from.getGreen() + ((diffGreen * step) / steps),
            from.getBlue() + ((diffBlue * step) / steps)).getRGB();
    }

}
