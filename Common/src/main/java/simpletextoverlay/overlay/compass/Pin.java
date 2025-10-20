package simpletextoverlay.overlay.compass;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class Pin extends PinInfo<Pin> {

    private Vec3 position;

    public Pin() {
        super(PinInfoRegistry.TYPE, PinType.NONE.toString());
    }

    public Pin(PinInfoType<? extends Pin> type, Vec3 exactPosition, PinType id) {
        super(type, id.toString());

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
        if (tag.getDouble("X").isPresent()
                && tag.getDouble("Y").isPresent()
                && tag.getDouble("Z").isPresent()) {
            double x = tag.getDouble("X").get();
            double y = tag.getDouble("Y").get();
            double z = tag.getDouble("Z").get();

            position = new Vec3(x, y, z);
        }
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

    public enum PinType {
        BEDSPAWN,
        LASTDEATH,
        NONE,
        WORLDSPAWN;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

}
