package simpletextoverlay.event;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Config.Type;

import simpletextoverlay.reference.Reference;
import simpletextoverlay.SimpleTextOverlay;

public class ConfigEventHandler {

    @SubscribeEvent
    public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID)) {
            SimpleTextOverlay.logger.info("Config updated.");
            ConfigManager.sync(Reference.MODID, Type.INSTANCE);
        }
    }

}
