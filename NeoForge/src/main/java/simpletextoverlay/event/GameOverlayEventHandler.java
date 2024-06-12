package simpletextoverlay.event;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.SimpleTextOverlay;

public class GameOverlayEventHandler {

    private final OverlayManager overlayManager = OverlayManager.INSTANCE;
    public static final GameOverlayEventHandler INSTANCE = new GameOverlayEventHandler();

    private final IGuiOverlay OVERLAY;

    public GameOverlayEventHandler() {
        OVERLAY = (gui, guiGraphics, partialTick, width, height) -> {
            if (OverlayConfig.loaded && !Minecraft.getInstance().getDebugOverlay().showDebugScreen()) {
                overlayManager.renderOverlay(guiGraphics, partialTick);
            }
        };
    }

    public void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(new ResourceLocation(SimpleTextOverlay.MODID, "overlay"), INSTANCE.OVERLAY);
    }

}
