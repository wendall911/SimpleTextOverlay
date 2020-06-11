package simpletextoverlay.proxy;

import net.minecraftforge.common.MinecraftForge;

import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.handler.ClientEventHandler;
import simpletextoverlay.value.registry.ValueRegistry;

public class ClientProxy {

    public ClientProxy() {
        MinecraftForge.EVENT_BUS.register(GameOverlayEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        ValueRegistry.INSTANCE.init();
    }

}

