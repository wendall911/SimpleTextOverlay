package simpletextoverlay.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class VecMath {

    public static Vec2 angleFromPos(Vec3 pos, double posX, double posY, double posZ) {
        double xd = pos.x - posX;
        double yd = pos.y - posY;
        double zd = pos.z - posZ;

        return new Vec2((float) Math.toDegrees(-Math.atan2(xd, zd)), (float)yd);
    }

    public static float angleDistance(float yaw, float angle) {
        float dist = angle - yaw;

        if (dist > 0) {
            return dist > 180 ? (dist - 360) : dist;
        }

        return dist < -180 ? (dist + 360) : dist;
    }

    public static boolean isCloserThan(BlockPos pos, int radius) {
        Vec3 position = convertBlockPos(pos);

        return position.closerThan(position, (double)radius);
    }

    public static Vec3 convertBlockPos(BlockPos pos) {
        return new Vec3((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }

}
