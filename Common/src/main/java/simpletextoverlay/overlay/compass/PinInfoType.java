package simpletextoverlay.overlay.compass;

import java.util.function.Supplier;

import net.minecraft.resources.Identifier;

public final class PinInfoType<T extends PinInfo<T>> {

    private final Supplier<T> factory;
    private final Identifier name;

    public PinInfoType(Supplier<T> factory, Identifier name) {
        this.factory = factory;
        this.name = name;
    }

    public Identifier getName() {
        return this.name;
    }

    public T create() {
        return factory.get();
    }

}
