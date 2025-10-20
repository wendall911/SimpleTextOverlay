package simpletextoverlay.platform;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.services.ICapabilityPlatform;
import simpletextoverlay.platform.services.ISeasonInfo;

public class Services extends technology.roughness.whitenoise.platform.Services {

    public static final ICapabilityPlatform CAPABILITY_PLATFORM = load(SimpleTextOverlay.LOGGER, ICapabilityPlatform.class);
    public static final ISeasonInfo SEASON_INFO = load(SimpleTextOverlay.LOGGER, ISeasonInfo.class);

}
