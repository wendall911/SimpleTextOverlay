package simpletextoverlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;

import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.network.SyncDataPacket;
import simpletextoverlay.platform.Services;

public class SimpleTextOverlayClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GameOverlayEventHandler.init();

        ClientPlayNetworking.registerGlobalReceiver(SyncDataPacket.TYPE, (payload, context) -> {
            Minecraft mc = context.client();

            Services.CAPABILITY_PLATFORM.getDataManagerCapability(mc.player).ifPresent(data -> {
                mc.execute(() -> data.readSyncData(Minecraft.getInstance().player, payload.data()));
            });
        });

    }

}
