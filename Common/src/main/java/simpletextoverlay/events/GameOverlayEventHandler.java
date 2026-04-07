package simpletextoverlay.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.util.LocalPlayerHelper;

public class GameOverlayEventHandler {

    public static void onHudRender(GuiGraphicsExtractor guiGraphics, float partialTicks) {
        OverlayManager overlayManager = OverlayManager.INSTANCE;
        Minecraft mc = Minecraft.getInstance();

        if (!LocalPlayerHelper.shouldLoad()) {
            return;
        }

        if (OverlayConfig.loaded && !mc.options.hideGui) {
            overlayManager.renderOverlay(guiGraphics, partialTicks);
        }
    }

}
