package simpletextoverlay.client.gui.overlay;

import net.minecraft.client.gui.FontRenderer;

import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.util.FontRendererHelper;

public class InfoText extends Info {

    private final FontRenderer fontRenderer;
    public final String text;
    private String textModified;

    public InfoText(final FontRenderer fontRenderer, final String text) {
        this(fontRenderer, text, 0, 0);
    }

    public InfoText(final FontRenderer fontRenderer, final String text, final int x, final int y) {
        super(x, y, text);
        this.fontRenderer = fontRenderer;
        this.text = text;
        this.textModified = text.replace("SCALESMALL", "");
        this.textModified = this.textModified.replace("CENTER", "");
    }

    @Override
    public void drawInfo() {
        FontRendererHelper.drawLeftAlignedString(this.fontRenderer, this.textModified, getX(), getY(), 0x00FFFFFF);
    }

    @Override
    public int getWidth() {
        return this.fontRenderer.getStringWidth(this.textModified);
    }

    @Override
    public int getHeight() {
        return this.fontRenderer.FONT_HEIGHT;
    }

    @Override
    public int getX() {
        final float scale = (float) ConfigHandler.client.general.scale;
        final int multiplier = (int) (scale / 0.5);
        if (text.contains("SCALESMALL")) {
            return (this.x + this.offsetX) * multiplier;
        }
        return this.x + this.offsetX;
    }

    @Override
    public int getY() {
        final float scale = (float) ConfigHandler.client.general.scale;
        final int multiplier = (int) (scale / 0.5);
        if (text.contains("SCALESMALL")) {
            return (this.y + this.offsetY) * multiplier;
        }
        return this.y + this.offsetY;
    }



    @Override
    public String toString() {
        return String.format("InfoText{text: %s, x: %d, y: %d, offsetX: %d, offsetY: %d, children: %s}", this.textModified, this.x, this.y, this.offsetX, this.offsetY, this.children);
    }

}
