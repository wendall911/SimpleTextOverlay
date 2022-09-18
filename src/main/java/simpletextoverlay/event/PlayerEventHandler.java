package simpletextoverlay.event;

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
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.PinHelper;
import simpletextoverlay.util.PinHelper.PointPin;

@Mod.EventBusSubscriber(modid=SimpleTextOverlay.MODID)
public class PlayerEventHandler {

    private static final String BEDSPAWN = "bedspawn";
    public static final String LASTDEATH = "lastdeath";
    private static final String WORLDSPAWN = "worldspawn";
    
    private static PointPin lastDeath;

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;

        if (player != null && !player.level.isClientSide) {
            ServerPlayer sp = (ServerPlayer) player;

            player.getCapability(DataManager.INSTANCE).ifPresent((pinsData) -> {
                ResourceKey<Level> worldKey = sp.getRespawnDimension();
                BlockPos spawnPos = sp.getLevel().getSharedSpawnPos();

                if (worldKey.location().toString().contains(BuiltinDimensionTypes.OVERWORLD.location().toString())) {
                    PointPin spawnPin = PinHelper.getPointPin(pinsData, worldKey, spawnPos, WORLDSPAWN);

                    PinHelper.setPointPin(pinsData, spawnPin);
                }

                if (lastDeath != null) {
                    if (lastDeath.pin != null) {
                        PinHelper.setPointPin(pinsData, lastDeath);
                        lastDeath.pin = null;
                    }
                }

                PointPin bedPin = PinHelper.getPointPin(pinsData, worldKey, sp.getRespawnPosition(), BEDSPAWN);

                if (sp.getRespawnPosition() != null) {
                    PinHelper.setPointPin(pinsData, bedPin);
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
                    BlockPos spawnPos = new BlockPos(player.getX(), player.getY(), player.getZ());
                    PointPin portalPin = PinHelper.getPointPin(pinsData, worldKey, spawnPos, WORLDSPAWN);

                    PinHelper.setPointPin(pinsData, portalPin);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerSetSpawn(PlayerSetSpawnEvent event) {
        if (!event.getEntity().level.isClientSide) {
            final Player player = event.getEntity();

            player.getCapability(DataManager.INSTANCE).ifPresent((pinsData) -> {
                ResourceKey<Level> worldKey = player.level.dimension();
                BlockPos respawnPos = event.getNewSpawn();
                PointPin bedPin = PinHelper.getPointPin(pinsData, worldKey, respawnPos, BEDSPAWN);

                if (respawnPos != null) {
                    PinHelper.setPointPin(pinsData, bedPin);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof ServerPlayer)) {
            return;
        }

        ServerPlayer sp = (ServerPlayer) entity;

        if (sp.level.isClientSide) {
            return;
        }

        sp.getCapability(DataManager.INSTANCE).ifPresent((pinsData) -> {
            ResourceKey<Level> worldKey = sp.level.dimension();
            BlockPos deathPos = new BlockPos(sp.getX(), sp.getY(), sp.getZ());

            lastDeath = PinHelper.getPointPin(pinsData, worldKey, deathPos, LASTDEATH);
        });
    }

}
