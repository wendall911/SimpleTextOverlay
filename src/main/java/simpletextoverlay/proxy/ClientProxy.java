package simpletextoverlay.proxy;

import net.minecraftforge.common.MinecraftForge;

import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.value.registry.ValueRegistry;

public class ClientProxy {

    public ClientProxy() {
        MinecraftForge.EVENT_BUS.register(GameOverlayEventHandler.INSTANCE);
        ValueRegistry.INSTANCE.init();
    }

}

