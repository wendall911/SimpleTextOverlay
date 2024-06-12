package simpletextoverlay.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.phys.Vec3;

import simpletextoverlay.overlay.compass.PinInfo;
import simpletextoverlay.platform.Services;
import simpletextoverlay.util.PinHelper;

public class SimpleTextOverlayEvents {

    public static final String BEDSPAWN = "bedspawn";
    public static final String LASTDEATH = "lastdeath";
    public static final String WORLDSPAWN = "worldspawn";

    public static Map<UUID, Map<ResourceKey<Level>, Map<String, PinHelper.PointPin>>> PINS_CACHE = new HashMap<>();

    public static void onEntityJoinLevel(Player player) {
        if (player != null && !player.level().isClientSide) {
            ServerPlayer sp = (ServerPlayer) player;

            Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
                ResourceKey<Level> worldKey = sp.getRespawnDimension();
                BlockPos spawnPos = sp.level().getSharedSpawnPos();
                UUID uuid = sp.getUUID();
                Map<ResourceKey<Level>, Map<String, PinHelper.PointPin>> playerCache = PINS_CACHE.computeIfAbsent(uuid, k -> new HashMap<>());
                Map<String, PinHelper.PointPin> cachedPins = playerCache.computeIfAbsent(worldKey, k -> new HashMap<>());
                final Map<String, PinInfo<?>> currentPins = pinsData.get(sp, worldKey).getPins(sp.getUUID());

                PinHelper.PointPin bedPin = cachedPins.get(BEDSPAWN);

                if (currentPins.get(WORLDSPAWN) == null && worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                    PinHelper.PointPin spawnPin = PinHelper.getPointPin(sp, pinsData, worldKey, spawnPos, WORLDSPAWN);

                    PinHelper.setPointPin(sp, pinsData, spawnPin);
                }

                if (bedPin != null && bedPin.pin != null) {
                    PinHelper.setPointPin(sp, pinsData, bedPin);
                }
                else if (currentPins.get(BEDSPAWN) == null && sp.getRespawnPosition() != null) {
                    bedPin = PinHelper.getPointPin(sp, pinsData, worldKey, sp.getRespawnPosition(), BEDSPAWN);

                    PinHelper.setPointPin(sp, pinsData, bedPin);
                }

                /*
                 * If we have this, let's set it, this may be a first join, not a join after death. Either way ...
                 */
                if (currentPins.get(LASTDEATH) != null) {
                    Vec3 pos = currentPins.get(LASTDEATH).getPosition();
                    BlockPos deathPos = new BlockPos((int) pos.x(), (int) pos.y(), (int) pos.z());
                    PinHelper.PointPin lastDeathPin = PinHelper.getPointPin(sp, pinsData, worldKey, deathPos, LASTDEATH);

                    PinHelper.setPointPin(sp, pinsData, lastDeathPin);
                }

                /*
                 * Check all dimensions for death pin. This ensures if player dies in another dimension,
                 * death location is captured.
                 */
                playerCache.forEach((key, value) -> {
                    PinHelper.PointPin lastDeathPin = value.get(LASTDEATH);

                    if (lastDeathPin != null && lastDeathPin.pin != null) {
                        PinHelper.setPointPin(sp, pinsData, lastDeathPin);
                    }
                });

                // Reset Cache
                PINS_CACHE.remove(uuid);

                Services.CAPABILITY_PLATFORM.syncData(sp);
            });
        }
    }

    public static void onPlayerChangeDimension(Player player, ResourceKey<Level> worldKey) {
        if (!player.level().isClientSide) {
            final ServerPlayer sp = (ServerPlayer) player;

            if (!worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
                    BlockPos spawnPos = new BlockPos((int) sp.getX(), (int) sp.getY(), (int) sp.getZ());
                    PinHelper.PointPin portalPin = PinHelper.getPointPin(sp, pinsData, worldKey, spawnPos, WORLDSPAWN);

                    PinHelper.setPointPin(sp, pinsData, portalPin);

                    Services.CAPABILITY_PLATFORM.syncData(sp);
                });
            }
        }
    }

    public static void onPlayerDeath(LivingEntity entity) {
        if (!(entity instanceof ServerPlayer sp)) {
            return;
        }

        if (sp.level().isClientSide) {
            return;
        }

        Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
            ResourceKey<Level> worldKey = sp.level().dimension();
            BlockPos deathPos = new BlockPos((int) sp.getX(), (int) sp.getY(), (int) sp.getZ());
            UUID uuid = sp.getUUID();
            Map<ResourceKey<Level>, Map<String, PinHelper.PointPin>> playerCache = PINS_CACHE.computeIfAbsent(uuid, k -> new HashMap<>());

            playerCache.computeIfAbsent(worldKey, k -> new HashMap<>()).put(LASTDEATH, PinHelper.getPointPin(sp, pinsData, worldKey, deathPos, LASTDEATH));
        });
    }

}
