package simpletextoverlay.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.overlay.compass.PinInfoType;

public class PinInfoRegistry {

    public static final ResourceKey<Registry<PinInfoType<?>>> PIN_INFO_KEY = ResourceKey.createRegistryKey(new ResourceLocation(SimpleTextOverlay.MODID, "pin_info_types"));
    public static final RegistryProvider<PinInfoType<?>> TIME_EFFECT_REGISTRY = RegistryProvider.get(PIN_INFO_KEY, SimpleTextOverlay.MODID, true);

    public static final RegistryObject<TimeEffect> WEATHER_EFFECT;
    public static final RegistryObject<TimeEffect> RANDOM_TICK_EFFECT;
    public static final RegistryObject<TimeEffect> POTION_EFFECT;
    public static final RegistryObject<TimeEffect> HUNGER_EFFECT;
    public static final RegistryObject<TimeEffect> BLOCK_ENTITY_EFFECT;

    static {
        WEATHER_EFFECT = TIME_EFFECT_REGISTRY.register("weather", WeatherSleepEffect::new);
        RANDOM_TICK_EFFECT = TIME_EFFECT_REGISTRY.register("random_tick", RandomTickSleepEffect::new);
        POTION_EFFECT = TIME_EFFECT_REGISTRY.register("potion", PotionTimeEffect::new);
        HUNGER_EFFECT = TIME_EFFECT_REGISTRY.register("hunger", HungerTimeEffect::new);
        BLOCK_ENTITY_EFFECT = TIME_EFFECT_REGISTRY.register("block_entity", BlockEntityTimeEffect::new);
    }

    public static void init() {}

}
