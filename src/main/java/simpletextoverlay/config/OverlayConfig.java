package simpletextoverlay.config;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.ColorHelper;

public class OverlayConfig {

    public static final ForgeConfigSpec CONFIG_SPEC;
    private static final OverlayConfig CONFIG;

    private static final List<String> positions = Arrays.asList("TOPLEFT", "TOPRIGHT", "BOTTOMLEFT", "BOTTOMRIGHT");
    private static final List<String> fieldList = Arrays.asList("fields");
    private static Color lightColorDark = ColorHelper.decode("#b02e26");
    private static Color lightColorBright = ColorHelper.decode("#ffd83d");
    private static Color timeColorDark = ColorHelper.decode("#474f52");
    private static Color timeColorBright = ColorHelper.decode("#ffd83d");
    private static Color labelColorDecoded;
    private static Color footColorDecoded;
    private static Color biomeColorDecoded;
    private static Predicate<Object> hexValidator = s -> s instanceof String
            && ((String) s).matches("#{1}[[a-z][A-Z][0-9]]{6}");
    private static Predicate<Object> hexRangeValidator = s -> s instanceof String
            && ((String) s).matches("#{1}[[a-z][A-Z][0-9]]{6}-{1}>{1}#{1}[[a-z][A-Z][0-9]]{6}");

    public final BooleanValue enabled;
    public final BooleanValue textShadow;
    public final ConfigValue<String> position;
    public final IntValue offsetX;
    public final IntValue offsetY;
    public final DoubleValue scale;
    public final ConfigValue<List<? extends String>> fields;
    public final ConfigValue<String> labelColor;
    public final ConfigValue<String> lightLabel;
    public final ConfigValue<String> lightColorRange;
    public final ConfigValue<String> timeLabel;
    public final ConfigValue<String> timeColorRange;
    public final BooleanValue timeUse12;
    public final ConfigValue<String> footLabel;
    public final ConfigValue<String> footColor;
    public final ConfigValue<String> biomeLabel;
    public final ConfigValue<String> biomeColor;

    static {
        Pair<OverlayConfig,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(OverlayConfig::new);

        CONFIG_SPEC = specPair.getRight();
        CONFIG = specPair.getLeft();
    }

    OverlayConfig(ForgeConfigSpec.Builder builder) {
        enabled = builder
            .comment("Show overlay")
            .define("enabled", true);
        position = builder
            .comment("Position, one of: " + positions.toString())
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
            .comment("Fields to show. Will display in same order as defined. Options: " + getFields().toString())
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
            .comment("Foot level color (Format: #825432)")
            .define("footColor", "#825432", hexValidator);
        biomeLabel = builder
            .comment("Label for biome.")
            .define("biomeLabel", "Biome: ");
        biomeColor = builder
            .comment("Biome color (Format: #474f52)")
            .define("biomeColor", "#474f52", hexValidator);
    }

    public static boolean enabled() {
        return CONFIG.enabled.get();
    }

    public static String position() {
        return CONFIG.position.get();
    }

    public static int offsetX() {
        return CONFIG.offsetX.get();
    }

    public static int offsetY() {
        return CONFIG.offsetY.get();
    }

    public static double scale() {
        return CONFIG.scale.get();
    }

    @SuppressWarnings("unchecked")
    public static List<String> fields() {
        List<String> fields = (List<String>) CONFIG.fields.get();

        if (CONFIG.position.get().startsWith("BOTTOM")) {
            Collections.reverse(fields);
        }

        // Populate range colors here to initialize when building fields list.
        String[] lightColors = CONFIG.lightColorRange.get().split("->");
        String[] timeColors = CONFIG.timeColorRange.get().split("->");

        lightColorDark = ColorHelper.decode(lightColors[0]);
        lightColorBright = ColorHelper.decode(lightColors[1]);
        timeColorDark = ColorHelper.decode(timeColors[0]);
        timeColorBright = ColorHelper.decode(timeColors[1]);
        labelColorDecoded = ColorHelper.decode(CONFIG.labelColor.get());
        footColorDecoded = ColorHelper.decode(CONFIG.footColor.get());
        biomeColorDecoded = ColorHelper.decode(CONFIG.biomeColor.get());

        return fields;
    }

    public static boolean textShadow() {
        return CONFIG.textShadow.get();
    }

    public static Color labelColor() {
        return labelColorDecoded;
    }

    public static String lightLabel() {
        return CONFIG.lightLabel.get();
    }

    public static Color lightColorDark() {
        return lightColorDark;
    }

    public static Color lightColorBright() {
        return lightColorBright;
    }

    public static String timeLabel() {
        return CONFIG.timeLabel.get();
    }

    public static Color timeColorDark() {
        return timeColorDark;
    }

    public static Color timeColorBright() {
        return timeColorBright;
    }

    public static boolean timeUse12() {
        return CONFIG.timeUse12.get();
    }

    public static String footLabel() {
        return CONFIG.footLabel.get();
    }

    public static Color footColor() {
        return footColorDecoded;
    }

    public static String biomeLabel() {
        return CONFIG.biomeLabel.get();
    }

    public static Color biomeColor() {
        return biomeColorDecoded;
    }

    private static Supplier<List<? extends String>> getFields() {
        String[] fields = new String[] { "light", "time", "foot", "biome" };
        return () -> Arrays.asList(fields);
    }

}
