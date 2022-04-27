package simpletextoverlay.overlay.compass;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.overlay.compass.PinInfoType;

public abstract class PinInfo<T extends PinInfo<T>> {

    private final PinInfoType<? extends T> type;
    private String internalId;

    public PinInfo(PinInfoType<? extends T> type, String id) {
        this.type = type;
        this.internalId = id;
    }

    public PinInfoType<? extends T> getType() {
        return type;
    }

    public String getInternalId() {
        return internalId;
    }

    public abstract Vec3 getPosition();

    public final CompoundTag write(CompoundTag tag) {
        tag.putString("ID", internalId);
        serializeAdditional(tag);

        return tag;
    }

    public final void read(CompoundTag tag) {
        internalId = tag.getString("ID");
        deserializeAdditional(tag);
    }

    public final void writeToPacket(FriendlyByteBuf buffer) {
        buffer.writeUtf(internalId, 1024);
        writeToPacketWithoutId(buffer);
    }

    public final void writeToPacketWithoutId(FriendlyByteBuf buffer) {
        serializeAdditional(buffer);
    }

    public final void readFromPacket(FriendlyByteBuf buffer) {
        internalId = buffer.readUtf();
        readFromPacketWithoutId(buffer);
    }

    public final void readFromPacketWithoutId(FriendlyByteBuf buffer) {
        deserializeAdditional(buffer);
    }

    protected abstract void serializeAdditional(CompoundTag tag);
    protected abstract void deserializeAdditional(CompoundTag tag);
    protected abstract void serializeAdditional(FriendlyByteBuf tag);
    protected abstract void deserializeAdditional(FriendlyByteBuf tag);

}
