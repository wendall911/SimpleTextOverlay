package simpletextoverlay.proxy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import simpletextoverlay.network.NetworkManager;
import simpletextoverlay.SimpleTextOverlay;

@Mod.EventBusSubscriber(modid = SimpleTextOverlay.MODID)
public class CommonProxy {

    public CommonProxy() {}

    public void start() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        registerListeners(bus);
    }

    public void registerListeners(IEventBus bus) {
        bus.register(RegistryListener.class);
    }

    public void pinInfoTypes(RegistryEvent.Register<PinInfoType<?>> event) {
        event.getRegistry().registerAll(
            new PinInfoType<>(Pin::new).setRegistryName("pin")
        );
    }

    public static final class RegistryListener {

        @SubscribeEvent
        public static void setup(FMLCommonSetupEvent event) {
            NetworkManager.setup();
        }

    }

}
