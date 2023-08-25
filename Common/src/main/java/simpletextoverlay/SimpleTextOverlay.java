package simpletextoverlay;

import com.illusivesoulworks.spectrelib.config.SpectreConfig;
import com.illusivesoulworks.spectrelib.config.SpectreConfigLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.compass.PinInfoRegistry;
import simpletextoverlay.platform.Services;

public class SimpleTextOverlay {

    public static final String MODID = "simpletextoverlay";
    public static final String MOD_NAME = "Simple Text Overlay";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        PinInfoRegistry.init();
    }

    public static void initConfig() {
        if (Services.PLATFORM.isPhysicalClient()) {
            SpectreConfig clientConfig = SpectreConfigLoader.add(SpectreConfig.Type.CLIENT, OverlayConfig.CLIENT_SPEC, MODID);

            clientConfig.addLoadListener(config -> OverlayConfig.init());
        }
    }

}
