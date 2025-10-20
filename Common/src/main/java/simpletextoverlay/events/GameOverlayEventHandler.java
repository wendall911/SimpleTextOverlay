package simpletextoverlay.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.util.LocalPlayerHelper;

public class GameOverlayEventHandler {

    public static void onHudRender(GuiGraphics guiGraphics, float partialTicks) {
        OverlayManager overlayManager = OverlayManager.INSTANCE;
        Minecraft mc = Minecraft.getInstance();

        if (!LocalPlayerHelper.shouldLoad()) {
            SimpleTextOverlay.LOGGER.warn("Overlay not loaded due to player state. Disabling overlays.");

            return;
        }

        if (OverlayConfig.loaded && !mc.getDebugOverlay().showDebugScreen() && !mc.options.hideGui) {
            overlayManager.renderOverlay(guiGraphics, partialTicks);
        }
    }

}
