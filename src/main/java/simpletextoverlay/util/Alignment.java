package simpletextoverlay.util;

import simpletextoverlay.config.OverlayConfig;

public class Alignment {

    public static int getX(int screenWidth, int textWidth) {
        String pos = OverlayConfig.position();
        int x = OverlayConfig.offsetX();

        if (pos.endsWith("RIGHT")) {
            x *= -1;
            x = x + screenWidth - textWidth;
        }

        return x;
    }

    public static int getY(int screenHeight, int lineNum, int lineHeight) {
        String pos = OverlayConfig.position();
        int y = OverlayConfig.offsetY() - 1;

        if (pos.startsWith("BOTTOM")) {
            y *= -1;
            y = y + screenHeight - (lineNum * (lineHeight + 1));
        }
        else {
            y = y + ((lineNum - 1) * lineHeight + 1);
        }

        return y;
    }

    public static int getCompassX(int screenWidth, int textWidth) {
        return (screenWidth - textWidth) / 2;
    }

    public static int getCompassY() {
        return OverlayConfig.offsetY();
    }

}
