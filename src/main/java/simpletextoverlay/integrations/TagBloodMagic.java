package simpletextoverlay.integrations;

import net.minecraft.client.resources.I18n;

import simpletextoverlay.tag.registry.TagRegistry;
import simpletextoverlay.tag.Tag;

import WayofTime.bloodmagic.util.helper.NetworkHelper;

public abstract class TagBloodMagic extends Tag {

    @Override
    public String getCategory() {
        return "bloodmagic";
    }

    @Override
    public String getLocalizedCategory() {
        return I18n.format("itemGroup.bloodmagic.creativeTab");
    }

    public static class CurrentEssence extends TagBloodMagic {
        @Override
        public String getValue() {
            return String.valueOf(NetworkHelper.getSoulNetwork(player).getCurrentEssence());
        }

        @Override
        public String getLocalizedDescription() {
            return I18n.format("tooltip.bloodmagic.sigil.divination.currentEssence", 0).replace(": 0", "");
        }
    }

    public static class OrbTier extends TagBloodMagic {
        @Override
        public String getValue() {
            return String.valueOf(NetworkHelper.getSoulNetwork(player).getOrbTier());
        }

        @Override
        public String getLocalizedDescription() {
            return I18n.format("tooltip.bloodmagic.sigil.divination.currentAltarTier", 0).replace(": 0", "");
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new TagBloodMagic.CurrentEssence().setName("bmcurrentlp"));
        TagRegistry.INSTANCE.register(new TagBloodMagic.OrbTier().setName("bmorbtier"));
    }

}
