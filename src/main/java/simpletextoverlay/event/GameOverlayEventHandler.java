package simpletextoverlay.event;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.SimpleTextOverlay;

@EventBusSubscriber(modid=SimpleTextOverlay.MODID, value=Dist.CLIENT)
public class GameOverlayEventHandler {

    private final OverlayManager overlayManager = OverlayManager.INSTANCE;
    public static final GameOverlayEventHandler INSTANCE = new GameOverlayEventHandler();

    private static boolean enabled = false;

    private IIngameOverlay OVERLAY;

    static {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(INSTANCE::onLoadComplete);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(INSTANCE::onModConfigReloading);
    }

    public GameOverlayEventHandler() {
        OVERLAY = OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HUD_TEXT_ELEMENT, SimpleTextOverlay.MODID + ":overlay", this::callRenderOverlay);
    }

    public void callRenderOverlay(ForgeIngameGui gui, PoseStack matrix, float partialTicks, int width, int height) {
        if (enabled && OverlayConfig.enabled() && !Minecraft.getInstance().options.renderDebug) {
            overlayManager.renderOverlay(gui, matrix, partialTicks, width, height);
        }
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        enabled = true;
        overlayManager.init();
    }

    public void onModConfigReloading(ModConfigEvent.Reloading event) {
        if (enabled && event.getConfig().getSpec() == OverlayConfig.CONFIG_SPEC) {
            overlayManager.init();
            OverlayRegistry.enableOverlay(OVERLAY, OverlayConfig.enabled());
        }
    }

}
