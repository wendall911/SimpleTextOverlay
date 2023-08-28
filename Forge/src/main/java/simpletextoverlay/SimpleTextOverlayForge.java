package simpletextoverlay;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import simpletextoverlay.network.NetworkManager;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlayForge {

    public SimpleTextOverlayForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);

        SimpleTextOverlay.init();
        SimpleTextOverlay.initConfig();
        NetworkManager.setup();
    }

    private void setup(final FMLCommonSetupEvent event) {
        //NetworkManager.setup();
    }

}
