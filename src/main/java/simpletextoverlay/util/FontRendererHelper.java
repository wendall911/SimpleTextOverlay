package simpletextoverlay.util;

import net.minecraft.client.gui.FontRenderer;

public class FontRendererHelper {

    public static void drawLeftAlignedString(final FontRenderer fontRenderer, final String str, final int x, final int y, final int color) {
        fontRenderer.drawShadow(str, x, y, color);
    }

    public static void drawCenteredString(final FontRenderer fontRenderer, final String str, final int x, final int y, final int color) {
        fontRenderer.drawShadow(str, x - fontRenderer.width(str) / 2, y, color);
    }

    public static void drawRightAlignedString(final FontRenderer fontRenderer, final String str, final int x, final int y, final int color) {
        fontRenderer.drawShadow(str, x - fontRenderer.width(str), y, color);
    }

}
