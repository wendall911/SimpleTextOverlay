package simpletextoverlay;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import simpletextoverlay.config.OverlayConfig;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlay {

    public static final String MODID = "simpletextoverlay";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public SimpleTextOverlay() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, OverlayConfig.CONFIG_SPEC);
	}

}
