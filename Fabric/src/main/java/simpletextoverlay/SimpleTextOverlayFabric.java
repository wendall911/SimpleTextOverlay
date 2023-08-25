package simpletextoverlay;

import net.fabricmc.api.ModInitializer;

public class SimpleTextOverlayFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        SimpleTextOverlay.init();
    }

}
