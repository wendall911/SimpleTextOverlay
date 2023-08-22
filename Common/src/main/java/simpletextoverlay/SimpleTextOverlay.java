package simpletextoverlay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simpletextoverlay.registry.PinInfoRegistry;

public class SimpleTextOverlay {

    public static final String MODID = "simpletextoverlay";
    public static final String MOD_NAME = "Simple Text Overlay";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        PinInfoRegistry.init();
    }

    public static void initConfig() {

    }

}
