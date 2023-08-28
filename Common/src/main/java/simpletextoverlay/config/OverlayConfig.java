package simpletextoverlay.config;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.List;

import com.illusivesoulworks.spectrelib.config.SpectreConfigSpec;

import org.apache.commons.compress.archivers.sevenz.CLI;
import org.apache.commons.lang3.tuple.Pair;

import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.util.ColorHelper;

public final class OverlayConfig {

    public static boolean loaded;
    public static final SpectreConfigSpec CLIENT_SPEC;
    private static final Client CLIENT;

    static {
        final Pair<Client, SpectreConfigSpec> specPairClient = new SpectreConfigSpec.Builder().configure(Client::new);

        CLIENT_SPEC = specPairClient.getRight();
        CLIENT = specPairClient.getLeft();
    }

    @SuppressWarnings("unchecked")
    public static void init() {
        List<String> fields = (List<String>) CLIENT.fields.get();

        if (CLIENT.position.get().startsWith("BOTTOM")) {
            Collections.reverse(fields);
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

        Client.sortedFields = fields;

        OverlayManager.INSTANCE.init();

        loaded = true;
    }

    private static class Client {
        private static final List<String> positions = Arrays.asList("TOPLEFT", "TOPRIGHT", "BOTTOMLEFT", "BOTTOMRIGHT");
        private static final List<String> fieldList = Arrays.asList("fields");
        private static final String[] fieldStrings = new String[]{"light", "time", "days", "foot", "biome", "season"};
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
            && ((String) s).matches("#[a-zA-Z\\d]{6}");
        private static final Predicate<Object> hexRangeValidator = s -> s instanceof String
            && ((String) s).matches("#[a-zA-Z\\d]{6}->#[a-zA-Z\\d]{6}");
        private static final List<String> seasonDimensionList = Arrays.asList("seasonDimensions");
        private static final String[] seasonDimensionFields = new String[]{"minecraft:overworld"};

        public final SpectreConfigSpec.BooleanValue enabled;
        public final SpectreConfigSpec.BooleanValue textShadow;
        public final SpectreConfigSpec.ConfigValue<String> position;
        public final SpectreConfigSpec.IntValue offsetX;
        public final SpectreConfigSpec.IntValue offsetY;
        public final SpectreConfigSpec.DoubleValue scale;
        public final SpectreConfigSpec.ConfigValue<List<? extends String>> fields;
        public final SpectreConfigSpec.ConfigValue<String> labelColor;
        public final SpectreConfigSpec.ConfigValue<String> lightLabel;
        public final SpectreConfigSpec.ConfigValue<String> lightColorRange;
        public final SpectreConfigSpec.ConfigValue<String> timeLabel;
        public final SpectreConfigSpec.ConfigValue<String> timeColorRange;
        public final SpectreConfigSpec.BooleanValue timeUse12;
        public final SpectreConfigSpec.ConfigValue<String> footLabel;
        public final SpectreConfigSpec.ConfigValue<String> footColor;
        public final SpectreConfigSpec.ConfigValue<String> biomeLabel;
        public final SpectreConfigSpec.ConfigValue<String> biomeColor;
        public final SpectreConfigSpec.ConfigValue<String> daysLabel;
        public final SpectreConfigSpec.ConfigValue<String> daysColor;
        public final SpectreConfigSpec.BooleanValue showCompass;
        public final SpectreConfigSpec.IntValue compassOpacity;
        public final SpectreConfigSpec.ConfigValue<List<? extends String>> seasonDimensions;

        public Client(SpectreConfigSpec.Builder builder) {
            enabled = builder
                .comment("Show overlay")
                .define("enabled", true);
            position = builder
                .comment("Position, one of: " + positions)
                .defineInList("position", "BOTTOMRIGHT", positions);
            offsetX = builder
                .comment("X offset")
                .defineInRange("offsetX", 3, -100, 100);
            offsetY = builder
                .comment("Y offset")
                .defineInRange("offsetY", 3, -100, 100);
            scale = builder
                .comment("The size of the biome info (multiplier)")
                .defineInRange("scale", 1.0, 0.5, 2.0);
            fields = builder
                .comment("Fields to show. Will display in same order as defined. Options: "
                    + "[\"" + String.join("\", \"", fieldStrings) + "\"]")
                .defineListAllowEmpty(fieldList, getFields(), s -> (s instanceof String));
            textShadow = builder
                .comment("Show text shadow.")
                .define("textShadow", true);
            labelColor = builder
                .comment("Label color (Format: #9c9d97)")
                .define("labelColor", "#9c9d97", hexValidator);
            lightLabel = builder
                .comment("Label for light level.")
                .define("lightLabel", "Light: ");
            lightColorRange = builder
                .comment("Light color range (Format (dark->bright): #b02e26->#ffd83d)")
                .define("lightColorRange", "#b02e26->#ffd83d", hexRangeValidator);
            timeLabel = builder
                .comment("Label for time.")
                .define("timeLabel", "");
            timeColorRange = builder
                .comment("Time color range (Format (dark->bright): #474f52->#ffd83d)")
                .define("timeColorRange", "#474f52->#ffd83d", hexRangeValidator);
            timeUse12 = builder
                .comment("Use 12 hour AM/PM display.")
                .define("timeUse12", true);
            footLabel = builder
                .comment("Label for foot level.")
                .define("footLabel", "Foot level: ");
            footColor = builder
                .comment("Foot level color (Format: #5d7c15)")
                .define("footColor", "#5d7c15", hexValidator);
            biomeLabel = builder
                .comment("Label for biome.")
                .define("biomeLabel", "Biome: ");
            biomeColor = builder
                .comment("Biome color (Format: #474f52)")
                .define("biomeColor", "#474f52", hexValidator);
            showCompass = builder
                .comment("Show HUD compass.")
                .define("enabled", true);
            compassOpacity = builder
                .comment("Compass background opacity.")
                .defineInRange("compassOpacity", 10, 0, 100);
            daysLabel = builder
                .comment("Label for total days.")
                .define("daysLabel", "Day: ");
            daysColor = builder
                .comment("Days color (Format: #3c44a9)")
                .define("daysColor", "#3c44a9", hexValidator);
            seasonDimensions = builder
                .comment("Fabric Seasons Only: Dimensions to show season. No API available for this lookup, so needs to be configured here.")
                .defineListAllowEmpty(seasonDimensionList, getSeasonDimensions(), s -> (s instanceof String));
        }
    }

    public static boolean enabled() {
        return CLIENT.enabled.get();
    }

    public static String position() {
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

    public static boolean hasSeasonDimension(String dimension) {
        return CLIENT.seasonDimensions.get().contains(dimension);
    }

    private static Supplier<List<? extends String>> getSeasonDimensions() {
        return () -> Arrays.asList(Client.seasonDimensionFields);
    }

}
