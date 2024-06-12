package simpletextoverlay.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import simpletextoverlay.attachments.AttachmentDataManager;
import simpletextoverlay.events.SimpleTextOverlayEvents;
import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.wrappers.PlayerListWrapper;

public class PlayerEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;

        if (player != null) {
            AttachmentDataManager.DATA_MANAGER_INSTANCE.setPlayer(player);
        }

        SimpleTextOverlayEvents.onEntityJoinLevel(player);
    }

    @SubscribeEvent
    public void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        SimpleTextOverlayEvents.onPlayerChangeDimension(event.getEntity(), event.getTo());
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        SimpleTextOverlayEvents.onPlayerDeath(event.getEntity());
    }

    /*
     * Resetting cache on local server starting event. This makes sure we are not sharing pins between world instances.
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        DataManager.resetCache();
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        MinecraftServer server = event.getEntity().getServer();

        if (server != null) {
            PlayerListWrapper playerList = new PlayerListWrapper(server.getPlayerList());

            playerList.savePlayerData((ServerPlayer) event.getEntity());
        }
    }

}
