package simpletextoverlay.proxy;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.network.NetworkManager;
import simpletextoverlay.overlay.compass.Pin;
import simpletextoverlay.overlay.compass.PinInfoType;
import simpletextoverlay.SimpleTextOverlay;

@Mod.EventBusSubscriber(modid = SimpleTextOverlay.MODID)
public class CommonProxy {

    public CommonProxy() {}

    public static final ResourceKey<Registry<PinInfoType<?>>> PIN_INFO_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(SimpleTextOverlay.MODID, "pin_info_types"));
    public static final DeferredRegister<PinInfoType<?>> PIN_INFO_TYPES =
            DeferredRegister.create(PIN_INFO_KEY, SimpleTextOverlay.MODID);
    public static final Supplier<IForgeRegistry<PinInfoType<?>>> PIN_INFO_TYPES_REGISTRY = PIN_INFO_TYPES
            .makeRegistry(Generic.from(PinInfoType.class), () -> new RegistryBuilder<PinInfoType<?>>().disableSaving());

    public void start() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        OverlayConfig.init();
        registerListeners(bus);
        bus.addGenericListener(PinInfoType.class, this::pinInfoTypes);
        PIN_INFO_TYPES.register(bus);
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

    private static class Generic {

        public static <T extends IForgeRegistryEntry<T>> Class<T> from(Class<? super T> cls) {
            return (Class<T>) cls;
        }

    }

}
