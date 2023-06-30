package simpletextoverlay.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import simpletextoverlay.config.OverlayConfig;

public class FontHelper {

    public static void draw(Minecraft mc, GuiGraphics guiGraphics, Component label, int x, int y, int color) {
        guiGraphics.drawString(mc.font, label, x, y, color, OverlayConfig.textShadow());
    }
    
    public static void draw(Minecraft mc, GuiGraphics guiGraphics, String label, int x, int y, int color) {
        draw(mc, guiGraphics, label, x, y, color, OverlayConfig.textShadow());
    }

    public static void draw(Minecraft mc, GuiGraphics guiGraphics, String label, int x, int y, int color, boolean shadow) {
        guiGraphics.drawString(mc.font, label, x, y, color, shadow);
    }

}
