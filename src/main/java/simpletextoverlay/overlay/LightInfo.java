package simpletextoverlay.overlay;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.ColorHelper;
import simpletextoverlay.util.Font;

public class LightInfo extends Info {

    public LightInfo(String label, int lineNum) {
        super(label, lineNum);
    }

    @Override
    public void renderText(PoseStack matrix, Minecraft mc, BlockPos pos, int scaledWidth, int scaledHeight) {
        int brightnessVal = mc.getCameraEntity().getLevel().getBrightness(LightLayer.SKY, pos);
        String brightness = String.valueOf(brightnessVal);

        int x = Alignment.getX(scaledWidth, mc.font.width(super.label + brightness));
        int y = Alignment.getY(scaledHeight, super.lineNum, mc.font.lineHeight);

        Font.draw(mc, matrix, super.label, x, y, OverlayConfig.labelColor().getRGB());

        x = x + mc.font.width(super.label);

        Font.draw(mc, matrix, brightness, x, y, ColorHelper.getLightColor(brightnessVal));
    }

}
