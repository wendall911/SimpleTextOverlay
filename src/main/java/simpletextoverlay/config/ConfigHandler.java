package simpletextoverlay.config;

import net.minecraftforge.common.config.Config;

import simpletextoverlay.reference.Names;
import simpletextoverlay.reference.Reference;
import simpletextoverlay.util.Alignment;

@Config(modid=Reference.MODID)
public class ConfigHandler {

    public static Client client;
    public static class Client {

        public static Align alignment;
        public static class Align {
            
            @Config.RangeInt(min = -100, max = 100)
            @Config.Comment({"Offsets for the top left side of the screen."})
            public static int topleftX = Alignment.TOPLEFT.defaultX;

            @Config.RangeInt(min = -100, max = 100)
            public static int topleftY = Alignment.TOPLEFT.defaultY;

            @Config.RangeInt(min = -100, max = 100)
            @Config.Comment({"Offsets for the top center side of the screen."})
            public static int topcenterX = Alignment.TOPCENTER.defaultX;

            @Config.RangeInt(min = -100, max = 100)
            public static int topcenterY = Alignment.TOPCENTER.defaultY;

            @Config.RangeInt(min = -100, max = 100)
            @Config.Comment({"Offsets for the top right side of the screen."})
            public static int toprightX = Alignment.TOPRIGHT.defaultX;

            @Config.RangeInt(min = -100, max = 100)
            public static int toprightY = Alignment.TOPRIGHT.defaultY;

            @Config.RangeInt(min = -100, max = 100)
            @Config.Comment({"Offsets for the middle left side of the screen."})
            public static int middleleftX = Alignment.MIDDLELEFT.defaultX;

            @Config.RangeInt(min = -100, max = 100)
            public static int middleleftY = Alignment.MIDDLELEFT.defaultY;

            @Config.RangeInt(min = -100, max = 100)
            @Config.Comment({"Offsets for the middle center side of the screen."})
            public static int middlecenterX = Alignment.MIDDLECENTER.defaultX;

            @Config.RangeInt(min = -100, max = 100)
            public static int middlecenterY = Alignment.MIDDLECENTER.defaultY;

            @Config.RangeInt(min = -100, max = 100)
            @Config.Comment({"Offsets for the middle right side of the screen."})
            public static int middlerightX = Alignment.MIDDLERIGHT.defaultX;

            @Config.RangeInt(min = -100, max = 100)
            public static int middlerightY = Alignment.MIDDLERIGHT.defaultY;

            @Config.RangeInt(min = -100, max = 100)
            @Config.Comment({"Offsets for the bottom left side of the screen."})
            public static int bottomleftX = Alignment.BOTTOMLEFT.defaultX;

            @Config.RangeInt(min = -100, max = 100)
            public static int bottomleftY = Alignment.BOTTOMLEFT.defaultY;

            @Config.RangeInt(min = -100, max = 100)
            @Config.Comment({"Offsets for the bottom center side of the screen."})
            public static int bottomcenterX = Alignment.BOTTOMCENTER.defaultX;

            @Config.RangeInt(min = -100, max = 100)
            public static int bottomcenterY = Alignment.BOTTOMCENTER.defaultY;

            @Config.RangeInt(min = -100, max = 100)
            @Config.Comment({"Offsets for the bottom right side of the screen."})
            public static int bottomrightX = Alignment.BOTTOMRIGHT.defaultX;

            @Config.RangeInt(min = -100, max = 100)
            public static int bottomrightY = Alignment.BOTTOMRIGHT.defaultY;

        }

        public static General general;
        public static class General {

            @Config.Comment({"The configuration that should be loaded on startup."})
            public static String overlayConfig = Names.Files.FILE_JSON;

            @Config.Comment({"Replace the debug overlay (F3) with the SimpleTextOverlay overlay."})
            public static boolean replaceDebug = false;

            @Config.Comment({"Display the overlay in chat."})
            public static boolean showInChat = true;

            @Config.Comment({"Display the overlay on the player list."})
            public static boolean showOnPlayerList = true;

            @Config.Comment({"The overlay will be scaled by this amount."})
            @Config.RangeDouble(min = 0.5, max = 2.0) 
            public static double scale = (double) 1.0;

            @Config.Comment({"The interval between file reads for the 'file' tag (in seconds)."})
            @Config.RangeInt(min = 5)
            public static int fileInterval = 5;

            @Config.Comment({"Display the vanilla potion effects overlay."})
            public static boolean showOverlayPotions = true;

            @Config.Comment({"Display the item overlay on icon (durability, stack size)."})
            public static boolean showOverlayItemIcons = true;

        }

    }

    public static Server server;
    public static class Server {

        @Config.Comment({"Force F3 debug screen to be replaced."})
        public static boolean forceDebug = false;

        @Config.Comment({"List of tags disallowed."})
        public static String[] blacklistTags = new String[] {
        };

    }

}
