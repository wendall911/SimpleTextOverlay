package simpletextoverlay.overlay;

import java.util.Locale;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.FontHelper;

public class DaysInfo extends Info {

    public DaysInfo(String label, int lineNum) {
        super(label, lineNum);
    }

    @Override
    public void renderText(GuiGraphics guiGraphics, Minecraft mc, BlockPos pos, int scaledWidth, int scaledHeight) {
        String numberDays = String.format(Locale.ENGLISH, "%d", Math.max(Objects.requireNonNull(mc.level).getDayTime() / 24000, 1));

        int x = Alignment.getX(scaledWidth, mc.font.width(super.label + numberDays));
        int y = Alignment.getY(scaledHeight, super.lineNum, mc.font.lineHeight);

        FontHelper.draw(mc, guiGraphics, super.label, x, y, OverlayConfig.labelColor().getRGB(), FontHelper.TextType.LABEL);

        x = x + mc.font.width(super.label);

        FontHelper.draw(mc, guiGraphics, numberDays, x, y, OverlayConfig.daysColor().getRGB(), FontHelper.TextType.VALUE);
    }

}
