package simpletextoverlay.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import simpletextoverlay.event.PlayerEventHandler;
import simpletextoverlay.network.PacketHandler;

public class CommonProxy {

    public void preInit(final FMLPreInitializationEvent event) {
        PacketHandler.init();
    }

    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    public void postInit(final FMLPostInitializationEvent event) {
    }

    public void serverStarting(final FMLServerStartingEvent event) {
    }

    public void serverStopping(final FMLServerStoppingEvent event) {
    }

}
