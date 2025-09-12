package simpletextoverlay.platform;

import simpletextoverlay.platform.services.ICapabilityPlatform;
import simpletextoverlay.platform.services.IClientPlatform;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.SimpleTextOverlay;

public class Services extends technology.roughness.whitenoise.platform.Services {

    public static final IClientPlatform CLIENT_PLATFORM = load(SimpleTextOverlay.LOGGER, IClientPlatform.class);
    public static final ICapabilityPlatform CAPABILITY_PLATFORM = load(SimpleTextOverlay.LOGGER, ICapabilityPlatform.class);
    public static final ISeasonInfo SEASON_INFO = load(SimpleTextOverlay.LOGGER, ISeasonInfo.class);

}
