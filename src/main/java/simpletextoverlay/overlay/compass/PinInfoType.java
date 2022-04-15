package simpletextoverlay.overlay.compass;

import java.util.function.Supplier;

import net.minecraftforge.registries.ForgeRegistryEntry;

public final class PinInfoType<T extends PinInfo<T>> extends ForgeRegistryEntry<PinInfoType<?>> {

    private final Supplier<T> factory;

    public PinInfoType(Supplier<T> factory) {
        this.factory = factory;
    }

    public final T create() {
        return factory.get();
    }

}
