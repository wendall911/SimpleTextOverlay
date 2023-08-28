package simpletextoverlay.overlay.compass;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;

public final class PinInfoType<T extends PinInfo<T>> {

    private final Supplier<T> factory;
    private final ResourceLocation name;

    public PinInfoType(Supplier<T> factory, ResourceLocation name) {
        this.factory = factory;
        this.name = name;
    }

    public ResourceLocation getName() {
        return this.name;
    }

    public T create() {
        return factory.get();
    }

}
