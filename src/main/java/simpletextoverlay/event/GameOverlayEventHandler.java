package simpletextoverlay.event;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.tag.Tag;

@Mod.EventBusSubscriber(modid = SimpleTextOverlay.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GameOverlayEventHandler {

    public static final GameOverlayEventHandler INSTANCE = new GameOverlayEventHandler();

    public static boolean enabled = false;
    public static boolean forceDebug = false;

    private final Minecraft client = Minecraft.getInstance();
    private final OverlayManager overlayManager = OverlayManager.INSTANCE;
    private boolean firstJoin = false;
    private String lastOverlayFile = "";

    private GameOverlayEventHandler() {}

    @SubscribeEvent
    public void onEntityJoinWorld(final EntityJoinWorldEvent event) {
        if (!firstJoin) {
            enabled = true;
            firstJoin = true;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderGameOverlayEventPre(final RenderGameOverlayEvent.Pre event) {
        if (replaceDebugger()) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.DEBUG) {
                if (!overlayManager.getOverlayFile().equals(OverlayConfig.CLIENT.debugOverlayFile.get())) {
                    lastOverlayFile = overlayManager.getOverlayFile();
                    overlayManager.loadOverlayFile(OverlayConfig.CLIENT.debugOverlayFile.get(), false);
                }

                event.setCanceled(true);
            }

            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT &&
                    !isDebugger() &&
                    overlayManager.getOverlayFile().equals(OverlayConfig.CLIENT.debugOverlayFile.get())) {
                overlayManager.loadOverlayFile(lastOverlayFile, false);
            }
        }

        if ( enabled && !OverlayConfig.CLIENT.showOverlayPotions.get() &&
                event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            event.setCanceled(true);
        }
    }

    private boolean isDebugger() {
        //return client.options.renderDebug || //SHIFT+<F3
        return client.options.renderDebug; //<F3>
    }

    private boolean replaceDebugger() {
        if (forceDebug) {
            return true;
        }

        return enabled && OverlayConfig.CLIENT.replaceDebug.get();
    }

    private boolean canRun() {
        if (client.options != null && enabled) {
            if (!OverlayConfig.CLIENT.showOnPlayerList.get() &&
                    InputMappings.isKeyDown(client.getWindow().getWindow(), 258)) {
                return false;
            }

            if (!OverlayConfig.CLIENT.replaceDebug.get() &&
                    client.options.renderDebug) {
                return false;
            }

            if (client.options.hideGui) {
                return false;
            }

            return client.screen == null || OverlayConfig.CLIENT.showInChat.get() &&
                client.screen instanceof ChatScreen;

        }

        return false;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        if (canRun()) {
            overlayManager.updateTagValues();
            overlayManager.renderOverlay();
        }
        if (!enabled || client.options == null) {
            Tag.releaseResources();
        }
    }

}
