package simpletextoverlay.reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Names {

    public static final class Command {
        public static final class Message {
            public static final String USAGE = "/sto <reload|load|save|enable|disable|config|cycle> [filename]";
            public static final String RELOAD = "Reloading...";
            public static final String LOAD = "Loading %s...";
            public static final String SAVE = "Saving %s...";
            public static final String SUCCESS = "Done!";
            public static final String FAILURE = "Something went wrong!";
            public static final String ENABLE = "Simple Text Overlay is now enabled.";
            public static final String DISABLE = "Simple Text Overlay is now disabled.";
            public static final String UNKNOWN = "Unknown command!";
            public static final String MISSING = "Filename required!";
        }

        public static final String SHORT_NAME = "sto";
        public static final String RELOAD = "reload";
        public static final String LOAD = "load";
        public static final String SAVE = "save";
        public static final String ENABLE = "enable";
        public static final String DISABLE = "disable";
        public static final String CYCLE = "cycle";
    }

    public static final class Files {
        public static final String FILE_JSON = "simpletextoverlay.json";
        public static final String FILE_DEBUG = "debugoverlay.json";
        public static final String EXT_JSON = ".json";
        public static final List<String> DEFAULT_FILES = Arrays.asList(
            "simpletextoverlay.json",
            "basicoverlay.json",
            "compassoverlay.json"
        );
        public static final List<String> BUILTINS = new ArrayList<String>(DEFAULT_FILES);

        public static List<String> DEBUG_BUILTINS = new ArrayList<String>(
            Arrays.asList(
                "debugoverlay.json",
                "debugalloverlay.json"
            )
        );
    }

    public static final class Keys {
        public static final String CATEGORY = "simpletextoverlay.key.category";
        public static final String TOGGLE = "simpletextoverlay.key.toggle";
        public static final String CYCLE = "simpletextoverlay.key.cycle";
    }

}
