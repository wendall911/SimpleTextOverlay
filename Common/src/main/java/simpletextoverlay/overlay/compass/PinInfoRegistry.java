package simpletextoverlay.overlay.compass;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

import simpletextoverlay.SimpleTextOverlay;

public class PinInfoRegistry {

    public static final BiMap<Identifier, PinInfoType<?>> typesMap = HashBiMap.create();
    public static final BiMap<Integer, PinInfoType<?>> idsMap = HashBiMap.create();

    private static final Identifier LOCATION = Identifier.fromNamespaceAndPath(SimpleTextOverlay.MODID, "pin");
    public static PinInfoType<Pin> TYPE = new PinInfoType<>(Pin::new, LOCATION);

    static {
        typesMap.put(LOCATION, TYPE);
        idsMap.put(1, TYPE);
    }

    public static void init() {}

    public static CompoundTag serializePin(@NotNull PinInfo<?> pinData) {
        PinInfoType<?> type = pinData.getType();
        Identifier typeId = type.getName();

        if(typeId == null) {
            throw new IllegalStateException(String.format("Serializer name is null %s", type.getClass().getName()));
        }

        CompoundTag tag = new CompoundTag();
        tag.putString("PinType", type.getName().toString());

        return pinData.write(tag);
    }

    @NotNull
    public static PinInfo<?> deserializePin(CompoundTag tag) {
        if (tag.getString("PinType").isPresent()) {
            Identifier typeId = Identifier.bySeparator(tag.getString("PinType").get(), ':');
            PinInfoType<?> type = typesMap.get(typeId);

            if (type == null) {
                throw new IllegalStateException(String.format("Serializer not registered %s", typeId));
            }
            PinInfo<?> info = type.create();
            info.read(tag);

            return info;
        }
        else {
            throw new IllegalStateException("Pin type not specified in tag");
        }
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
