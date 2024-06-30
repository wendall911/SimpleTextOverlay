package simpletextoverlay.overlay;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.FontHelper;

public class FootInfo extends Info {

    public FootInfo(String label, int lineNum) {
        super(label, lineNum);
    }

    @Override
    public void renderText(GuiGraphics guiGraphics, Minecraft mc, BlockPos pos, int scaledWidth, int scaledHeight) {
        String footLevel = String.valueOf((int) Objects.requireNonNull(mc.getCameraEntity()).getY());
        int x = Alignment.getX(scaledWidth, mc.font.width(super.label + footLevel));
        int y = Alignment.getY(scaledHeight, super.lineNum, mc.font.lineHeight);

        FontHelper.draw(mc, guiGraphics, super.label, x, y, OverlayConfig.labelColor().getRGB(), FontHelper.TextType.LABEL);

        x = x + mc.font.width(super.label);

        FontHelper.draw(mc, guiGraphics, footLevel, x, y, OverlayConfig.footColor().getRGB(), FontHelper.TextType.VALUE);
    }

}
