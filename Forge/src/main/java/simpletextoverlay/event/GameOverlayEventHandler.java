package simpletextoverlay.event;

import net.minecraft.client.Minecraft;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(INSTANCE::onLoadComplete);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(INSTANCE::onRegisterOverlays);
    }

    public GameOverlayEventHandler() {
        OVERLAY = (gui, poseStack, partialTick, width, height) -> {
            if (OverlayConfig.loaded && !Minecraft.getInstance().options.renderDebug) {
                overlayManager.renderOverlay(poseStack, partialTick);
            }
        };
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        overlayManager.init();
    }

    public void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(SimpleTextOverlay.MODID + "_overlay", INSTANCE.OVERLAY);
    }

}
