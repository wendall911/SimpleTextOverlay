package simpletextoverlay.overlay;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.FontHelper;

import java.util.Objects;

public class FootInfo extends Info {

    public FootInfo(String label, int lineNum) {
        super(label, lineNum);
    }

    @Override
    public void renderText(PoseStack matrix, Minecraft mc, BlockPos pos, int scaledWidth, int scaledHeight) {
        String footLevel = String.valueOf((int) Objects.requireNonNull(mc.getCameraEntity()).getY());
        int x = Alignment.getX(scaledWidth, mc.font.width(super.label + footLevel));
        int y = Alignment.getY(scaledHeight, super.lineNum, mc.font.lineHeight);

        FontHelper.draw(mc, matrix, super.label, x, y, OverlayConfig.labelColor().getRGB());

        x = x + mc.font.width(super.label);

        FontHelper.draw(mc, matrix, footLevel, x, y, OverlayConfig.footColor().getRGB());
    }

}
