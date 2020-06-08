package simpletextoverlay.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class TagOverlayEventHandler {

    private boolean unregisterTaglist = false;

    private final GuiScreen guiScreen;

    private TagOverlayEventHandler(final GuiScreen guiScreen, final boolean unregister) {
        this.guiScreen = guiScreen;
        this.unregisterTaglist = unregister;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
        if (this.unregisterTaglist) {
            Minecraft.getMinecraft().displayGuiScreen(this.guiScreen);
            MinecraftForge.EVENT_BUS.unregister(this);
            this.unregisterTaglist = false;
        }
    }

    public static void create(final GuiScreen guiScreen, final boolean unregister) {
        MinecraftForge.EVENT_BUS.register(new TagOverlayEventHandler(guiScreen, unregister));
    }

}
