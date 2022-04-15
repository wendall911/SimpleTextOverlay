package simpletextoverlay.overlay.compass;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import simpletextoverlay.overlay.compass.PinInfo;
import simpletextoverlay.overlay.compass.PinInfoType;

public class PinInfoRegistry {

    public static IForgeRegistry<PinInfoType<?>> REGISTRY = RegistryManager.ACTIVE.getRegistry(PinInfoType.class);

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nonnull
    public static CompoundTag serializePin(@Nonnull PinInfo<?> pinData) {
        PinInfoType type = pinData.getType();
        ResourceLocation typeId = type.getRegistryName();

        if(typeId == null) {
            throw new IllegalStateException(String.format("Serializer name is null %s", type.getClass().getName()));
        }

        CompoundTag tag = new CompoundTag();
        tag.putString("Type", type.getRegistryName().toString());
        tag = pinData.write(tag);

        return tag;
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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void serializePin(PinInfo<?> pinData, FriendlyByteBuf buffer) {
        PinInfoType type = pinData.getType();

        buffer.writeRegistryIdUnsafe(REGISTRY, type);
        pinData.writeToPacket(buffer);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void serializePinWithoutId(PinInfo<?> pinData, FriendlyByteBuf buffer) {
        PinInfoType type = pinData.getType();

        buffer.writeRegistryIdUnsafe(REGISTRY, type);
        pinData.writeToPacketWithoutId(buffer);
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

    @Nonnull
    public static PinInfo<?> deserializePinWithoutId(FriendlyByteBuf buffer) {
        PinInfoType<?> serializer = buffer.readRegistryIdUnsafe(REGISTRY);

        if (serializer == null) {
            throw new IllegalStateException("Server returned unknown serializer");
        }

        PinInfo<?> info = serializer.create();
        info.readFromPacketWithoutId(buffer);

        return info;
    }

}
