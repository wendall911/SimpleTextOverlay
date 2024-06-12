package simpletextoverlay;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import simpletextoverlay.network.SyncDataPacket;
import simpletextoverlay.overlay.compass.DataManager;

public class SimpleTextOverlayFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(SyncDataPacket.TYPE, SyncDataPacket.STREAM_CODEC);

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            DataManager.resetCache();
        });
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            DataManager.resetCache();
        });
    }

}
