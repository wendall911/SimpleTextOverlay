package simpletextoverlay.overlay;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import simpletextoverlay.config.OverlayConfig;

public class InfoItem extends Info {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    private final ItemStack itemStack;
    private final boolean large;
    private final int size;

    public InfoItem(final ItemStack itemStack) {
        this(itemStack, false);
    }

    public InfoItem(final ItemStack itemStack, final boolean large) {
        this(itemStack, large, 0, 0);
    }

    public InfoItem(final ItemStack itemStack, final boolean large, final int x, final int y) {
        super(x, y);
        this.itemStack = itemStack;
        this.large = large;
        this.size = large ? 16 : 8;
        if (large) {
            this.y = -4;
        }
    }

    @Override
    public void drawInfo() {
        if (!this.itemStack.isEmpty()) {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableRescaleNormal();
            RenderHelper.setupForFlatItems();

            RenderSystem.translatef(getX(), getY(), 0);
            if (!this.large) {
                RenderSystem.scaled(0.5f, 0.5f, 0.5f);
            }

            final ItemRenderer renderItem = MINECRAFT.getItemRenderer();
            final float zLevel = renderItem.blitOffset;
            renderItem.blitOffset = 300;
            renderItem.renderGuiItem(this.itemStack, 0, 0);

            if (OverlayConfig.CLIENT.showOverlayItemIcons.get()) {
                renderItem.renderGuiItemDecorations(MINECRAFT.font, this.itemStack, 0, 0, "");
            }

            renderItem.blitOffset = zLevel;

            if (!this.large) {
                RenderSystem.scaled(2.0f, 2.0f, 2.0f);
            }
            RenderSystem.translatef(-getX(), -getY(), 0);

            RenderHelper.turnOff();
            RenderSystem.disableRescaleNormal();
            RenderSystem.disableBlend();
        }
    }

    @Override
    public int getWidth() {
        return !this.itemStack.isEmpty() ? this.size : 0;
    }

    @Override
    public int getHeight() {
        return !this.itemStack.isEmpty() ? this.size : 0;
    }

    @Override
    public String toString() {
        return String.format("InfoItem{itemStack: %s, x: %d, y: %d, offsetX: %d, offsetY: %d, children: %s}", this.itemStack, this.x, this.y, this.offsetX, this.offsetY, this.children);
    }

}
