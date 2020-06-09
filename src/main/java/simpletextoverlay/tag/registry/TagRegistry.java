package simpletextoverlay.tag.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.minecraftforge.fml.ModList;

//import simpletextoverlay.integrations.TagSereneSeasons;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.tag.Tag;
//import simpletextoverlay.tag.TagFormatting;
//import simpletextoverlay.tag.TagMisc;
//import simpletextoverlay.tag.TagMouseOver;
//import simpletextoverlay.tag.TagNearbyPlayer;
//import simpletextoverlay.tag.TagPlayerEquipment;
//import simpletextoverlay.tag.TagPlayerGeneral;
//import simpletextoverlay.tag.TagPlayerPosition;
//import simpletextoverlay.tag.TagPlayerPotion;
//import simpletextoverlay.tag.TagRiding;
//import simpletextoverlay.tag.TagTime;
//import simpletextoverlay.tag.TagWorld;

public class TagRegistry {

    public static final TagRegistry INSTANCE = new TagRegistry();

    private Map<String, Tag> stringTagMap = new HashMap<>();

    private void register(final String name, final Tag tag) {
        if (this.stringTagMap.containsKey(name)) {
            SimpleTextOverlay.logger.error("Duplicate tag key '" + name + "'!");
            return;
        }

        if (name == null) {
            SimpleTextOverlay.logger.error("Tag name cannot be null!");
            return;
        }

        this.stringTagMap.put(name.toLowerCase(Locale.ENGLISH), tag);
    }

    public void register(final Tag tag) {
        register(tag.getName(), tag);

        for (final String name : tag.getAliases()) {
            register(name, tag);
        }
    }

    public String getValue(final String name) {
        final Tag tag = this.stringTagMap.get(name.toLowerCase(Locale.ENGLISH));
        return tag != null ? tag.getValue() : null;
    }

    public List<Tag> getRegisteredTags() {
        final List<Tag> tags = new ArrayList<>();
        for (final Map.Entry<String, Tag> entry : this.stringTagMap.entrySet()) {
            tags.add(entry.getValue());
        }
        return tags;
    }

    public void init() {
        //TagFormatting.register();
        //TagMisc.register();
        //TagMouseOver.register();
        //TagNearbyPlayer.register();
        //TagPlayerEquipment.register();
        //TagPlayerGeneral.register();
        //TagPlayerPosition.register();
        //TagPlayerPotion.register();
        //TagRiding.register();
        //if (ModList.get().isLoaded("sereneseasons")) {
        //    TagSereneSeasons.register();
        //}
        //TagTime.register();
        //TagWorld.register();

        SimpleTextOverlay.logger.info("Registered " + this.stringTagMap.size() + " tags.");
    }

}
