/*
 * Derived from https://github.com/MCUpdater/IGI-Addon-ToughAsNails/blob/master/src/main/java/com/mcupdater/mods/igitoughasnails/TagTAN.java
 * License Apache License, Version 2.0
 */

package simpletextoverlay.integrations;

import net.minecraft.client.resources.I18n;

import toughasnails.api.TANCapabilities;
import toughasnails.api.temperature.IModifierMonitor;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureHandler;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.tag.registry.TagRegistry;
import simpletextoverlay.tag.Tag;

public abstract class TagToughAsNails extends Tag {

    @Override
    public String getCategory() {
        return "toughasnails";
    }

    @Override
    public String getLocalizedCategory() {
        return I18n.format("itemGroup.tabToughAsNails");
    }

    private static TemperatureHandler getTempStats() {
        return (TemperatureHandler) player.getCapability(TANCapabilities.TEMPERATURE, player.getHorizontalFacing());
    }

    private static TemperatureDebugger getTempDebug() {
        return getTempStats().debugger;
    }

    public static class BodyTemp extends TagToughAsNails {
        @Override
        public String getValue() {
            try {
                return String.valueOf((double)getTempStats().getTemperature().getRawValue());
            } catch (Throwable e) {
                SimpleTextOverlay.logger.error("Error retrieving BodyTemp. '{}'", e);
            }
            return "-1";
        }
    }

    public static class BodyTempC extends TagToughAsNails {
        @Override
        public String getValue() {
            try {
                double rawTemp = (double)getTempStats().getTemperature().getRawValue();
                double celciusTemp = 34.33 + (rawTemp * 0.223);
                return String.format("%.2f", celciusTemp);
            } catch (Throwable e) {
                SimpleTextOverlay.logger.error("Error retrieving BodyTempC. '{}'", e);
            }
            return "-1";
        }
    }

    public static class BodyTempF extends TagToughAsNails {
        @Override
        public String getValue() {
            try {
                double rawTemp = (double)getTempStats().getTemperature().getRawValue();
                double fahrenheitTemp = 93.8 + (rawTemp * 0.4);
                return String.format("%.2f", fahrenheitTemp);
            } catch (Throwable e) {
                SimpleTextOverlay.logger.error("Error retrieving BodyTempF. '{}'", e);
            }
            return "-1";
        }
    }

    public static class EnvironmentTemp extends TagToughAsNails {
        @Override
        public String getValue() {
            try {
                IModifierMonitor.Context armorModifier = getTempDebug().modifiers.get("armor");
                IModifierMonitor.Context tsArmorModifier = getTempDebug().modifiers.get("tinkersurvival:armor");
                int armorTemp = 0;

                if (armorModifier != null) {
                    armorTemp += armorModifier.endTemperature.getRawValue() - armorModifier.startTemperature.getRawValue();
                }
                if (tsArmorModifier != null) {
                    armorTemp += tsArmorModifier.endTemperature.getRawValue() - tsArmorModifier.startTemperature.getRawValue();
                }

                return String.valueOf(Math.min(Math.max(0, getTempDebug().targetTemperature - armorTemp), 25));
            } catch (Throwable e) {
                SimpleTextOverlay.logger.error("Error retrieving EnvironmentTemp. '{}'", e);
            }
            return "-1";
        }
    }

    public static class EnvironmentTempC extends TagToughAsNails {
        @Override
        public String getValue() {
            try {
                IModifierMonitor.Context armorModifier = getTempDebug().modifiers.get("armor");
                IModifierMonitor.Context tsArmorModifier = getTempDebug().modifiers.get("tinkersurvival:armor");
                int armorTemp = 0;

                if (armorModifier != null) {
                    armorTemp += armorModifier.endTemperature.getRawValue() - armorModifier.startTemperature.getRawValue();
                }
                if (tsArmorModifier != null) {
                    armorTemp += tsArmorModifier.endTemperature.getRawValue() - tsArmorModifier.startTemperature.getRawValue();
                }

                double rawTemp = (double)Math.min(Math.max(0, getTempDebug().targetTemperature - armorTemp), 25);
                double celciusTemp = -4.2 + (rawTemp * 2.1);

                return String.format("%.2f", celciusTemp);
            } catch (Throwable e) {
                SimpleTextOverlay.logger.error("Error retrieving EnvironmentTemp. '{}'", e);
            }
            return "-1";
        }
    }

    public static class EnvironmentTempF extends TagToughAsNails {
        @Override
        public String getValue() {
            try {
                IModifierMonitor.Context armorModifier = getTempDebug().modifiers.get("armor");
                IModifierMonitor.Context tsArmorModifier = getTempDebug().modifiers.get("tinkersurvival:armor");
                int armorTemp = 0;

                if (armorModifier != null) {
                    armorTemp += armorModifier.endTemperature.getRawValue() - armorModifier.startTemperature.getRawValue();
                }
                if (tsArmorModifier != null) {
                    armorTemp += tsArmorModifier.endTemperature.getRawValue() - tsArmorModifier.startTemperature.getRawValue();
                }

                double rawTemp = (double)Math.min(Math.max(0, getTempDebug().targetTemperature - armorTemp), 25);
                double fahrenheitTemp = 24.44 + (rawTemp * 3.78);

                return String.format("%.2f", fahrenheitTemp);
            } catch (Throwable e) {
                SimpleTextOverlay.logger.error("Error retrieving EnvironmentTemp. '{}'", e);
            }
            return "-1";
        }
    }

    public static class ArmorTemp extends TagToughAsNails {
        @Override
        public String getValue() {
            try {
                IModifierMonitor.Context armorModifier = getTempDebug().modifiers.get("armor");
                IModifierMonitor.Context tsArmorModifier = getTempDebug().modifiers.get("tinkersurvival:armor");
                int armorTemp = 0;

                if (armorModifier != null) {
                    armorTemp += armorModifier.endTemperature.getRawValue() - armorModifier.startTemperature.getRawValue();
                }
                if (tsArmorModifier != null) {
                    armorTemp += tsArmorModifier.endTemperature.getRawValue() - tsArmorModifier.startTemperature.getRawValue();
                }
                return String.valueOf(armorTemp);
            } catch (Throwable e) {
                SimpleTextOverlay.logger.error("Error retrieving ArmorTemp. '{}'", e);
            }
            return "-1";
        }
    }

    public static class ArmorTempC extends TagToughAsNails {
        @Override
        public String getValue() {
            try {
                IModifierMonitor.Context armorModifier = getTempDebug().modifiers.get("armor");
                IModifierMonitor.Context tsArmorModifier = getTempDebug().modifiers.get("tinkersurvival:armor");
                double armorTemp = 0.0;

                if (armorModifier != null) {
                    armorTemp += (double)armorModifier.endTemperature.getRawValue() - armorModifier.startTemperature.getRawValue();
                }
                if (tsArmorModifier != null) {
                    armorTemp += tsArmorModifier.endTemperature.getRawValue() - tsArmorModifier.startTemperature.getRawValue();
                }

                double celciusTemp = (armorTemp * 2.1);

                return String.format("%.2f", celciusTemp);
            } catch (Throwable e) {
                SimpleTextOverlay.logger.error("Error retrieving ArmorTemp. '{}'", e);
            }
            return "-1";
        }
    }

    public static class ArmorTempF extends TagToughAsNails {
        @Override
        public String getValue() {
            try {
                IModifierMonitor.Context armorModifier = getTempDebug().modifiers.get("armor");
                IModifierMonitor.Context tsArmorModifier = getTempDebug().modifiers.get("tinkersurvival:armor");
                double armorTemp = 0.0;

                if (armorModifier != null) {
                    armorTemp += (double)armorModifier.endTemperature.getRawValue() - armorModifier.startTemperature.getRawValue();
                }
                if (tsArmorModifier != null) {
                    armorTemp += tsArmorModifier.endTemperature.getRawValue() - tsArmorModifier.startTemperature.getRawValue();
                }
                double fahrenheitTemp = (armorTemp * 3.78);

                return String.format("%.2f", fahrenheitTemp);
            } catch (Throwable e) {
                SimpleTextOverlay.logger.error("Error retrieving ArmorTemp. '{}'", e);
            }
            return "-1";
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new TagToughAsNails.ArmorTemp().setName("tanarmortemp"));
        TagRegistry.INSTANCE.register(new TagToughAsNails.ArmorTempC().setName("tanarmortempc"));
        TagRegistry.INSTANCE.register(new TagToughAsNails.ArmorTempF().setName("tanarmortempf"));
        TagRegistry.INSTANCE.register(new TagToughAsNails.BodyTemp().setName("tanbodytemp"));
        TagRegistry.INSTANCE.register(new TagToughAsNails.BodyTempC().setName("tanbodytempc"));
        TagRegistry.INSTANCE.register(new TagToughAsNails.BodyTempF().setName("tanbodytempf"));
        TagRegistry.INSTANCE.register(new TagToughAsNails.EnvironmentTemp().setName("tanenvtemp"));
        TagRegistry.INSTANCE.register(new TagToughAsNails.EnvironmentTempC().setName("tanenvtempc"));
        TagRegistry.INSTANCE.register(new TagToughAsNails.EnvironmentTempF().setName("tanenvtempf"));
    }

}
