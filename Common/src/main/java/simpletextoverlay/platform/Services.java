package simpletextoverlay.platform;

import technology.roughness.whitenoise.platform.ServicesBase;

import simpletextoverlay.platform.services.ICapabilityPlatform;
import simpletextoverlay.platform.services.IClientPlatform;
import simpletextoverlay.platform.services.IPlatform;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.SimpleTextOverlay;

public class Services extends ServicesBase {

    public static final IClientPlatform CLIENT_PLATFORM = load(SimpleTextOverlay.LOGGER, IClientPlatform.class);
    public static final IPlatform PLATFORM = load(SimpleTextOverlay.LOGGER,IPlatform.class);
    public static final ICapabilityPlatform CAPABILITY_PLATFORM = load(SimpleTextOverlay.LOGGER,ICapabilityPlatform.class);
    public static final ISeasonInfo SEASON_INFO = load(SimpleTextOverlay.LOGGER,ISeasonInfo.class);

}
