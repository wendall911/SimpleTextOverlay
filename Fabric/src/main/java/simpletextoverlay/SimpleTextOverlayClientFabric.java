package simpletextoverlay;

import io.netty.buffer.Unpooled;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.network.FabricSyncData;
import simpletextoverlay.platform.Services;

public class SimpleTextOverlayClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GameOverlayEventHandler.init();

        ClientPlayNetworking.registerGlobalReceiver(FabricSyncData.TYPE, (payload, context) -> {
            Minecraft mc = context.client();
            Services.CAPABILITY_PLATFORM.getDataManagerCapability(mc.player).ifPresent(data -> {
                mc.execute(() -> data.read(Minecraft.getInstance().player, new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.getSyncData().bytes))));
            });
        });
    }

}
