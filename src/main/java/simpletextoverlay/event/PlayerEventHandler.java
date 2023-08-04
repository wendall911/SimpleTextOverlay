package simpletextoverlay.event;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.Level;

import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.PinHelper;
import simpletextoverlay.util.PinHelper.PointPin;

@Mod.EventBusSubscriber(modid=SimpleTextOverlay.MODID)
public class PlayerEventHandler {

    public static final String BEDSPAWN = "bedspawn";
    public static final String LASTDEATH = "lastdeath";
    public static final String WORLDSPAWN = "worldspawn";

    public static final Map<ResourceKey<Level>, Map<String, PointPin>> PINS_CACHE = new HashMap<>();

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;

        if (player != null && !player.level.isClientSide) {
            ServerPlayer sp = (ServerPlayer) player;

            sp.getCapability(DataManager.INSTANCE).ifPresent((pinsData) -> {
                ResourceKey<Level> worldKey = sp.getRespawnDimension();
                BlockPos spawnPos = sp.getLevel().getSharedSpawnPos();
                Map<String, PointPin> cachedPins = PINS_CACHE.computeIfAbsent(worldKey, k -> new HashMap<>());
                PointPin spawnPin = cachedPins.get(WORLDSPAWN);
                PointPin bedPin = cachedPins.get(BEDSPAWN);
                PointPin lastDeathPin = cachedPins.get(LASTDEATH);

                if (spawnPin != null && spawnPin.pin != null) {
                    PinHelper.setPointPin(pinsData, spawnPin);
                }
                else if (worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                    spawnPin = PinHelper.getPointPin(pinsData, worldKey, spawnPos, WORLDSPAWN);
                    cachedPins.put(WORLDSPAWN, spawnPin);

                    PinHelper.setPointPin(pinsData, spawnPin);
                }

                if (bedPin != null && bedPin.pin != null) {
                    PinHelper.setPointPin(pinsData, bedPin);
                }
                else if (sp.getRespawnPosition() != null) {
                    bedPin = PinHelper.getPointPin(pinsData, worldKey, sp.getRespawnPosition(), BEDSPAWN);
                    cachedPins.put(BEDSPAWN, bedPin);

                    PinHelper.setPointPin(pinsData, bedPin);
                }

                if (lastDeathPin != null && lastDeathPin.pin != null) {
                    PinHelper.setPointPin(pinsData, lastDeathPin);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getEntity().level.isClientSide) {
            final Player player = event.getEntity();
            final ResourceKey<Level> worldKey = event.getTo();

            if (!worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                player.getCapability(DataManager.INSTANCE).ifPresent((pinsData) -> {
                    BlockPos spawnPos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ());
                    PointPin portalPin = PinHelper.getPointPin(pinsData, worldKey, spawnPos, WORLDSPAWN);
                    Map<String, PointPin> cachedPins = PINS_CACHE.get(worldKey);

                    cachedPins.put(WORLDSPAWN, portalPin);
                    PinHelper.setPointPin(pinsData, portalPin);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof ServerPlayer sp)) {
            return;
        }

        if (sp.level.isClientSide) {
            return;
        }

        sp.getCapability(DataManager.INSTANCE).ifPresent((pinsData) -> {
            ResourceKey<Level> worldKey = sp.level.dimension();
            BlockPos deathPos = new BlockPos((int) sp.getX(), (int) sp.getY(), (int) sp.getZ());
            Map<String, PointPin> cachedPins = PINS_CACHE.get(worldKey);

            cachedPins.put(LASTDEATH, PinHelper.getPointPin(pinsData, worldKey, deathPos, LASTDEATH));
        });
    }

}
