package simpletextoverlay.event;

import net.minecraft.client.Minecraft;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPauseChangeEvent;

import simpletextoverlay.attachments.AttachmentDataManager;

public class LocalPlayerEventHandler {

    /*
     * Yet another hack workaround for not giving context for the player. I'd have to rewrite the entire mod to work with
     * how NeoForge decided to implement AttachmentsData not allowing me to get a context for the entity that it is
     * attached to in the network request. This is "faster" garbage at this point.
     */
    @SubscribeEvent
    public void onClientPause(ClientPauseChangeEvent.Pre event) {
        //AttachmentDataManager.DATA_MANAGER_INSTANCE.setPlayer(Minecraft.getInstance().player);
    }

}
