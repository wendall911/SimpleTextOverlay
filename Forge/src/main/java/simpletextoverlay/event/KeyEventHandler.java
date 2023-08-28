package simpletextoverlay.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

import simpletextoverlay.client.ModKeyBindings;
import simpletextoverlay.network.NetworkManager;
import simpletextoverlay.network.RequestDeathHistory;

@OnlyIn(Dist.CLIENT)
public class KeyEventHandler {

    @SubscribeEvent
    public void onInput(InputEvent.Key event) {
        if (ModList.get().isLoaded("corpse")) {
            if (ModKeyBindings.KEY_DEATH_HISTORY.consumeClick()) {
                NetworkManager.INSTANCE.sendToServer(new RequestDeathHistory());
            }
        }
    }

}