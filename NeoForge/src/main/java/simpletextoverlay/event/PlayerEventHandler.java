package simpletextoverlay.event;

import net.minecraft.world.entity.player.Player;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import simpletextoverlay.events.SimpleTextOverlayEvents;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;

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

}
