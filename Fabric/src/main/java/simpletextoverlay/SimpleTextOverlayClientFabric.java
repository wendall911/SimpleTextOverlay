package simpletextoverlay;

import io.netty.buffer.Unpooled;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.network.SyncData;
import simpletextoverlay.overlay.compass.DataManager;

public class SimpleTextOverlayClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GameOverlayEventHandler.init();

        ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(SimpleTextOverlay.MODID, DataManager.STO_DATA), (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                SyncData syncData = new SyncData(new byte[buf.readableBytes()]);

                DataManager.read(new FriendlyByteBuf(Unpooled.wrappedBuffer(syncData.bytes)));
            });
        });
    }

}
