package simpletextoverlay.util;

import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import simpletextoverlay.overlay.compass.Pin;
import simpletextoverlay.overlay.compass.DataManager;

public class PinHelper {

    public static PointPin getPointPin(DataManager pinsData, ResourceKey<Level> worldKey, @Nullable BlockPos pos, String type) {
        String loc = worldKey.location() + "_" + type;
        PointPin point = pinsData.getOrCreatePinData(loc, PointPin::new);

        boolean posChanged = !Objects.equals(point.position, pos);
        boolean hasPin = point.pin != null;

        if (hasPin && (posChanged || pos == null)) {
            pinsData.get(point.worldKey).removePin(point.pin);
        }

        if (pos != null) {
            point.worldKey = worldKey;
            point.position = pos;
            point.pin = new Pin(Pin.TYPE, Vec3.atCenterOf(pos), type);
        }

        return point;
    }

    public static void setPointPin(DataManager pinsData, PointPin point) {
        pinsData.get(point.worldKey).addPin(point.pin);
    }

    public static class PointPin {

        @Nullable
        public Pin pin;
        public ResourceKey<Level> worldKey;
        public BlockPos position;

    }

}
