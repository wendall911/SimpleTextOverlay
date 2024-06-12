package simpletextoverlay.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.SimpleTextOverlay;

public class GameOverlayEventHandler implements LayeredDraw.Layer {

    private final OverlayManager overlayManager = OverlayManager.INSTANCE;
    public static final GameOverlayEventHandler INSTANCE = new GameOverlayEventHandler();

    public void onRegisterOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(new ResourceLocation(SimpleTextOverlay.MODID, "overlay"), INSTANCE);
    }

    @Override
    public void render(GuiGraphics guiGraphics, float partialTick) {
        if (OverlayConfig.loaded && !Minecraft.getInstance().getDebugOverlay().showDebugScreen()) {
            overlayManager.renderOverlay(guiGraphics, partialTick);
        }
    }
}
