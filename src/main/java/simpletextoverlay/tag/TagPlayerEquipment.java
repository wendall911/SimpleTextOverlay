package simpletextoverlay.tag;

import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
//import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;

import simpletextoverlay.overlay.InfoItem;
import simpletextoverlay.tag.registry.TagRegistry;
import simpletextoverlay.util.EntityHelper;

/*
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.ICurioItemHandler;
import top.theillusivec4.curios.api.inventory.CurioStackHandler;
*/

public abstract class TagPlayerEquipment extends Tag {

    private static Map<String, SlotInfo> slots = new HashMap<String, SlotInfo>() {
        {
            put("offhand", new SlotInfo("vanilla", -2));
            put("mainhand", new SlotInfo("vanilla", -1));
            put("helmet", new SlotInfo("vanilla", 3));
            put("chestplate", new SlotInfo("vanilla", 2));
            put("leggings", new SlotInfo("vanilla", 1));
            put("boots", new SlotInfo("vanilla", 0));
            if (ModList.get().isLoaded("curios")) {
                put("back", new SlotInfo("curio", 0));
                put("backpacked", new SlotInfo("curio", 0));
                put("belt", new SlotInfo("curio", 0));
                put("body", new SlotInfo("curio", 0));
                put("charm", new SlotInfo("curio", 0));
                put("head", new SlotInfo("curio", 0));
                put("hands", new SlotInfo("curio", 0));
                put("necklace", new SlotInfo("curio", 0));
                put("ring", new SlotInfo("curio", 0));
                put("ringtwo", new SlotInfo("curio", 1));
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
            return player.getOffhandItem();
        }

        if (slotName.equals("mainhand")) {
            return player.getMainHandItem();
        }

        if (info.type.equals("vanilla")) {
            return player.inventory.getArmor(info.slot);
        }

        if (ModList.get().isLoaded("curios") && info.type.equals("curio")) {
            /*
            return getCuriosHandler().map((curios) -> {
                String name = slotName;

                if (slotName.contains("ring")) {
                    name = "ring";
                }

                CurioStackHandler handler = curios.getStackHandler(name);
                if (handler != null) {
                    int found = 0;
                    for (int i = 0; i < handler.getSlots(); i++) {
                        ItemStack stack = handler.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            if (found == info.slot) {
                                return stack;
                            }
                            else {
                                found++;
                            }
                        }
                    }
                }
                return ItemStack.EMPTY;
            }).orElse(ItemStack.EMPTY);
            */
        }

        return ItemStack.EMPTY;
    }

    /*
    private static LazyOptional<ICurioItemHandler> getCuriosHandler() {
        return CuriosAPI.getCuriosHandler(player);
    }
    */

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
            if (itemStack.getItem() instanceof BowItem) {
                final StringBuilder arrowBuilder = new StringBuilder();
                final int regularArrows = EntityHelper.getItemCountInInventory(player.inventory, Items.ARROW);
                final int spectralArrows = EntityHelper.getItemCountInInventory(player.inventory, Items.SPECTRAL_ARROW);
                final int tippedArrows = EntityHelper.getItemCountInInventory(player.inventory, Items.TIPPED_ARROW);

                arrowBuilder.append(" (")
                        .append(regularArrows + spectralArrows + tippedArrows)
                        .append(")");

                arrows = arrowBuilder.toString();
            } else {
                arrows = "";
            }

            return itemStack.getItem().getName(itemStack).getContents() + arrows;
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

            return itemStack.getItem().getName(itemStack).getContents();
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

            Multimap<String, AttributeModifier> modifierMap = itemStack.getAttributeModifiers(EquipmentSlotType.MAINHAND);
            for (Entry<String, AttributeModifier> entry : modifierMap.entries()) {
                AttributeModifier attributeModifier = entry.getValue();
                if (attributeModifier.getId().equals(ATTACK_DAMAGE_MODIFIER)) {
                    double damageModifier = attributeModifier.getAmount();
                    //damageModifier = damageModifier + player.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
                    damageModifier = damageModifier + (double)EnchantmentHelper.getDamageBonus(itemStack, CreatureAttribute.UNDEFINED);
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

            Multimap<String, AttributeModifier> modifierMap = itemStack.getAttributeModifiers(EquipmentSlotType.MAINHAND);
            for (Entry<String, AttributeModifier> entry : modifierMap.entries()) {
                AttributeModifier attributeModifier = entry.getValue();
                if (attributeModifier.getId().equals(ATTACK_SPEED_MODIFIER)) {
                    double speedModifier = attributeModifier.getAmount();
                    //speedModifier += player.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
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

            return String.valueOf(itemStack.isDamageableItem() ? itemStack.getDamageValue() : 0);
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

            return String.valueOf(itemStack.isDamageableItem() ? itemStack.getMaxDamage() : 0);
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

            return String.valueOf(itemStack.isDamageableItem() ? itemStack.getMaxDamage() - itemStack.getDamageValue() : 0);
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
            if (!info.type.equals("curio")) {
                TagRegistry.INSTANCE.register(new Durability(key).setName(key + "durability"));
                TagRegistry.INSTANCE.register(new MaximumDurability(key).setName(key + "maxdurability"));
                TagRegistry.INSTANCE.register(new DurabilityRemaining(key).setName(key + "durabilityremaining"));
            }
            TagRegistry.INSTANCE.register(new Icon(key, false).setName(key + "icon"));
            TagRegistry.INSTANCE.register(new Icon(key, true).setName(key + "largeicon"));
        }
    }

}
