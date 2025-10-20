package simpletextoverlay.common;

import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import org.slf4j.helpers.MessageFormatter;

import simpletextoverlay.config.OverlayConfig;

public class Translations {

    private static final Joiner LINE_JOINER = Joiner.on("\n");
    private static final Map<String, String> translations = Maps.newHashMap();

    static {
        translations.put("position", "Text info position.");
        translations.put("offsetx", "X offset");
        translations.put("offsety", "Y offset");
        translations.put("scale", "The size of the biome info (multiplier)");
        translations.put("fields", joiner(
            "Fields to show. Will display in same order as defined.",
            "Options: [\"" + String.join("\", \"", OverlayConfig.Client.fieldStrings) + "\"]"
        ));
        translations.put("textshadow", "Show text shadow.");
        translations.put("labelcolor", "Label color (Format: #9c9d97)");
        translations.put("lightlabel", "Label for light level.");
        translations.put("lightcolorrange", "Light color range (Format (dark->bright): #b02e26->#ffd83d)");
        translations.put("timelabel", "Label for time.");
        translations.put("timecolorrange", "Time color range (Format (dark->bright): #474f52->#ffd83d)");
        translations.put("timeuse12", "Use 12 hour AM/PM display.");
        translations.put("footlabel", "Label for foot level.");
        translations.put("footcolor", "Foot level color (Format: #5d7c15)");
        translations.put("biomelabel", "Label for biome.");
        translations.put("biomecolor", "Biome color (Format: #474f52)");
        translations.put("enabled", "Show HUD compass.");
        translations.put("compassopacity", "Compass background opacity.");
        translations.put("dayslabel", "Label for total days.");
        translations.put("dayscolor", "Days color (Format: #3c44a9)");
    }

    public static String get(String key) {
        return translations.getOrDefault(key, key);
    }

    public static String get(String key, String... values) {
        return MessageFormatter.arrayFormat(translations.getOrDefault(key, key), values).getMessage();
    }

    private static String joiner(String... string) {
        return LINE_JOINER.join(string);
    }

}
