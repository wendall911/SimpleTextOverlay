package simpletextoverlay.overlay.compass;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import simpletextoverlay.registry.RegistryObject;
import simpletextoverlay.registry.RegistryProvider;
import simpletextoverlay.SimpleTextOverlay;

public class PinInfoRegistry {

    public static final BiMap<ResourceLocation, PinInfoType<?>> typesMap = HashBiMap.create();
    public static final BiMap<Integer, PinInfoType<?>> idsMap = HashBiMap.create();

    public static final ResourceKey<Registry<PinInfoType<?>>> PIN_INFO_KEY = ResourceKey.createRegistryKey(new ResourceLocation(SimpleTextOverlay.MODID, "pin_info_types"));
    public static final RegistryProvider<PinInfoType<?>> PIN_INFO_REGISTRY = RegistryProvider.get(PIN_INFO_KEY, SimpleTextOverlay.MODID, true);
    public static final RegistryObject<PinInfoType<?>> PIN_INFO_TYPE_REGISTRY_OBJECT;
    private static final ResourceLocation LOCATION = new ResourceLocation(SimpleTextOverlay.MODID, "pin");
    public static PinInfoType<Pin> TYPE = new PinInfoType<>(Pin::new, LOCATION);

    static {
        PIN_INFO_TYPE_REGISTRY_OBJECT = PIN_INFO_REGISTRY.register("pin", () -> TYPE);

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
        tag.putString("Type", type.getRegistryName().toString());

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
