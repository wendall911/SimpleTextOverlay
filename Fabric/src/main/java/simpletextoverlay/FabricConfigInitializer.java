package simpletextoverlay;

import com.illusivesoulworks.spectrelib.config.SpectreLibInitializer;

public class FabricConfigInitializer implements SpectreLibInitializer {

    @Override
    public void onInitializeConfig() {
        SimpleTextOverlay.init();
    }

}
