package simpletextoverlay.events;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

import simpletextoverlay.overlay.compass.PinInfo;
import simpletextoverlay.platform.Services;
import simpletextoverlay.util.PinHelper;

public class SimpleTextOverlayEvents {

    public static final String BEDSPAWN = "bedspawn";
    public static final String LASTDEATH = "lastdeath";
    public static final String WORLDSPAWN = "worldspawn";

    public static Map<ResourceKey<Level>, Map<String, PinHelper.PointPin>> PINS_CACHE = new HashMap<>();

    public static void onEntityJoinLevel(Player player) {
        if (player != null && !player.level.isClientSide) {
            ServerPlayer sp = (ServerPlayer) player;

            Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
                ResourceKey<Level> worldKey = sp.getRespawnDimension();
                BlockPos spawnPos = sp.getLevel().getSharedSpawnPos();
                Map<String, PinHelper.PointPin> cachedPins = PINS_CACHE.computeIfAbsent(worldKey, k -> new HashMap<>());
                final Map<String, PinInfo<?>> currentPins = pinsData.get(worldKey).getPins();

                PinHelper.PointPin bedPin = cachedPins.get(BEDSPAWN);

                if (currentPins.get(WORLDSPAWN) == null && worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                    PinHelper.PointPin spawnPin = PinHelper.getPointPin(pinsData, worldKey, spawnPos, WORLDSPAWN);

                    PinHelper.setPointPin(pinsData, spawnPin);
                }

                if (bedPin != null && bedPin.pin != null) {
                    PinHelper.setPointPin(pinsData, bedPin);
                }
                else if (currentPins.get(BEDSPAWN) == null && sp.getRespawnPosition() != null) {
                    bedPin = PinHelper.getPointPin(pinsData, worldKey, sp.getRespawnPosition(), BEDSPAWN);

                    PinHelper.setPointPin(pinsData, bedPin);
                }

                /*
                 * Check all dimensions for death pin. This ensures if player dies in another dimension,
                 * death location is captured.
                 */
                PINS_CACHE.forEach((key, value) -> {
                    PinHelper.PointPin lastDeathPin = value.get(LASTDEATH);

                    if (lastDeathPin != null && lastDeathPin.pin != null) {
                        PinHelper.setPointPin(pinsData, lastDeathPin);
                    }
                });

                // Reset Cache
                PINS_CACHE = new HashMap<>();

                Services.CAPABILITY_PLATFORM.syncData(sp);
            });
        }
    }

    public static void onPlayerChangeDimension(Player player, ResourceKey<Level> worldKey) {
        if (!player.level.isClientSide) {
            final ServerPlayer sp = (ServerPlayer) player;

            if (!worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
                    BlockPos spawnPos = new BlockPos(sp.getX(), sp.getY(), sp.getZ());
                    PinHelper.PointPin portalPin = PinHelper.getPointPin(pinsData, worldKey, spawnPos, WORLDSPAWN);

                    PinHelper.setPointPin(pinsData, portalPin);

                    Services.CAPABILITY_PLATFORM.syncData(sp);
                });
            }
        }
    }

    public static void onPlayerDeath(LivingEntity entity) {
        if (!(entity instanceof ServerPlayer sp)) {
            return;
        }

        if (sp.level.isClientSide) {
            return;
        }

        Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
            ResourceKey<Level> worldKey = sp.level.dimension();
            BlockPos deathPos = new BlockPos(sp.getX(), sp.getY(), sp.getZ());

            PINS_CACHE.computeIfAbsent(worldKey, k -> new HashMap<>()).put(LASTDEATH, PinHelper.getPointPin(pinsData, worldKey, deathPos, LASTDEATH));
        });
    }

}
