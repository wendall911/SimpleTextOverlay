package simpletextoverlay.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.core.Core;
import simpletextoverlay.tag.Tag;

public class Ticker {
    public static final Ticker INSTANCE = new Ticker();

    public static boolean enabled = true;

    private final Minecraft client = Minecraft.getMinecraft();
    private final Core core = Core.INSTANCE;

    private Ticker() {}

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderGameOverlayEventPre(final RenderGameOverlayEvent.Pre event) {
        if (canRun()) {
            if (ConfigHandler.replaceDebug && event.getType() == RenderGameOverlayEvent.ElementType.DEBUG) {
                event.setCanceled(true);
            }

            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
                event.setCanceled(true);
            }
        }

        if (!ConfigHandler.showOverlayPotions && event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        onTick(event);
    }

    @SubscribeEvent
    public void onRenderTick(final TickEvent.RenderTickEvent event) {
        onTick(event);
    }

    // TODO: this requires a bit of optimization... it's just boolean checks mostly but still
    private boolean canRun() {
        if (!enabled) {
            return false;
        }

        return this.client.profiler.profilingEnabled || ConfigHandler.replaceDebug || ConfigHandler.replaceDebug == this.client.gameSettings.showDebugInfo;

    }

    private boolean isRunning() {
        if (!canRun()) {
            return false;
        }

        if (this.client.profiler.profilingEnabled) {
            return true;
        }

        // a && b || !a && !b  -->  a == b
        if (this.client.gameSettings != null && ConfigHandler.replaceDebug == this.client.gameSettings.showDebugInfo) {
            if (!ConfigHandler.showOnPlayerList && this.client.gameSettings.keyBindPlayerList.isKeyDown()) {
                return false;
            }

            if (this.client.gameSettings.hideGUI) {
                return false;
            }

            return this.client.currentScreen == null || ConfigHandler.showInChat && this.client.currentScreen instanceof GuiChat;

        }

        return false;
    }

    private void onTick(final TickEvent event) {
        if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.END) {
            this.client.profiler.startSection("simpletextoverlay");
            if (isRunning()) {
                if (event.type == TickEvent.Type.CLIENT) {
                    this.core.onTickClient();
                } else if (event.type == TickEvent.Type.RENDER) {
                    this.core.onTickRender();
                }
            }

            if ((!enabled || this.client.gameSettings == null) && event.type == TickEvent.Type.CLIENT) {
                Tag.setServer(null);
                Tag.releaseResources();
            }
            this.client.profiler.endSection();
        }
    }

}
