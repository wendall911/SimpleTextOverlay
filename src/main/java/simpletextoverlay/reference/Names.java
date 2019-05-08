package simpletextoverlay.reference;

@SuppressWarnings("HardCodedStringLiteral")
public final class Names {
    public static final class Mods {
        public static final String BLOODMAGIC_MODID = "AWWayofTime";
        public static final String BLOODMAGIC_NAME = "Blood Magic";

        public static final String SIMPLYJETPACKS_MODID = "simplyjetpacks";
        public static final String SIMPLYJETPACKS_NAME = "Simply Jetpacks";

        public static final String TERRAFIRMACRAFT_MODID = "terrafirmacraft";
        public static final String TERRAFIRMACRAFT_NAME = "TerraFirmaCraft";

        public static final String THAUMCRAFT_MODID = "Thaumcraft";
        public static final String THAUMCRAFT_NAME = "Thaumcraft";
    }

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
    }

    public static final class Config {
        public static final class Category {
            public static final String GENERAL = "general";
            public static final String ALIGNMENT = "alignment";
        }

        public static final String FILENAME = "filename";
        public static final String FILENAME_DESC = "The configuration that should be loaded on startup.";
        public static final String REPLACE_DEBUG = "replaceDebug";
        public static final String REPLACE_DEBUG_DESC = "Replace the debug overlay (F3) with the SimpleTextOverlay overlay.";
        public static final String SHOW_IN_CHAT = "showInChat";
        public static final String SHOW_IN_CHAT_DESC = "Display the overlay in chat.";
        public static final String SHOW_ON_PLAYER_LIST = "showOnPlayerList";
        public static final String SHOW_ON_PLAYER_LIST_DESC = "Display the overlay on the player list.";
        public static final String SCALE = "scale";
        public static final String SCALE_DESC = "The overlay will be scaled by this amount.";
        public static final String FILE_INTERVAL = "fileInterval";
        public static final String FILE_INTERVAL_DESC = "The interval between file reads for the 'file' tag (in seconds).";

        public static final String SHOW_OVERLAY_POTIONS = "showOverlayPotions";
        public static final String SHOW_OVERLAY_POTIONS_DESC = "Display the vanilla potion overlay.";

        public static final String SHOW_OVERLAY_ITEM_ICONS = "showOverlayItemIcons";
        public static final String SHOW_OVERLAY_ITEM_ICONS_DESC = "Display the item overlay on icon (durability, stack size).";

        public static final String LANG_PREFIX = Reference.MODID + ".config";
    }

    public static final class Files {
        public static final String FILE_JSON = "simpletextoverlay.json";
        public static final String FILE_DEBUG = "debugoverlay.json";
        public static final String EXT_JSON = ".json";
    }

    public static final class Keys {
        public static final String CATEGORY = "simpletextoverlay.key.category";
        public static final String TOGGLE = "simpletextoverlay.key.toggle";
    }
}
