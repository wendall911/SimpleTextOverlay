package simpletextoverlay.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import simpletextoverlay.config.OverlayConfig;

public class FontHelper {

    public static void draw(Minecraft mc, GuiGraphics guiGraphics, Component label, int x, int y, int color, TextType textType) {
        drawBackdrop(mc, guiGraphics, mc.font.width(label), x, y, color, textType);
        guiGraphics.drawString(mc.font, label, x, y, color, OverlayConfig.textShadow());
    }
    
    public static void draw(Minecraft mc, GuiGraphics guiGraphics, String label, int x, int y, int color, TextType textType) {
        draw(mc, guiGraphics, label, x, y, color, OverlayConfig.textShadow(), textType);
    }

    public static void draw(Minecraft mc, GuiGraphics guiGraphics, String label, int x, int y, int color, boolean shadow, TextType textType) {
        drawBackdrop(mc, guiGraphics, mc.font.width(label), x, y, color, textType);
        guiGraphics.drawString(mc.font, label, x, y, color, shadow);
    }

    private static void drawBackdrop(Minecraft mc, GuiGraphics guiGraphics, int textWidth, int x, int y, int color, TextType textType) {
        int leftOffset = 2;
        int rightOffset = 2;

        switch (textType) {
            case LABEL -> rightOffset = 0;
            case VALUE -> leftOffset = 0;
        }
        if (textWidth != 0 && !mc.options.backgroundForChatOnly().get()) {
            int backgroundColor = mc.options.getBackgroundColor(0);

            if (backgroundColor != 0) {
                guiGraphics.fill(x - leftOffset, y - 1, x + textWidth + rightOffset, y + 7 + 2, FastColor.ARGB32.multiply(backgroundColor, color));
            }
        }
    }

    public enum TextType {
        NONE(),
        LABEL(),
        VALUE()
    }

}
