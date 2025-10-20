package simpletextoverlay.events;

import java.util.Map;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayer.RespawnConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

import simpletextoverlay.overlay.compass.Pin.PinType;
import simpletextoverlay.overlay.compass.PinInfo;
import simpletextoverlay.platform.Services;
import simpletextoverlay.util.PinHelper;

public class SimpleTextOverlayEvents {

    public static void onEntityJoinLevel(Player player) {
        if (player != null && !player.level().isClientSide()) {
            ServerPlayer sp = (ServerPlayer) player;

            initPins(sp);
        }
    }

    public static void onPlayerChangeDimension(Player player, ResourceKey<Level> worldKey) {
        if (!player.level().isClientSide()) {
            final ServerPlayer sp = (ServerPlayer) player;

            if (!worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
                    BlockPos spawnPos = new BlockPos((int) sp.getX(), (int) sp.getY(), (int) sp.getZ());

                    PinHelper.setPointPin(sp, pinsData, worldKey, spawnPos, PinType.WORLDSPAWN);

                    Services.CAPABILITY_PLATFORM.syncData(sp);
                });
            }
            else {
                initPins(sp);
            }
        }
    }

    public static void initPins(ServerPlayer sp) {
        Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
            ResourceKey<Level> worldKey = sp.level().dimension();
            Optional<GlobalPos> lastDeathLocation = sp.getLastDeathLocation();
            final Map<String, PinInfo<?>> pins = pinsData.get(sp).getPins();
            PinInfo<?> worldSpawn = pins.get(PinType.WORLDSPAWN.toString());
            RespawnConfig respawnConfig = sp.getRespawnConfig();

            if (worldSpawn == null && worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                BlockPos spawnPos = sp.level().getServer().getWorldData().overworldData().getRespawnData().pos();
                PinHelper.setPointPin(sp, pinsData, worldKey, spawnPos, PinType.WORLDSPAWN);
            }

            if (respawnConfig != null) {
                PinHelper.setPointPin(sp, pinsData, respawnConfig.respawnData().dimension(), respawnConfig.respawnData().pos(), PinType.BEDSPAWN);
            }

            lastDeathLocation.ifPresent(globalPos -> PinHelper.setPointPin(sp, pinsData, globalPos.dimension(), globalPos.pos(), PinType.LASTDEATH));

            Services.CAPABILITY_PLATFORM.syncData(sp);
        });
    }

}
