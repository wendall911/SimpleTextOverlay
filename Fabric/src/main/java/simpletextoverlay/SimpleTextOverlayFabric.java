package simpletextoverlay;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import simpletextoverlay.overlay.compass.DataManager;

public class SimpleTextOverlayFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        SimpleTextOverlay.init();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            DataManager.clearCache();
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            DataManager.clearCache();
        });
    }

}
