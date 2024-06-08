package simpletextoverlay.event;

import net.minecraft.world.entity.EntityType;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import simpletextoverlay.capability.CapabilityDataManager;

public class CapabilityEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerCapability(final RegisterCapabilitiesEvent event) {
        event.registerEntity(
            CapabilityDataManager.DATA_MANAGER_CAPABILITY,
            EntityType.PLAYER,
            (player, ctx) -> new CapabilityDataManager.DataManagerProvider(player)
        );
    }

}
