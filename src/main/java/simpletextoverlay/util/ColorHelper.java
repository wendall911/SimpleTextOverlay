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

        int dRed = dark.getRed() - bright.getRed();
        int dGreen = dark.getGreen() - bright.getGreen();
        int dBlue = dark.getBlue() - bright.getBlue();

        step = step + 1;

        return new Color(
            bright.getRed() + ((dRed * step) / 16),
            bright.getGreen() + ((dGreen * step) / 16),
            bright.getBlue() + ((dBlue * step) / 16)).getRGB();
    }

    public static Color decode(String color) {
        return Color.decode(color);
    }

}
