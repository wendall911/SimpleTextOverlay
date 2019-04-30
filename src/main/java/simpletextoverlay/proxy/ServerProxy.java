package simpletextoverlay.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import simpletextoverlay.handler.PlayerHandler;

public class ServerProxy extends CommonProxy {
    @Override
    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerHandler());
    }
}
