/*
 * Derived from https://github.com/Xalcon/IGISereneSeasons/blob/master/src/main/java/net/xalcon/igisereneseasons/SereneSeasonTags.java
 * MIT License
 * Copyright (c) 2019 Xalcon
 */

package simpletextoverlay.integrations;

import java.util.function.Function;

import net.minecraft.client.resources.I18n;

import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.reference.Reference;
import simpletextoverlay.tag.registry.TagRegistry;
import simpletextoverlay.tag.Tag;

public final class TagSereneSeasons {

    private abstract static class SereneSeasonsTag extends Tag {

        @Override
        public String getCategory() {
            return "sereneseasons";
        }

    }

    private static <T> void registerTag(String tagname, Function<ISeasonState, T> callback, boolean season) {
        TagRegistry.INSTANCE.register(new SereneSeasonsTag() {
            @Override
            public String getValue() {
                try {
                    String name = String.valueOf(callback.apply(SeasonHelper.dataProvider.getClientSeasonState()));
                    if (!season) {
                        return name;
                    }
                    else {
                        return I18n.format(Reference.MODID + ".season."
                                + name.toLowerCase() + ".name");
                    }
                }
                catch(Exception ex) {
                    SimpleTextOverlay.logger.error(ex);
                    return "ERROR";
                }
            }
        }.setName(tagname));
    }

    public static void register() {
        registerTag("currentseasonord", s -> s.getSeason().ordinal(), false);
        registerTag("currentseason", s -> s.getSeason().name(), true);
        registerTag("currentsubseasonord", s -> s.getSubSeason().ordinal(), false);
        registerTag("currentsubseason", s -> s.getSubSeason().name(), true);
        registerTag("currenttropicalseason", ISeasonState::getDay, false);
        registerTag("cycleduration", ISeasonState::getCycleDuration, false);
        registerTag("cycleticks", ISeasonState::getSeasonCycleTicks, false);
        registerTag("dayduration", ISeasonState::getDayDuration, false);
        registerTag("dayofseason", s -> s.getDay() % (s.getSeasonDuration() / s.getDayDuration()), false);
        registerTag("seasonduration", ISeasonState::getSeasonDuration, false);
        registerTag("ssday", ISeasonState::getDay, false);
        registerTag("subseasonduration", ISeasonState::getSubSeasonDuration, false);
    }

}
