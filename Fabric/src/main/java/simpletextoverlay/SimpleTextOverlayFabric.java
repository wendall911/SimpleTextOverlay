package simpletextoverlay;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import simpletextoverlay.network.SyncDataPacket;

public class SimpleTextOverlayFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(SyncDataPacket.TYPE, SyncDataPacket.STREAM_CODEC);
    }

}
