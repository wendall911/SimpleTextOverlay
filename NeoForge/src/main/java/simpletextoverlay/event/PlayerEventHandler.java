package simpletextoverlay.event;

import net.minecraft.server.level.ServerPlayer;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import simpletextoverlay.events.SimpleTextOverlayEvents;

public class PlayerEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        ServerPlayer sp = event.getEntity() instanceof ServerPlayer ? (ServerPlayer) event.getEntity() : null;

        SimpleTextOverlayEvents.onEntityJoinLevel(sp);
    }

    @SubscribeEvent
    public void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayer sp = event.getEntity() instanceof ServerPlayer ? (ServerPlayer) event.getEntity() : null;

        SimpleTextOverlayEvents.onPlayerChangeDimension(sp);
    }

}
