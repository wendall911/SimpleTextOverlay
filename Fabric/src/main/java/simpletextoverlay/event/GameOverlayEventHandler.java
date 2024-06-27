package simpletextoverlay.event;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.OverlayManager;

public class GameOverlayEventHandler implements HudRenderCallback {

    private final OverlayManager overlayManager = OverlayManager.INSTANCE;

    public static void init() {
        HudRenderCallback.EVENT.register(new GameOverlayEventHandler());
    }

    @Override
    public void onHudRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (OverlayConfig.loaded && !Minecraft.getInstance().getDebugOverlay().showDebugScreen()) {
            overlayManager.renderOverlay(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(true));
        }
    }

}
