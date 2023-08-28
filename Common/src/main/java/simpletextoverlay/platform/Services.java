package simpletextoverlay.platform;

import java.util.ServiceLoader;

import simpletextoverlay.platform.services.ICapabilityPlatform;
import simpletextoverlay.platform.services.IClientPlatform;
import simpletextoverlay.platform.services.IPlatform;
import simpletextoverlay.platform.services.ISeasonInfo;
import simpletextoverlay.SimpleTextOverlay;

public class Services {

    public static final IClientPlatform CLIENT_PLATFORM = load(IClientPlatform.class);
    public static final IPlatform PLATFORM = load(IPlatform.class);
    public static final ICapabilityPlatform CAPABILITY_PLATFORM = load(ICapabilityPlatform.class);
    public static final ISeasonInfo SEASON_INFO = load(ISeasonInfo.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow(
                () -> new NullPointerException("Failed to load service for " + clazz.getName()));
        SimpleTextOverlay.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);

        return loadedService;
    }

}
