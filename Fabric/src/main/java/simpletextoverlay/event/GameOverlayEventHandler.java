package simpletextoverlay.event;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.Minecraft;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.OverlayManager;

public class GameOverlayEventHandler implements HudRenderCallback {

    private final OverlayManager overlayManager = OverlayManager.INSTANCE;

    public static void init() {
        HudRenderCallback.EVENT.register(new GameOverlayEventHandler());
    }

    @Override
    public void onHudRender(PoseStack matrix, float partialTicks) {
        if (OverlayConfig.loaded && !Minecraft.getInstance().options.renderDebug) {
            overlayManager.renderOverlay(matrix, partialTicks);
        }
    }

}