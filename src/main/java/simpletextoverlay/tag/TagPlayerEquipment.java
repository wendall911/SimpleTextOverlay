package simpletextoverlay.tag;

import baubles.api.BaublesApi;

import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import net.minecraftforge.fml.common.Loader;

import simpletextoverlay.client.gui.overlay.InfoItem;
import simpletextoverlay.tag.registry.TagRegistry;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.EntityHelper;

public abstract class TagPlayerEquipment extends Tag {

    private static Map<String, SlotInfo> slots = new HashMap<String, SlotInfo>() {
        {
            put("offhand", new SlotInfo("vanilla", -2));
            put("mainhand", new SlotInfo("vanilla", -1));
            put("helmet", new SlotInfo("vanilla", 3));
            put("chestplate", new SlotInfo("vanilla", 2));
            put("leggings", new SlotInfo("vanilla", 1));
            put("boots", new SlotInfo("vanilla", 0));

            if (Loader.isModLoaded("baubles")) {
                put("amulet", new SlotInfo("bauble", 0));
                put("ringone", new SlotInfo("bauble", 1));
                put("ringtwo", new SlotInfo("bauble", 2));
                put("belt", new SlotInfo("bauble", 3));
                put("head", new SlotInfo("bauble", 4));
                put("body", new SlotInfo("bauble", 5));
                put("charm", new SlotInfo("bauble", 6));
            }
        }
    };

    protected final String slotName;

    public TagPlayerEquipment(final String slotName) {
        this.slotName = slotName;
    }

    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    @Override
    public String getCategory() {
        return "playerequipment";
    }

    private static class SlotInfo {

        public String type;
        public int slot;

        public SlotInfo(String type, int slot) {
            this.type = type;
            this.slot = slot;
        }

    }

    protected ItemStack getItemStack(final String slotName) {
        SlotInfo info = slots.get(slotName);

        if (slotName.equals("offhand")) {
            return player.getHeldItemOffhand();
        }

        if (slotName.equals("mainhand")) {
            return player.getHeldItemMainhand();
        }

        if (info.type.equals("vanilla")) {
            return player.inventory.armorItemInSlot(info.slot);
        }

        if (info.type.equals("bauble")) {
            return BaublesApi.getBaubles(player).getStackInSlot(info.slot);
        }

        return ItemStack.EMPTY;
    }

    public static class Name extends TagPlayerEquipment {
        public Name(final String slotName) {
            super(slotName);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slotName);
            if (itemStack.isEmpty()) {
                return "";
            }

            final String arrows;
            if (itemStack.getItem() instanceof ItemBow) {
                final StringBuilder arrowBuilder = new StringBuilder();
                final int regularArrows = EntityHelper.getItemCountInInventory(player.inventory, Items.ARROW);
                final int spectralArrows = EntityHelper.getItemCountInInventory(player.inventory, Items.SPECTRAL_ARROW);
                final int tippedArrows = EntityHelper.getItemCountInInventory(player.inventory, Items.TIPPED_ARROW);

                arrowBuilder.append(" (")
                        .append(Integer.toString(regularArrows + spectralArrows + tippedArrows))
                        .append(")");

                arrows = arrowBuilder.toString();
            } else {
                arrows = "";
            }

            return itemStack.getDisplayName() + arrows;
        }
    }

    public static class UniqueName extends TagPlayerEquipment {
        public UniqueName(final String slotName) {
            super(slotName);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slotName);
            if (itemStack.isEmpty()) {
                return "";
            }

            return String.valueOf(Item.REGISTRY.getNameForObject(itemStack.getItem()));
        }
    }

    public static class AttackDamage extends TagPlayerEquipment {
        public AttackDamage(final String slotName) {
            super(slotName);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slotName);
            String attackDamage = "";

            Multimap<String, AttributeModifier> modifierMap = itemStack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            for (Entry<String, AttributeModifier> entry : modifierMap.entries()) {
                AttributeModifier attributeModifier = entry.getValue();
                if (attributeModifier.getID().equals(ATTACK_DAMAGE_MODIFIER)) {
                    double damageModifier = attributeModifier.getAmount();
                    damageModifier = damageModifier + player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
                    damageModifier = damageModifier + (double)EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
                    attackDamage = String.format("%.1f", Math.round(damageModifier * 10.0) / 10.0);
                }
            }

            return attackDamage;
        }
    }

    public static class AttackSpeed extends TagPlayerEquipment {
        public AttackSpeed(final String slotName) {
            super(slotName);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slotName);
            String attackSpeed = "";

            Multimap<String, AttributeModifier> modifierMap = itemStack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            for (Entry<String, AttributeModifier> entry : modifierMap.entries()) {
                AttributeModifier attributeModifier = entry.getValue();
                if (attributeModifier.getID().equals(ATTACK_SPEED_MODIFIER)) {
                    double speedModifier = attributeModifier.getAmount();
                    speedModifier += player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
                    attackSpeed = String.format("%.2f", Math.round(speedModifier * 100.0) / 100.0);
                }
            }

            return attackSpeed;
        }
    }

    public static class Durability extends TagPlayerEquipment {
        public Durability(final String slotName) {
            super(slotName);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slotName);
            if (itemStack.isEmpty()) {
                return String.valueOf(-1);
            }

            return String.valueOf(itemStack.isItemStackDamageable() ? itemStack.getItemDamage() : 0);
        }
    }

    public static class MaximumDurability extends TagPlayerEquipment {
        public MaximumDurability(final String slotName) {
            super(slotName);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slotName);
            if (itemStack.isEmpty()) {
                return String.valueOf(-1);
            }

            return String.valueOf(itemStack.isItemStackDamageable() ? itemStack.getMaxDamage() : 0);
        }
    }

    public static class DurabilityRemaining extends TagPlayerEquipment {
        public DurabilityRemaining(final String slotName) {
            super(slotName);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slotName);
            if (itemStack.isEmpty()) {
                return String.valueOf(-1);
            }

            return String.valueOf(itemStack.isItemStackDamageable() ? itemStack.getMaxDamage() - itemStack.getItemDamage() : 0);
        }
    }

    public static class Icon extends TagPlayerEquipment {
        private final boolean large;

        public Icon(final String slotName, final boolean large) {
            super(slotName);
            this.large = large;
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slotName);
            final InfoItem item = new InfoItem(itemStack, this.large);
            info.add(item);
            return getIconTag(item);
        }
    }

    public static void register() {
        for (String key : slots.keySet()) {
            SlotInfo info = slots.get(key);

            if (key.equals("mainhand") || key.equals("offhand")) {
                TagRegistry.INSTANCE.register(new AttackDamage(key).setName(key + "attackdamage"));
                TagRegistry.INSTANCE.register(new AttackSpeed(key).setName(key + "attackspeed"));
            }

            TagRegistry.INSTANCE.register(new Name(key).setName(key + "name"));
            TagRegistry.INSTANCE.register(new UniqueName(key).setName(key + "uniquename"));
            if (!info.type.equals("bauble")) {
                TagRegistry.INSTANCE.register(new Durability(key).setName(key + "durability"));
                TagRegistry.INSTANCE.register(new MaximumDurability(key).setName(key + "maxdurability"));
                TagRegistry.INSTANCE.register(new DurabilityRemaining(key).setName(key + "durabilityremaining"));
            }
            TagRegistry.INSTANCE.register(new Icon(key, false).setName(key + "icon"));
            TagRegistry.INSTANCE.register(new Icon(key, true).setName(key + "largeicon"));
        }
    }

}
