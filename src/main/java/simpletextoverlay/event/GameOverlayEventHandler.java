package simpletextoverlay.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.client.gui.overlay.OverlayManager;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.tag.Tag;

public class GameOverlayEventHandler {
    public static final GameOverlayEventHandler INSTANCE = new GameOverlayEventHandler();

    public static boolean enabled = false;
    public static boolean forceDebug = false;

    private final Minecraft client = Minecraft.getMinecraft();
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
                if (!overlayManager.getOverlayFile().equals(ConfigHandler.client.general.debugOverlayFile)) {
                    lastOverlayFile = overlayManager.getOverlayFile();
                    overlayManager.loadOverlayFile(ConfigHandler.client.general.debugOverlayFile, false);
                }

                event.setCanceled(true);
            }

            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT &&
                    !isDebugger() &&
                    overlayManager.getOverlayFile().equals(ConfigHandler.client.general.debugOverlayFile)) {
                overlayManager.loadOverlayFile(lastOverlayFile, false);
            }
        }

        if ( enabled && !ConfigHandler.client.general.showOverlayPotions &&
                event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            event.setCanceled(true);
        }
    }

    private boolean isDebugger() {
        return client.profiler.profilingEnabled || //SHIFT+<F3>
            client.gameSettings.showDebugInfo; //<F3>
    }

    private boolean replaceDebugger() {
        if (forceDebug) {
            return true;
        }

        return enabled && ConfigHandler.client.general.replaceDebug;
    }

    private boolean canRun() {
        if (client.gameSettings != null && enabled) {
            if (!ConfigHandler.client.general.replaceDebug &&
                client.profiler.profilingEnabled) {
                return false;
            }

            if (!ConfigHandler.client.general.showOnPlayerList &&
                    client.gameSettings.keyBindPlayerList.isKeyDown()) {
                return false;
            }

            if (!ConfigHandler.client.general.replaceDebug &&
                    client.gameSettings.showDebugInfo) {
                return false;
            }

            if (client.gameSettings.hideGUI) {
                return false;
            }

            return client.currentScreen == null ||
                ConfigHandler.client.general.showInChat &&
                client.currentScreen instanceof GuiChat;

        }

        return false;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        if (canRun()) {
            overlayManager.updateTagValues();
            overlayManager.renderOverlay();
        }
        if (!enabled || client.gameSettings == null) {
            Tag.releaseResources();
        }
    }

}
