package simpletextoverlay.util;

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

    public static void setPointPin(Player player, DataManager pinsData, ResourceKey<Level> worldKey, @Nullable BlockPos pos, String type) {
        PointPin point = new PointPin(worldKey, pos, type);

        pinsData.get(player, point.worldKey).addPin(point.pin);
    }

    public static class PointPin {

        @Nullable
        public Pin pin;
        public ResourceKey<Level> worldKey;
        public BlockPos pos;

        public PointPin(ResourceKey<Level> worldKey, BlockPos pos, String type) {
            this.worldKey = worldKey;
            this.pos = pos;
            this.pin = new Pin(PinInfoRegistry.TYPE, Vec3.atCenterOf(pos), type);
        }

    }

}
