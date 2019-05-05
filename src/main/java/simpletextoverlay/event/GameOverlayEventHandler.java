package simpletextoverlay.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.core.Core;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.tag.Tag;

public class GameOverlayEventHandler {
    public static final GameOverlayEventHandler INSTANCE = new GameOverlayEventHandler();

    public static boolean enabled = false;

    private final Minecraft client = Minecraft.getMinecraft();
    private final Core core = Core.INSTANCE;
    private boolean firstJoin = false;

    private GameOverlayEventHandler() {}

    @SubscribeEvent
    public void onEntityJoinWorld(final EntityJoinWorldEvent event) {
        if (!firstJoin) {
            this.enabled = true;
            this.firstJoin = true;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderGameOverlayEventPre(final RenderGameOverlayEvent.Pre event) {
        if (canRun()) {
            if (ConfigHandler.client.general.replaceDebug && event.getType() == RenderGameOverlayEvent.ElementType.DEBUG) {
                event.setCanceled(true);
            }

            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
                event.setCanceled(true);
            }
        }

        if (!ConfigHandler.client.general.showOverlayPotions && event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            event.setCanceled(true);
        }
    }

    // TODO: this requires a bit of optimization... it's just boolean checks mostly but still
    private boolean canRun() {
        if (!enabled) {
            return false;
        }

        //this.client.profiler.profilingEnabled SHIFT+F3
        //this.client.gameSettings.showDebugInfo F3

        return this.client.profiler.profilingEnabled
            || ConfigHandler.client.general.replaceDebug
            || ConfigHandler.client.general.replaceDebug ==
                this.client.gameSettings.showDebugInfo;
    }

    private boolean isRunning() {
        if (!canRun()) {
            return false;
        }

        if (this.client.profiler.profilingEnabled) {
            return true;
        }

        // a && b || !a && !b  -->  a == b
        if (this.client.gameSettings != null && ConfigHandler.client.general.replaceDebug == this.client.gameSettings.showDebugInfo) {
            if (!ConfigHandler.client.general.showOnPlayerList && this.client.gameSettings.keyBindPlayerList.isKeyDown()) {
                return false;
            }

            if (this.client.gameSettings.hideGUI) {
                return false;
            }

            return this.client.currentScreen == null || ConfigHandler.client.general.showInChat && this.client.currentScreen instanceof GuiChat;

        }

        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            if (isRunning()) {
                this.core.updateTagValues();
                this.core.renderOverlay();
            }
            if (!enabled || this.client.gameSettings == null) {
                Tag.releaseResources();
            }
        }
    }

}
