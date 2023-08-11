package simpletextoverlay.proxy;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import simpletextoverlay.client.ModKeyBindings;
import simpletextoverlay.event.KeyEventHandler;

@OnlyIn(Dist.CLIENT)
public final class ClientProxy extends CommonProxy {

    public ClientProxy() {}

    private static boolean setupDone = false;

    @Override
    public void start() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        registerListeners(bus);

        super.start();
    }

    @Override
    public void registerListeners(IEventBus bus) {
        super.registerListeners(bus);

        bus.addListener(this::keyBindingSetup);
        bus.addListener(this::clientSetup);
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
