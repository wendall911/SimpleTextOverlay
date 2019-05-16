package simpletextoverlay.tag;

import com.google.common.collect.Multimap;

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

import simpletextoverlay.client.gui.overlay.InfoItem;
import simpletextoverlay.tag.registry.TagRegistry;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.EntityHelper;

public abstract class TagPlayerEquipment extends Tag {
    public static final String[] TYPES = new String[] {
            "offhand", "mainhand", "helmet", "chestplate", "leggings", "boots"
    };
    public static final int[] SLOTS = new int[] {
            -2, -1, 3, 2, 1, 0
    };
    protected final int slot;

    public TagPlayerEquipment(final int slot) {
        this.slot = slot;
    }

    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    @Override
    public String getCategory() {
        return "playerequipment";
    }

    protected ItemStack getItemStack(final int slot) {
        if (slot == -2) {
            return player.getHeldItemOffhand();
        }

        if (slot == -1) {
            return player.getHeldItemMainhand();
        }

        return player.inventory.armorItemInSlot(slot);
    }

    public static class Name extends TagPlayerEquipment {
        public Name(final int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slot);
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
        public UniqueName(final int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slot);
            if (itemStack.isEmpty()) {
                return "";
            }

            return String.valueOf(Item.REGISTRY.getNameForObject(itemStack.getItem()));
        }
    }

    public static class AttackDamage extends TagPlayerEquipment {
        public AttackDamage(final int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slot);
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
        public AttackSpeed(final int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slot);
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
        public Durability(final int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slot);
            if (itemStack.isEmpty()) {
                return String.valueOf(-1);
            }

            return String.valueOf(itemStack.isItemStackDamageable() ? itemStack.getItemDamage() : 0);
        }
    }

    public static class MaximumDurability extends TagPlayerEquipment {
        public MaximumDurability(final int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slot);
            if (itemStack.isEmpty()) {
                return String.valueOf(-1);
            }

            return String.valueOf(itemStack.isItemStackDamageable() ? itemStack.getMaxDamage() : 0);
        }
    }

    public static class DurabilityRemaining extends TagPlayerEquipment {
        public DurabilityRemaining(final int slot) {
            super(slot);
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slot);
            if (itemStack.isEmpty()) {
                return String.valueOf(-1);
            }

            return String.valueOf(itemStack.isItemStackDamageable() ? itemStack.getMaxDamage() - itemStack.getItemDamage() : 0);
        }
    }

    public static class Icon extends TagPlayerEquipment {
        private final boolean large;

        public Icon(final int slot, final boolean large) {
            super(slot);
            this.large = large;
        }

        @Override
        public String getValue() {
            final ItemStack itemStack = getItemStack(this.slot);
            final InfoItem item = new InfoItem(itemStack, this.large);
            info.add(item);
            return getIconTag(item);
        }
    }

    public static void register() {
        for (int i = 0; i < 2; i++) {
            TagRegistry.INSTANCE.register(new AttackDamage(SLOTS[i]).setName(TYPES[i] + "attackdamage"));
            TagRegistry.INSTANCE.register(new AttackDamage(SLOTS[i]).setName(TYPES[i] + "attackdamage"));
            TagRegistry.INSTANCE.register(new AttackSpeed(SLOTS[i]).setName(TYPES[i] + "attackspeed"));
            TagRegistry.INSTANCE.register(new AttackSpeed(SLOTS[i]).setName(TYPES[i] + "attackspeed"));
        }
        for (int i = 0; i < TYPES.length; i++) {
            TagRegistry.INSTANCE.register(new Name(SLOTS[i]).setName(TYPES[i] + "name"));
            TagRegistry.INSTANCE.register(new UniqueName(SLOTS[i]).setName(TYPES[i] + "uniquename"));
            TagRegistry.INSTANCE.register(new Durability(SLOTS[i]).setName(TYPES[i] + "durability"));
            TagRegistry.INSTANCE.register(new MaximumDurability(SLOTS[i]).setName(TYPES[i] + "maxdurability"));
            TagRegistry.INSTANCE.register(new DurabilityRemaining(SLOTS[i]).setName(TYPES[i] + "durabilityremaining"));
            TagRegistry.INSTANCE.register(new Icon(SLOTS[i], false).setName(TYPES[i] + "icon"));
            TagRegistry.INSTANCE.register(new Icon(SLOTS[i], true).setName(TYPES[i] + "largeicon"));
        }
    }
}
