package simpletextoverlay.overlay.compass;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

public class Pins {

    @Nullable
    private final ResourceKey<DimensionType> dimensionTypeKey;
    private final Map<String, PinInfo<?>> pins = new HashMap<>();

    public Pins(@Nullable ResourceKey<DimensionType> dimensionTypeKey) {
        this.dimensionTypeKey = dimensionTypeKey;
    }

    public Map<String, PinInfo<?>> getPins() {
        return pins;
    }

    public void read(ListTag nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            CompoundTag pinTag = nbt.getCompound(i);
            PinInfo<?> pin = PinInfoRegistry.deserializePin(pinTag);

            pins.put(pin.getInternalId(), pin);
        }
    }

    public ListTag write() {
        ListTag tag = new ListTag();

        for (PinInfo<?> pin : pins.values()) {
            tag.add(PinInfoRegistry.serializePin(pin));
        }

        return tag;
    }

    public void addPin(PinInfo<?> pin) {
        pins.put(pin.getInternalId(), pin);
    }

    public void removePin(PinInfo<?> pin) {
        removePin(pin.getInternalId());
    }

    public void removePin(String id) {
        PinInfo<?> pin = pins.get(id);

        if (pin != null) {
            pins.remove(pin.getInternalId());
        }
    }

    @Nullable
    public ResourceKey<DimensionType> getDimensionTypeKey() {
        return dimensionTypeKey;
    }

}
