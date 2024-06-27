package simpletextoverlay.util;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import simpletextoverlay.overlay.compass.Pin;
import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.overlay.compass.PinInfoRegistry;

public class PinHelper {

    public static PointPin getPointPin(Player player, DataManager pinsData, ResourceKey<Level> worldKey, @Nullable BlockPos pos, String type) {
        String loc = worldKey.location() + "_" + type;
        PointPin point = pinsData.getOrCreatePinData(loc, PointPin::new);

        boolean posChanged = !Objects.equals(point.position, pos);
        boolean hasPin = point.pin != null;

        if (hasPin && (posChanged || pos == null)) {
            pinsData.get(player, point.worldKey).removePin(player.getUUID(), point.pin);
        }

        if (pos != null) {
            point.worldKey = worldKey;
            point.position = pos;
            point.pin = new Pin(PinInfoRegistry.TYPE, Vec3.atCenterOf(pos), type);
        }

        return point;
    }

    public static void setPointPin(Player player, DataManager pinsData, PointPin point) {
        pinsData.get(player, point.worldKey).addPin(player.getUUID(), point.pin);
    }

    public static class PointPin {

        @Nullable
        public Pin pin;
        public ResourceKey<Level> worldKey;
        public BlockPos position;

    }

}
