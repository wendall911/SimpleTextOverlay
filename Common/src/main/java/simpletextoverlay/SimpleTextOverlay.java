package simpletextoverlay;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import technology.roughness.whitenoise.config.WhiteNoiseConfig;
import technology.roughness.whitenoise.config.WhiteNoiseConfigLoader;
import technology.roughness.whitenoise.platform.Services;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.overlay.compass.PinInfoRegistry;

public class SimpleTextOverlay {

    public static final String MODID = "simpletextoverlay";
    public static final String MOD_NAME = "Simple Text Overlay";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        PinInfoRegistry.init();
    }

    public static void initConfig() {
        if (Services.PLATFORM.isPhysicalClient()) {
            WhiteNoiseConfigLoader.add(WhiteNoiseConfig.Type.CLIENT, OverlayConfig.CLIENT_SPEC, MODID);
        }
    }

}
