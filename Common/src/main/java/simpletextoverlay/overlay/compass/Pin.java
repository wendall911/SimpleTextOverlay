package simpletextoverlay.overlay.compass;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class Pin extends PinInfo<Pin> {

    @ObjectHolder("simpletextoverlay:pin")
    public static PinInfoType<Pin> TYPE = null;

    private Vec3 position;

    public Pin() {
        super(PinInfoRegistry.TYPE, "none");
    }

    public Pin(PinInfoType<? extends Pin> type, Vec3 exactPosition, String id) {
        super(type, id);

        this.position = exactPosition;
    }

    @Override
    public Vec3 getPosition() {
        return position;
    }

    @Override
    protected void serializeAdditional(CompoundTag tag) {
        tag.putDouble("X", position.x);
        tag.putDouble("Y", position.y);
        tag.putDouble("Z", position.z);
    }

    @Override
    protected void deserializeAdditional(CompoundTag tag) {
        position = new Vec3(
            tag.getDouble("X"),
            tag.getDouble("Y"),
            tag.getDouble("Z")
        );
    }

    @Override
    protected void serializeAdditional(FriendlyByteBuf buffer) {
        buffer.writeDouble(position.x);
        buffer.writeDouble(position.y);
        buffer.writeDouble(position.z);
    }

    @Override
    protected void deserializeAdditional(FriendlyByteBuf buffer) {
        position = new Vec3(
            buffer.readDouble(),
            buffer.readDouble(),
            buffer.readDouble()
        );
    }

}
