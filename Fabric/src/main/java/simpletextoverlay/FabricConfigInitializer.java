package simpletextoverlay;

import technology.roughness.whitenoise.config.WhiteNoiseConfigInitializer;

public class FabricConfigInitializer implements WhiteNoiseConfigInitializer {

    @Override
    public void onInitialize() {
        SimpleTextOverlay.initConfig();
    }

}
