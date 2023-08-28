package simpletextoverlay.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import simpletextoverlay.capability.CapabilityDataManager;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.overlay.compass.DataManager;

@Mod.EventBusSubscriber(modid = SimpleTextOverlay.MODID)
public class CapabilityEventHandler {

    private static final ResourceLocation PROVIDER = new ResourceLocation(SimpleTextOverlay.MODID, "sto_provider");

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player && !(event.getObject() instanceof FakePlayer)) {
            event.addCapability(
                    PROVIDER,
                    new CapabilityDataManager.Provider(player)
            );
        }
    }

    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.register(DataManager.class);
    }

}
