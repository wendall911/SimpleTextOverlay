package simpletextoverlay.overlay;

import java.util.Objects;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.ColorHelper;
import simpletextoverlay.util.FontHelper;

public class LightInfo extends Info {

    public LightInfo(String label, int lineNum) {
        super(label, lineNum);
    }

    @Override
    public void renderText(PoseStack matrix, Minecraft mc, BlockPos pos, int scaledWidth, int scaledHeight) {
        Level level = Objects.requireNonNull(mc.getCameraEntity()).getLevel();
        int brightnessVal;
        int skyLight;

        if (level.dimensionType().hasSkyLight()) {
            int blockLight = level.getChunkSource().getLightEngine().getRawBrightness(pos.above(), 15);

            int i = level.getBrightness(LightLayer.SKY, pos.above()) - level.getSkyDarken();
            float f = level.getSunAngle(1.0F);
            if (i > 0) {
                float f1 = f < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
                f += (f1 - f) * 0.2F;
                i = Math.round((float)i * Mth.cos(f));
            }
            skyLight = Mth.clamp(i, 0, 15);

            if (level.isRainingAt(pos)) {
                if (level.isThundering()) {
                    skyLight -= 3;
                }
                else {
                    skyLight -= 2;
                }
            }

            brightnessVal = Math.max(blockLight, skyLight);

            String brightness = String.valueOf(brightnessVal);

            int x = Alignment.getX(scaledWidth, mc.font.width(super.label + brightness));
            int y = Alignment.getY(scaledHeight, super.lineNum, mc.font.lineHeight);

            FontHelper.draw(mc, matrix, super.label, x, y, OverlayConfig.labelColor().getRGB(), FontHelper.TextType.LABEL);

            x = x + mc.font.width(super.label);

            FontHelper.draw(mc, matrix, brightness, x, y, ColorHelper.getLightColor(brightnessVal), FontHelper.TextType.VALUE);
        }
    }

}
