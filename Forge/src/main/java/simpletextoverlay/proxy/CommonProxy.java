package simpletextoverlay.proxy;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

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
            .makeRegistry(() -> new RegistryBuilder<PinInfoType<?>>().disableSaving());
    private static final ResourceLocation LOCATION = new ResourceLocation(SimpleTextOverlay.MODID, "pin");
    public static PinInfoType<Pin> TYPE = new PinInfoType<>(Pin::new, LOCATION);

    public void start() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        OverlayConfig.init();
        registerListeners(bus);
        PIN_INFO_TYPES.register(bus);
    }

    public void registerListeners(IEventBus bus) {
        bus.register(RegistryListener.class);
    }

    public static final class RegistryListener {

        @SubscribeEvent
        public static void setup(FMLCommonSetupEvent event) {
            NetworkManager.init();
        }

        @SubscribeEvent
        public static void registerPin(RegisterEvent event) {
            event.register(Registries.CUSTOM_STAT,cr -> {
                PIN_INFO_TYPES_REGISTRY.get().register("pin", TYPE);
            });
        }

    }

}
