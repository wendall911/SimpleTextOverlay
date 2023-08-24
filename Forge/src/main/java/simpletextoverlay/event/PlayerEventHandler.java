package simpletextoverlay.event;

import net.minecraft.world.entity.player.Player;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import simpletextoverlay.events.SimpleTextOverlayEvents;
import simpletextoverlay.SimpleTextOverlay;

@Mod.EventBusSubscriber(modid=SimpleTextOverlay.MODID)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;

        SimpleTextOverlayEvents.onEntityJoinLevel(player);
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        SimpleTextOverlayEvents.onPlayerChangeDimension(event.getEntity(), event.getTo());
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        SimpleTextOverlayEvents.onPlayerDeath(event.getEntity());
    }

}
