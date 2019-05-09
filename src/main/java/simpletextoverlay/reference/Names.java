package simpletextoverlay.reference;

@SuppressWarnings("HardCodedStringLiteral")
public final class Names {

    public static final class Command {
        public static final class Message {
            public static final String USAGE = "commands.simpletextoverlay.usage";
            public static final String RELOAD = "commands.simpletextoverlay.reload";
            public static final String LOAD = "commands.simpletextoverlay.load";
            public static final String SAVE = "commands.simpletextoverlay.save";
            public static final String SUCCESS = "commands.simpletextoverlay.success";
            public static final String FAILURE = "commands.simpletextoverlay.failure";
            public static final String ENABLE = "commands.simpletextoverlay.enable";
            public static final String DISABLE = "commands.simpletextoverlay.disable";
            public static final String UNKNOWN = "commands.simpletextoverlay.unknown";
            public static final String MISSING = "commands.simpletextoverlay.missing";
        }

        public static final String NAME = "simpletextoverlay";
        public static final String SHORT_NAME = "sto";
        public static final String RELOAD = "reload";
        public static final String LOAD = "load";
        public static final String SAVE = "save";
        public static final String ENABLE = "enable";
        public static final String DISABLE = "disable";
        public static final String TAGLIST = "taglist";
        public static final String CYCLE = "cycle";
    }

    public static final class Files {
        public static final String FILE_JSON = "simpletextoverlay.json";
        public static final String FILE_DEBUG = "debugoverlay.json";
        public static final String EXT_JSON = ".json";
        public static final String[] BUILTINS = new String [] {
            "simpletextoverlay.json",
            "tsoverlay.json"
        };
        public static final String[] DEBUG_BUILTINS = new String [] {
            "debugoverlay.json",
            "debugall.json"
        };
    }

    public static final class Keys {
        public static final String CATEGORY = "simpletextoverlay.key.category";
        public static final String TOGGLE = "simpletextoverlay.key.toggle";
        public static final String CYCLE = "simpletextoverlay.key.cycle";
    }

}
