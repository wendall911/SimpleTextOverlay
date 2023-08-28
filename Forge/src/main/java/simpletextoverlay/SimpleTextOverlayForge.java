package simpletextoverlay;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import simpletextoverlay.client.ModKeyBindings;
import simpletextoverlay.event.KeyEventHandler;
import simpletextoverlay.network.NetworkManager;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlayForge {

    private static boolean setupDone = false;

    public SimpleTextOverlayForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::keyBindingSetup);
        bus.addListener(this::clientSetup);

        SimpleTextOverlay.init();
        SimpleTextOverlay.initConfig();
        NetworkManager.init();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyEventHandler());
    }

    public void keyBindingSetup(RegisterKeyMappingsEvent event) {
        if (!setupDone && ModList.get().isLoaded("corpse")) {
            ModKeyBindings.init(event);
            setupDone = true;
        }
    }

}
