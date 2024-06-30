package simpletextoverlay.util;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import simpletextoverlay.config.OverlayConfig;

public class FontHelper {

    public static void draw(Minecraft mc, PoseStack matrix, Component label, int x, int y, int color, TextType textType) {
        drawBackdrop(mc, matrix, mc.font.width(label), x, y, color, textType);
        if (OverlayConfig.textShadow()) {
            mc.font.drawShadow(matrix, label, x, y, color);
        }
        else {
            mc.font.draw(matrix, label, x, y, color);
        }
    }
    
    public static void draw(Minecraft mc, PoseStack matrix, String label, int x, int y, int color, TextType textType) {
        draw(mc, matrix, label, x, y, color, OverlayConfig.textShadow(), textType);
    }

    public static void draw(Minecraft mc, PoseStack matrix, String label, int x, int y, int color, boolean shadow, TextType textType) {
        drawBackdrop(mc, matrix, mc.font.width(label), x, y, color, textType);
        if (shadow) {
            mc.font.drawShadow(matrix, label, x, y, color);
        }
        else {
            mc.font.draw(matrix, label, x, y, color);
        }
    }

    private static void drawBackdrop(Minecraft mc, PoseStack matrix, int textWidth, int x, int y, int color, TextType textType) {
        int leftOffset = 2;
        int rightOffset = 2;

        switch (textType) {
            case LABEL -> rightOffset = 0;
            case VALUE -> leftOffset = 0;
        }
        if (textWidth != 0 && !mc.options.backgroundForChatOnly().get()) {
            int backgroundColor = mc.options.getBackgroundColor(0);

            if (backgroundColor != 0) {
                GuiComponent.fill(matrix, x - leftOffset, y - 1, x + textWidth + rightOffset, y + 7 + 2, FastColor.ARGB32.multiply(backgroundColor, color));
            }
        }
    }

    public enum TextType {
        NONE(),
        LABEL(),
        VALUE()
    }

}
