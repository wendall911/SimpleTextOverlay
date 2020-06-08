package simpletextoverlay.config;

public enum OverlayOption implements ISyncedOption {

    BLACKLIST_TAGS("blacklistTags"),
    FORCE_DEBUG("forceDebug");

    private final String name;

    OverlayOption(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

}

