package simpletextoverlay.platform;

import simpletextoverlay.platform.services.ICapabilityPlatform;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.SimpleTextOverlay;

import technology.roughness.whitenoise.platform.ServicesBase;

public class Services extends ServicesBase {

    public static final ICapabilityPlatform CAPABILITY_PLATFORM = load(SimpleTextOverlay.LOGGER, ICapabilityPlatform.class);
    public static final ISeasonInfo SEASON_INFO = load(SimpleTextOverlay.LOGGER, ISeasonInfo.class);

}
