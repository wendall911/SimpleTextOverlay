package simpletextoverlay.event;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
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

    private final IIngameOverlay OVERLAY;

    static {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(INSTANCE::onLoadComplete);
    }

    public GameOverlayEventHandler() {
        OVERLAY = OverlayRegistry.registerOverlayAbove(
            ForgeIngameGui.HUD_TEXT_ELEMENT,
            SimpleTextOverlay.MODID + ":overlay",
            (matrix, partialTicks, width, height, height2) -> callRenderOverlay(partialTicks, width)
        );
    }

    public void callRenderOverlay(PoseStack matrix, float partialTicks) {
        if (OverlayConfig.loaded && !Minecraft.getInstance().options.renderDebug) {
            overlayManager.renderOverlay(matrix, partialTicks);
        }
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        overlayManager.init();
    }

}
