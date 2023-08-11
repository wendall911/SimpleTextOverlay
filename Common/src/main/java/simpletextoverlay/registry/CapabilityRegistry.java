package simpletextoverlay.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.overlay.compass.DataManager;

@Mod.EventBusSubscriber(modid = SimpleTextOverlay.MODID)
public class CapabilityRegistry {

    public static final Capability<DataManager> DATA_MANAGER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.register(DataManager.class);
    }

}
