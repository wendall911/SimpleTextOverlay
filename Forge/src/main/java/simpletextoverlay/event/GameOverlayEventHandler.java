package simpletextoverlay.event;

import net.minecraft.client.Minecraft;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.SimpleTextOverlay;

@EventBusSubscriber(modid=SimpleTextOverlay.MODID, value=Dist.CLIENT)
public class GameOverlayEventHandler {

    private final OverlayManager overlayManager = OverlayManager.INSTANCE;
    public static final GameOverlayEventHandler INSTANCE = new GameOverlayEventHandler();

    private final IGuiOverlay OVERLAY;

    static {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(INSTANCE::onRegisterOverlays);
    }

    public GameOverlayEventHandler() {
        OVERLAY = (gui, guiGraphics, partialTick, width, height) -> {
            if (!OverlayConfig.loaded) {
                OverlayConfig.init();
            }
            if (OverlayConfig.loaded && !Minecraft.getInstance().getDebugOverlay().showDebugScreen()) {
                overlayManager.renderOverlay(guiGraphics, partialTick);
            }
        };
    }
    public void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(SimpleTextOverlay.MODID + "_overlay", INSTANCE.OVERLAY);
    }

}
