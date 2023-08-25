package simpletextoverlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.Minecraft;

import simpletextoverlay.component.SimpleTextOverlayComponents;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.platform.Services;

public class SimpleTextOverlayClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GameOverlayEventHandler.init();
        OverlayManager.INSTANCE.init();

        ClientPlayNetworking.registerGlobalReceiver(SimpleTextOverlayComponents.STO_DATA, (client, handler, buf, responseSender) -> {
            client.execute(() -> Services.CAPABILITY_PLATFORM.getDataManagerCapability(Minecraft.getInstance().player).ifPresent(data -> {
                data.read(buf);
            }));
        });
    }

}
