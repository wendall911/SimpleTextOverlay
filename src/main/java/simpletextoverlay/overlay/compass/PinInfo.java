package simpletextoverlay.overlay.compass;

import javax.annotation.Nullable;
import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.overlay.compass.PinInfoType;

public abstract class PinInfo<T extends PinInfo<T>> {

    private final PinInfoType<? extends T> type;
    @Nullable
    private DataManager.Pins pins;
    private String internalId;
    @Nullable
    private boolean isServerProvided = true;

    public PinInfo(PinInfoType<? extends T> type, String id) {
        this.type = type;
        this.internalId = id;
    }

    public PinInfoType<? extends T> getType() {
        return type;
    }

    @Nullable
    public final DataManager.Pins getOwner() {
        return pins;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String id) {
        internalId = id;
    }

    public abstract Vec3 getPosition();

    public void makeClientPin() {
        isServerProvided = false;
    }

    void setPins(@Nullable DataManager.Pins pins) {
        this.pins = pins;
    }

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

    public static Vec3 toVec3d(BlockPos pos) {
        return new Vec3(pos.getX() + 0.5, pos.getY() + 0.5,pos.getZ() + 0.5);
    }

}
