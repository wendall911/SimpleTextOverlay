package simpletextoverlay.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;

public abstract class Info {

    public String label;
    public int lineNum;

    protected Info(String label, int lineNum) {
        this.label = label;
        this.lineNum = lineNum;
    }

    public abstract void renderText(GuiGraphicsExtractor matrix, Minecraft mc, BlockPos pos, int scaledWidth, int scaledHeight);

}
