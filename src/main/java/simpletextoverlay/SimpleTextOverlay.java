package simpletextoverlay;

import java.util.function.Supplier;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.overlay.compass.Pin;
import simpletextoverlay.overlay.compass.PinInfoType;
import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.network.NetworkManager;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlay {

    public static final String MODID = "simpletextoverlay";

    public static final Logger LOGGER = LogManager.getFormatterLogger(MODID);

    public static SimpleTextOverlay INSTANCE;
    public static IEventBus BUS;

    public static final DeferredRegister<PinInfoType<?>> PIN_INFO_TYPES = DeferredRegister.create(Generic.<PinInfoType<?>>from(PinInfoType.class), MODID);
    public static final Supplier<IForgeRegistry<PinInfoType<?>>> PIN_INFO_TYPES_REGISTRY = PIN_INFO_TYPES
            .makeRegistry("pin_info_types", () -> new RegistryBuilder<PinInfoType<?>>().disableSaving());

    public SimpleTextOverlay() {
        BUS = FMLJavaModLoadingContext.get().getModEventBus();
        INSTANCE = this;

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, OverlayConfig.CONFIG_SPEC);

        BUS.addListener(INSTANCE::onRegisterCapabilities);
        BUS.addListener(INSTANCE::onSetup);
        BUS.addGenericListener(PinInfoType.class, this::pinInfoTypes);

        PIN_INFO_TYPES.register(BUS);
    }

    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        DataManager.init(event);
    }

    public void onSetup(final FMLCommonSetupEvent event) {
        NetworkManager.setup();
    }

    public void pinInfoTypes(RegistryEvent.Register<PinInfoType<?>> event) {
        event.getRegistry().registerAll(
            new PinInfoType<>(Pin::new).setRegistryName("pin")
        );
    }

    private static class Generic {

        public static <T extends IForgeRegistryEntry<T>> Class<T> from(Class<? super T> cls) {
            return (Class<T>) cls;
        }

    }

}
