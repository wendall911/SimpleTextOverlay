package simpletextoverlay;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import simpletextoverlay.overlay.compass.DataManager;

public class SimpleTextOverlayFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            DataManager.resetCache();
        });
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            DataManager.resetCache();
        });
    }

}
