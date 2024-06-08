package simpletextoverlay.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import simpletextoverlay.client.ModKeyBindings;
import simpletextoverlay.network.RequestDeathHistory;

public class KeyEventHandler {

    @SubscribeEvent
    public void onInput(InputEvent.Key event) {
        if (ModList.get().isLoaded("corpse")) {
            if (ModKeyBindings.KEY_DEATH_HISTORY.consumeClick()) {
                PacketDistributor.SERVER.noArg().send(new RequestDeathHistory());
            }
        }
    }

}
