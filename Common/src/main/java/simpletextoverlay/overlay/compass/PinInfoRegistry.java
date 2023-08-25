package simpletextoverlay.overlay.compass;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import simpletextoverlay.SimpleTextOverlay;

public class PinInfoRegistry {

    public static final BiMap<ResourceLocation, PinInfoType<?>> typesMap = HashBiMap.create();
    public static final BiMap<Integer, PinInfoType<?>> idsMap = HashBiMap.create();

    private static final ResourceLocation LOCATION = new ResourceLocation(SimpleTextOverlay.MODID, "pin");
    public static PinInfoType<Pin> TYPE = new PinInfoType<>(Pin::new, LOCATION);

    static {
        typesMap.put(LOCATION, TYPE);
        idsMap.put(1, TYPE);
    }

    public static void init() {}

    public static CompoundTag serializePin(@NotNull PinInfo<?> pinData) {
        PinInfoType<?> type = pinData.getType();
        ResourceLocation typeId = type.getName();

        if(typeId == null) {
            throw new IllegalStateException(String.format("Serializer name is null %s", type.getClass().getName()));
        }

        CompoundTag tag = new CompoundTag();
        tag.putString("Type", type.getName().toString());

        return pinData.write(tag);
    }

    @NotNull
    public static PinInfo<?> deserializePin(CompoundTag tag) {
        ResourceLocation typeId = new ResourceLocation(tag.getString("Type"));
        PinInfoType<?> type = typesMap.get(typeId);

        if (type == null) {
            throw new IllegalStateException(String.format("Serializer not registered %s", typeId));
        }

        PinInfo<?> info = type.create();
        info.read(tag);

        return info;
    }

    public static void serializePin(PinInfo<?> pinData, FriendlyByteBuf buffer) {
        PinInfoType<?> type = pinData.getType();
        int id = idsMap.inverse().get(type);

        buffer.writeVarInt(id);
        pinData.writeToPacket(buffer);
    }

    @NotNull
    public static PinInfo<?> deserializePin(FriendlyByteBuf buffer) {
        int id = buffer.readVarInt();
        PinInfoType<?> serializer = idsMap.get(id);

        if (serializer == null) {
            throw new IllegalStateException("Server returned unknown serializer");
        }

        PinInfo<?> info = serializer.create();
        info.readFromPacket(buffer);

        return info;
    }

}
