package simpletextoverlay.overlay.compass;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.registries.IForgeRegistry;

import simpletextoverlay.overlay.compass.PinInfo;
import simpletextoverlay.overlay.compass.PinInfoType;

import simpletextoverlay.SimpleTextOverlay;

public class PinInfoRegistry {

    public static IForgeRegistry<PinInfoType<?>> REGISTRY = SimpleTextOverlay.PIN_INFO_TYPES_REGISTRY.get();

    public static CompoundTag serializePin(@Nonnull PinInfo<?> pinData) {
        PinInfoType type = pinData.getType();
        ResourceLocation typeId = type.getRegistryName();

        if(typeId == null) {
            throw new IllegalStateException(String.format("Serializer name is null %s", type.getClass().getName()));
        }

        CompoundTag tag = new CompoundTag();
        tag.putString("Type", type.getRegistryName().toString());

        return pinData.write(tag);
    }

    @Nonnull
    public static PinInfo<?> deserializePin(CompoundTag tag) {
        ResourceLocation typeId = new ResourceLocation(tag.getString("Type"));
        PinInfoType<?> type = REGISTRY.getValue(typeId);

        if (type == null) {
            throw new IllegalStateException(String.format("Serializer not registered %s", typeId));
        }

        PinInfo<?> info = type.create();
        info.read(tag);

        return info;
    }

    public static void serializePin(PinInfo<?> pinData, FriendlyByteBuf buffer) {
        PinInfoType type = pinData.getType();

        buffer.writeRegistryIdUnsafe(REGISTRY, type);
        pinData.writeToPacket(buffer);
    }

    @Nonnull
    public static PinInfo<?> deserializePin(FriendlyByteBuf buffer) {
        PinInfoType<?> serializer = buffer.readRegistryIdUnsafe(REGISTRY);

        if (serializer == null) {
            throw new IllegalStateException("Server returned unknown serializer");
        }

        PinInfo<?> info = serializer.create();
        info.readFromPacket(buffer);

        return info;
    }

}
