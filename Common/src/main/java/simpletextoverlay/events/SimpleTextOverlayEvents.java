package simpletextoverlay.events;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayer.RespawnConfig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

import simpletextoverlay.overlay.compass.Pin.PinType;
import simpletextoverlay.overlay.compass.PinInfo;
import simpletextoverlay.platform.Services;
import simpletextoverlay.util.PinHelper;

public class SimpleTextOverlayEvents {

    public static void onEntityJoinLevel(ServerPlayer sp) {
        onPlayerChangeDimension(sp);
    }

    public static void onPlayerChangeDimension(ServerPlayer sp) {
        initPins(sp);
    }

    public static void initPins(ServerPlayer sp) {
        if (sp == null) {
            return;
        }

        Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
            ResourceKey<Level> dimension = sp.level().dimension();
            boolean isOverworld = dimension.identifier().toString().contains(BuiltinDimensionTypes.OVERWORLD.identifier().toString());
            Optional<GlobalPos> lastDeathLocation = sp.getLastDeathLocation();
            PinInfo<?> worldSpawn = pinsData.get(sp).getPin(PinType.WORLDSPAWN.toString());
            RespawnConfig respawnConfig = sp.getRespawnConfig();

            if (worldSpawn == null && isOverworld) {
                BlockPos spawnPos = sp.level().getServer().getWorldData().overworldData().getRespawnData().pos();

                PinHelper.setPointPin(sp, pinsData, dimension, spawnPos, PinType.WORLDSPAWN);
            }
            else if (!isOverworld) {
                BlockPos spawnPos = new BlockPos((int) sp.getX(), (int) sp.getY(), (int) sp.getZ());

                PinHelper.setPointPin(sp, pinsData, dimension, spawnPos, PinType.WORLDSPAWN);
            }

            if (respawnConfig != null) {
                PinHelper.setPointPin(sp, pinsData, respawnConfig.respawnData().dimension(), respawnConfig.respawnData().pos(), PinType.BEDSPAWN);
            }

            lastDeathLocation.ifPresent(globalPos -> PinHelper.setPointPin(sp, pinsData, globalPos.dimension(), globalPos.pos(), PinType.LASTDEATH));

            Services.CAPABILITY_PLATFORM.syncData(sp);
        });
    }

}
