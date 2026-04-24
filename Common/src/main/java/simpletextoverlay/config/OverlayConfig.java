package simpletextoverlay.config;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import technology.roughness.whitenoise.config.WhiteNoiseConfigSpec;

import simpletextoverlay.common.Translations;
import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.Alignment.AlignmentType;
import simpletextoverlay.util.ColorHelper;

public final class OverlayConfig {

    public static boolean loaded;
    public static final WhiteNoiseConfigSpec CLIENT_SPEC;
    private static final Client CLIENT;

    static {
        final Pair<Client, WhiteNoiseConfigSpec> specPairClient = new WhiteNoiseConfigSpec.Builder().configure(Client::new);

        CLIENT_SPEC = specPairClient.getRight();
        CLIENT = specPairClient.getLeft();
    }

    @SuppressWarnings("unchecked")
    public static void init() {
        List<String> fields = (List<String>) CLIENT.fields.get();
        Client.sortedFields = new ArrayList<>(fields);

        if (CLIENT.position.get().name().startsWith("BOTTOM")) {
            Collections.reverse(Client.sortedFields);
        }

        // Populate range colors here to initialize when building fields list.
        String[] lightColors = CLIENT.lightColorRange.get().split("->");
        String[] timeColors = CLIENT.timeColorRange.get().split("->");

        Client.lightColorDark = ColorHelper.decode(lightColors[0]);
        Client.lightColorBright = ColorHelper.decode(lightColors[1]);
        Client.timeColorDark = ColorHelper.decode(timeColors[0]);
        Client.timeColorBright = ColorHelper.decode(timeColors[1]);
        Client.labelColorDecoded = ColorHelper.decode(CLIENT.labelColor.get());
        Client.footColorDecoded = ColorHelper.decode(CLIENT.footColor.get());
        Client.biomeColorDecoded = ColorHelper.decode(CLIENT.biomeColor.get());
        Client.daysColorDecoded = ColorHelper.decode(CLIENT.daysColor.get());

        OverlayManager.INSTANCE.init();

        loaded = true;
    }

    public static class Client {
        private static final List<String> fieldList = List.of("fields");
        public static final String[] fieldStrings = new String[]{"light", "time", "days", "foot", "biome", "season"};
        private static final Predicate<Object> fieldStringValidator = s -> s instanceof String &&
            Arrays.asList(fieldStrings).contains(s);
        private static final String[] defaultFields = new String[]{"light", "time", "foot", "biome", "season"};
        private static List<String> sortedFields;
        private static Color lightColorDark = ColorHelper.decode("#b02e26");
        private static Color lightColorBright = ColorHelper.decode("#ffd83d");
        private static Color timeColorDark = ColorHelper.decode("#474f52");
        private static Color timeColorBright = ColorHelper.decode("#ffd83d");
        private static Color labelColorDecoded;
        private static Color footColorDecoded;
        private static Color biomeColorDecoded;
        private static Color daysColorDecoded;
        private static final Predicate<Object> hexValidator = s -> s instanceof String
            && ((String) s).matches("#[a-fA-F\\d]{6}");
        private static final Predicate<Object> hexRangeValidator = s -> s instanceof String
            && ((String) s).matches("#[a-fA-F\\d]{6}->#[a-fA-F\\d]{6}");

        public final WhiteNoiseConfigSpec.BooleanValue textShadow;
        public final WhiteNoiseConfigSpec.EnumValue<Alignment.AlignmentType> position;
        public final WhiteNoiseConfigSpec.IntValue offsetX;
        public final WhiteNoiseConfigSpec.IntValue offsetY;
        public final WhiteNoiseConfigSpec.DoubleValue scale;
        public final WhiteNoiseConfigSpec.ConfigValue<List<? extends String>> fields;
        public final WhiteNoiseConfigSpec.ConfigValue<String> labelColor;
        public final WhiteNoiseConfigSpec.ConfigValue<String> lightLabel;
        public final WhiteNoiseConfigSpec.ConfigValue<String> lightColorRange;
        public final WhiteNoiseConfigSpec.ConfigValue<String> timeLabel;
        public final WhiteNoiseConfigSpec.ConfigValue<String> timeColorRange;
        public final WhiteNoiseConfigSpec.BooleanValue timeUse12;
        public final WhiteNoiseConfigSpec.ConfigValue<String> footLabel;
        public final WhiteNoiseConfigSpec.ConfigValue<String> footColor;
        public final WhiteNoiseConfigSpec.ConfigValue<String> biomeLabel;
        public final WhiteNoiseConfigSpec.ConfigValue<String> biomeColor;
        public final WhiteNoiseConfigSpec.ConfigValue<String> daysLabel;
        public final WhiteNoiseConfigSpec.ConfigValue<String> daysColor;
        public final WhiteNoiseConfigSpec.BooleanValue showCompass;
        public final WhiteNoiseConfigSpec.IntValue compassOpacity;

        public Client(WhiteNoiseConfigSpec.Builder builder) {
            position = builder
                .comment(getTranslation("position"))
                .defineEnum("position", AlignmentType.BOTTOMRIGHT);
            offsetX = builder
                .comment(getTranslation("offsetx"))
                .defineInRange("offsetX", 3, -100, 100);
            offsetY = builder
                .comment(getTranslation("offsety"))
                .defineInRange("offsetY", 3, -100, 100);
            scale = builder
                .comment(getTranslation("scale"))
                .defineInRange("scale", 1.0, 0.5, 2.0);
            fields = builder
                .comment(getTranslation("fields"))
                .defineListAllowEmpty(fieldList, getFields(), fieldStringValidator);
            textShadow = builder
                .comment(getTranslation("textshadow"))
                .define("textShadow", true);
            labelColor = builder
                .comment(getTranslation("labelcolor"))
                .define("labelColor", "#9C9D97", hexValidator);
            lightLabel = builder
                .comment(getTranslation("lightlabel"))
                .define("lightLabel", "Light: ");
            lightColorRange = builder
                .comment(getTranslation("lightcolorrange"))
                .define("lightColorRange", "#D53A30->#FFD83D", hexRangeValidator);
            timeLabel = builder
                .comment(getTranslation("timelabel"))
                .define("timeLabel", "");
            timeColorRange = builder
                .comment(getTranslation("timecolorrange"))
                .define("timeColorRange", "#D53A30->#FFD83D", hexRangeValidator);
            timeUse12 = builder
                .comment(getTranslation("timeuse12"))
                .define("timeUse12", true);
            footLabel = builder
                .comment(getTranslation("footlabel"))
                .define("footLabel", "Foot level: ");
            footColor = builder
                .comment(getTranslation("footcolor"))
                .define("footColor", "#D19F74", hexValidator);
            biomeLabel = builder
                .comment(getTranslation("biomelabel"))
                .define("biomeLabel", "Biome: ");
            biomeColor = builder
                .comment(getTranslation("biomecolor"))
                .define("biomeColor", "#ADD86E", hexValidator);
            showCompass = builder
                .comment(getTranslation("enabled"))
                .define("enabled", true);
            compassOpacity = builder
                .comment(getTranslation("compassopacity"))
                .defineInRange("compassOpacity", 10, 0, 100);
            daysLabel = builder
                .comment(getTranslation("dayslabel"))
                .define("daysLabel", "Day: ");
            daysColor = builder
                .comment(getTranslation("dayscolor"))
                .define("daysColor", "#3c44a9", hexValidator);
        }
    }

    public static AlignmentType position() {
        return CLIENT.position.get();
    }

    public static int offsetX() {
        return CLIENT.offsetX.get();
    }

    public static int offsetY() {
        return CLIENT.offsetY.get();
    }

    public static double scale() {
        return CLIENT.scale.get();
    }

    public static List<String> fields() {
        return Client.sortedFields;
    }

    public static boolean textShadow() {
        return CLIENT.textShadow.get();
    }

    public static Color labelColor() {
        return Client.labelColorDecoded;
    }

    public static String lightLabel() {
        return CLIENT.lightLabel.get();
    }

    public static Color lightColorDark() {
        return Client.lightColorDark;
    }

    public static Color lightColorBright() {
        return Client.lightColorBright;
    }

    public static String timeLabel() {
        return CLIENT.timeLabel.get();
    }

    public static Color timeColorDark() {
        return Client.timeColorDark;
    }

    public static Color timeColorBright() {
        return Client.timeColorBright;
    }

    public static boolean timeUse12() {
        return CLIENT.timeUse12.get();
    }

    public static String footLabel() {
        return CLIENT.footLabel.get();
    }

    public static Color footColor() {
        return Client.footColorDecoded;
    }

    public static String biomeLabel() {
        return CLIENT.biomeLabel.get();
    }

    public static Color biomeColor() {
        return Client.biomeColorDecoded;
    }

    public static boolean showCompass() {
        return CLIENT.showCompass.get();
    }

    private static Supplier<List<? extends String>> getFields() {
        return () -> Arrays.asList(Client.defaultFields);
    }

    public static int getCompassOpacity() {
        return CLIENT.compassOpacity.get();
    }

    public static String daysLabel() {
        return CLIENT.daysLabel.get();
    }

    public static Color daysColor() {
        return Client.daysColorDecoded;
    }
    
    private static String getTranslation(String key) {
        return Translations.get(key);
    }

}
