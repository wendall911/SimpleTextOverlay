package simpletextoverlay;

import net.fabricmc.api.ClientModInitializer;

import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.overlay.OverlayManager;

public class SimpleTextOverlayClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GameOverlayEventHandler.init();
        OverlayManager.INSTANCE.init();
    }

}
