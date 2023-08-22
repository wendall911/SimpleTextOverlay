package simpletextoverlay.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.overlay.compass.Pin;
import simpletextoverlay.overlay.compass.PinInfoType;

public class PinInfoRegistry {

    public static final ResourceKey<Registry<PinInfoType<?>>> PIN_INFO_KEY = ResourceKey.createRegistryKey(new ResourceLocation(SimpleTextOverlay.MODID, "pin_info_types"));
    public static final RegistryProvider<PinInfoType<?>> PIN_INFO_REGISTRY = RegistryProvider.get(PIN_INFO_KEY, SimpleTextOverlay.MODID, true);

    public static final RegistryObject<PinInfoType<?>> PIN_INFO_TYPE_REGISTRY_OBJECT;

    static {
        PIN_INFO_TYPE_REGISTRY_OBJECT = PIN_INFO_REGISTRY.register("pin", () -> new PinInfoType<>(Pin::new));
    }

    public static void init() {}

}
