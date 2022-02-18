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

        int bRed = bright.getRed() - dark.getRed();
        int bGreen = bright.getGreen() - dark.getGreen();
        int bBlue = bright.getBlue() - bright.getBlue();

        step = step + 1;

        return new Color(
            dark.getRed() + ((bRed * step) / 16),
            dark.getGreen() + ((bGreen * step) / 16),
            dark.getBlue() + ((bBlue * step) / 16)).getRGB();
    }

    public static Color decode(String color) {
        return Color.decode(color);
    }

}
