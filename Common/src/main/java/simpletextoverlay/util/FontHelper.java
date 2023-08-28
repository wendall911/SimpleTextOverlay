package simpletextoverlay.util;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;

import simpletextoverlay.config.OverlayConfig;

public class FontHelper {

    public static void draw(Minecraft mc, PoseStack matrix, TranslatableComponent label, int x, int y, int color) {
        if (OverlayConfig.textShadow()) {
            mc.font.drawShadow(matrix, label, x, y, color);
        }
        else {
            mc.font.draw(matrix, label, x, y, color);
        }
    }
    
    public static void draw(Minecraft mc, PoseStack matrix, String label, int x, int y, int color) {
        draw(mc, matrix, label, x, y, color, OverlayConfig.textShadow());
    }

    public static void draw(Minecraft mc, PoseStack matrix, String label, int x, int y, int color, boolean shadow) {
        if (shadow) {
            mc.font.drawShadow(matrix, label, x, y, color);
        }
        else {
            mc.font.draw(matrix, label, x, y, color);
        }
    }

}
