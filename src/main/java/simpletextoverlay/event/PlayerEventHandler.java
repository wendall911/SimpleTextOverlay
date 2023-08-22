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
import net.minecraftforge.network.PacketDistributor;

import simpletextoverlay.capabilities.CapabilityRegistry;
import simpletextoverlay.network.NetworkManager;
import simpletextoverlay.network.SyncData;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.overlay.compass.PinInfo;
import simpletextoverlay.util.PinHelper;
import simpletextoverlay.util.PinHelper.PointPin;

@Mod.EventBusSubscriber(modid=SimpleTextOverlay.MODID)
public class PlayerEventHandler {

    public static final String BEDSPAWN = "bedspawn";
    public static final String LASTDEATH = "lastdeath";
    public static final String WORLDSPAWN = "worldspawn";

    public static Map<ResourceKey<Level>, Map<String, PointPin>> PINS_CACHE = new HashMap<>();

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;

        if (player != null && !player.level.isClientSide) {
            ServerPlayer sp = (ServerPlayer) player;

            sp.getCapability(CapabilityRegistry.DATA_MANAGER_CAPABILITY).ifPresent((pinsData) -> {
                ResourceKey<Level> worldKey = sp.getRespawnDimension();
                BlockPos spawnPos = sp.getLevel().getSharedSpawnPos();
                Map<String, PointPin> cachedPins = PINS_CACHE.computeIfAbsent(worldKey, k -> new HashMap<>());
                final Map<String, PinInfo<?>> currentPins = pinsData.get(worldKey).getPins();

                PointPin bedPin = cachedPins.get(BEDSPAWN);

                if (currentPins.get(WORLDSPAWN) == null && worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                    PointPin spawnPin = PinHelper.getPointPin(pinsData, worldKey, spawnPos, WORLDSPAWN);

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
                    PointPin lastDeathPin = value.get(LASTDEATH);

                    if (lastDeathPin != null && lastDeathPin.pin != null) {
                        PinHelper.setPointPin(pinsData, lastDeathPin);
                    }
                });

                // Reset Cache
                PINS_CACHE = new HashMap<>();

                NetworkManager.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> sp),
                    new SyncData()
                );
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getEntity().level.isClientSide) {
            final ServerPlayer sp = (ServerPlayer) event.getEntity();
            final ResourceKey<Level> worldKey = event.getTo();

            if (!worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                sp.getCapability(CapabilityRegistry.DATA_MANAGER_CAPABILITY).ifPresent((pinsData) -> {
                    BlockPos spawnPos = new BlockPos(sp.getX(), sp.getY(), sp.getZ());
                    PointPin portalPin = PinHelper.getPointPin(pinsData, worldKey, spawnPos, WORLDSPAWN);

                    PinHelper.setPointPin(pinsData, portalPin);

                    NetworkManager.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> sp),
                        new SyncData()
                    );
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

        sp.getCapability(CapabilityRegistry.DATA_MANAGER_CAPABILITY).ifPresent((pinsData) -> {
            ResourceKey<Level> worldKey = sp.level.dimension();
            BlockPos deathPos = new BlockPos(sp.getX(), sp.getY(), sp.getZ());

            PINS_CACHE.computeIfAbsent(worldKey, k -> new HashMap<>()).put(LASTDEATH, PinHelper.getPointPin(pinsData, worldKey, deathPos, LASTDEATH));
        });
    }

}
