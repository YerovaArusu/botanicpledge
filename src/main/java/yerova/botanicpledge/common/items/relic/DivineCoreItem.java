package yerova.botanicpledge.common.items.relic;


import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.common.item.relic.RelicItem;
import vazkii.botania.common.lib.BotaniaTags;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.PlayerUtils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class DivineCoreItem extends RelicItem implements ICurioItem {

    private static final String TAG_MANA = "mana";
    public static final int[] LEVELS = new int[]{
            0, 10_000, 1_000_000, 10_000_000, 100_000_000, 1_000_000_000, 2_000_000_000
    };
    public static final int MAX_LEVEL_MANA = 2_000_000_000;

    public DivineCoreItem(Properties props) {
        super(props);
    }


    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.equals(Enchantments.PROJECTILE_PROTECTION)
                || enchantment.equals(Enchantments.BLAST_PROTECTION)
                || enchantment.equals(Enchantments.FIRE_PROTECTION)
                || enchantment.equals(Enchantments.ALL_DAMAGE_PROTECTION)
                ;
    }


    @Override
    public int getEnchantmentValue() {
        return 1;
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        if (slotContext.entity() instanceof Player player) {

            //Draconic Evolution Armor should not be used together with this mod, because it might lead to total unbalance of Power
            if (!PlayerUtils.checkForArmorFromMod(player, BPConstants.DRACONIC_EVOLUTION_MODID)) {

                if (player.tickCount % 20 == 0) {
                    if (player.flyDist > 0) {

                        stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
                            attribute.setManaCostPerTick(attribute.getManaCostPerTick() * BPConstants.MANA_TICK_COST_WHILE_FLIGHT_CONVERSION_RATE);
                            ManaItemHandler.instance().requestManaExact(stack, player, attribute.getManaCostPerTick(), true);
                        });
                    }
                }
            }
        }
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    public static UUID getCoreUUID(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
        String tagCoreUuidMostLegacy = "coreUUIDMost";
        String tagCoreUuidLeastLegacy = "coreUUIDLeast";
        if (tag.contains(tagCoreUuidMostLegacy) && tag.contains(tagCoreUuidLeastLegacy)) {
            UUID uuid = new UUID(tag.getLong(tagCoreUuidMostLegacy), tag.getLong(tagCoreUuidLeastLegacy));
            tag.putUUID(BPConstants.TAG_CORE_UUID, uuid);
        }
        if (!tag.hasUUID(BPConstants.TAG_CORE_UUID)) {
            UUID uuid = UUID.randomUUID();
            tag.putUUID(BPConstants.TAG_CORE_UUID, uuid);
        }
        return tag.getUUID(BPConstants.TAG_CORE_UUID);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {

        if (slotContext.entity() instanceof Player player) {
            //Draconic Evolution Armor should not be used together with this mod, because it might lead to total unbalance of Power
            if (!PlayerUtils.checkForArmorFromMod(player, BPConstants.DRACONIC_EVOLUTION_MODID)) {

                stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {

                    if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ARMOR_TAG_NAME))) {
                        double attributeValue = 0.0;
                        for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ARMOR_TAG_NAME)).toList()) {
                            attributeValue += entry.getValue();
                        }
                        AttributeModifier modifier = new AttributeModifier(getCoreUUID(stack), BPConstants.ARMOR_TAG_NAME, attributeValue, AttributeModifier.Operation.ADDITION);
                        if (!player.getAttribute(Attributes.ARMOR).hasModifier(modifier)) {
                            player.getAttribute(Attributes.ARMOR).addPermanentModifier(modifier);
                        }
                    }

                    if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ARMOR_TOUGHNESS_TAG_NAME))) {
                        double attributeValue = 0.0;
                        for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ARMOR_TOUGHNESS_TAG_NAME)).toList()) {
                            attributeValue += entry.getValue();
                        }
                        AttributeModifier modifier = new AttributeModifier(getCoreUUID(stack), BPConstants.ARMOR_TOUGHNESS_TAG_NAME, attributeValue, AttributeModifier.Operation.ADDITION);
                        if (!player.getAttribute(Attributes.ARMOR_TOUGHNESS).hasModifier(modifier)) {
                            player.getAttribute(Attributes.ARMOR_TOUGHNESS).addPermanentModifier(modifier);
                        }
                    }

                    if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.MAX_HEALTH_TAG_NAME))) {
                        double attributeValue = 0.0;
                        for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.MAX_HEALTH_TAG_NAME)).toList()) {
                            attributeValue += entry.getValue();
                        }
                        AttributeModifier modifier = new AttributeModifier(getCoreUUID(stack), BPConstants.MAX_HEALTH_TAG_NAME, attributeValue, AttributeModifier.Operation.ADDITION);
                        if (!player.getAttribute(Attributes.MAX_HEALTH).hasModifier(modifier)) {
                            player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(modifier);
                        }
                    }

                    if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.MOVEMENT_SPEED_TAG_NAME))) {
                        double attributeValue = 0.0;
                        for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.MOVEMENT_SPEED_TAG_NAME)).toList()) {
                            attributeValue += entry.getValue();
                        }

                        AttributeModifier modifier = new AttributeModifier(getCoreUUID(stack), BPConstants.MOVEMENT_SPEED_TAG_NAME, 1 + (attributeValue / 100), AttributeModifier.Operation.MULTIPLY_TOTAL);
                        if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(modifier)) {
                            player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(modifier);

                        }
                    }
                });

                this.startFlying(player);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {

            //Draconic Evolution Armor should not be used together with this mod, because it might lead to total unbalance of Power
            if (!PlayerUtils.checkForArmorFromMod(player, BPConstants.DRACONIC_EVOLUTION_MODID)) {
                AtomicInteger newCost = new AtomicInteger();
                stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(a -> newCost.set(a.getManaCostPerTick()));
                AtomicInteger oldCost = new AtomicInteger();
                stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(a -> oldCost.set(a.getManaCostPerTick()));


                if (newStack == ItemStack.EMPTY || oldCost.get() != newCost.get()) {

                    stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {

                        if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ARMOR_TAG_NAME))) {
                            double attributeValue = 0.0;
                            for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ARMOR_TAG_NAME)).toList()) {
                                attributeValue += entry.getValue();
                            }
                            AttributeModifier modifier = new AttributeModifier(getCoreUUID(stack), "Divine Core", attributeValue, AttributeModifier.Operation.ADDITION);
                            if (player.getAttribute(Attributes.ARMOR).hasModifier(modifier)) {
                                player.getAttribute(Attributes.ARMOR).removeModifier(modifier);
                            }
                        }

                        if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ARMOR_TOUGHNESS_TAG_NAME))) {
                            double attributeValue = 0.0;
                            for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ARMOR_TOUGHNESS_TAG_NAME)).toList()) {
                                attributeValue += entry.getValue();
                            }
                            AttributeModifier modifier = new AttributeModifier(getCoreUUID(stack), "Divine Core", attributeValue, AttributeModifier.Operation.ADDITION);
                            if (player.getAttribute(Attributes.ARMOR_TOUGHNESS).hasModifier(modifier)) {
                                player.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(modifier);
                            }
                        }

                        if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.MAX_HEALTH_TAG_NAME))) {
                            double attributeValue = 0.0;
                            for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.MAX_HEALTH_TAG_NAME)).toList()) {
                                attributeValue += entry.getValue();
                            }
                            AttributeModifier modifier = new AttributeModifier(getCoreUUID(stack), "Divine Core", attributeValue, AttributeModifier.Operation.ADDITION);
                            if (player.getAttribute(Attributes.MAX_HEALTH).hasModifier(modifier)) {
                                player.getAttribute(Attributes.MAX_HEALTH).removeModifier(modifier);
                            }
                        }

                        if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.MOVEMENT_SPEED_TAG_NAME))) {
                            double attributeValue = 0.0;
                            for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.MOVEMENT_SPEED_TAG_NAME)).toList()) {
                                attributeValue += entry.getValue();
                            }
                            AttributeModifier modifier = new AttributeModifier(getCoreUUID(stack), "Divine Core", 1 + (attributeValue / 100), AttributeModifier.Operation.MULTIPLY_TOTAL);
                            if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(modifier)) {
                                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(modifier);
                            }
                        }
                    });
                    this.stopFlying(player);

                }
            }
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return !PlayerUtils.checkForArmorFromMod((Player) slotContext.entity(), BPConstants.DRACONIC_EVOLUTION_MODID);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return !PlayerUtils.checkForArmorFromMod((Player) slotContext.entity(), BPConstants.DRACONIC_EVOLUTION_MODID);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return !PlayerUtils.checkForArmorFromMod((Player) slotContext.entity(), BPConstants.DRACONIC_EVOLUTION_MODID)
                && !(((Player) slotContext.entity()).getOffhandItem().getItem() instanceof DivineCoreItem);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {

        stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {

            tooltip.add(Component.literal("Shield: " + Double.parseDouble(String.format(Locale.ENGLISH, "%1.2f", ((double) attribute.getCurrentShield() / attribute.getMaxShield() * 100))) + "%").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("Charge: " + Double.parseDouble(String.format(Locale.ENGLISH, "%1.2f", ((double) attribute.getCurrentCharge() / attribute.getMaxCharge() * 100))) + "%").withStyle(ChatFormatting.GRAY));


            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ARMOR_TAG_NAME))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ARMOR_TAG_NAME)).toList()) {
                    tooltip.add(Component.literal(" +" + entry.getValue() + " " + Component.translatable(BPConstants.ARMOR_TAG_NAME).getString()).withStyle(ChatFormatting.BLUE));
                }
            }

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ARMOR_TOUGHNESS_TAG_NAME))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ARMOR_TOUGHNESS_TAG_NAME)).toList()) {
                    tooltip.add(Component.literal(" +" + entry.getValue() + " " + Component.translatable(BPConstants.ARMOR_TOUGHNESS_TAG_NAME).getString()).withStyle(ChatFormatting.BLUE));
                }
            }

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.MAX_HEALTH_TAG_NAME))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.MAX_HEALTH_TAG_NAME)).toList()) {
                    tooltip.add(Component.literal(" +" + entry.getValue() + " " + Component.translatable(BPConstants.MAX_HEALTH_TAG_NAME).getString()).withStyle(ChatFormatting.BLUE));
                }
            }

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.MOVEMENT_SPEED_TAG_NAME))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.MOVEMENT_SPEED_TAG_NAME)).toList()) {
                    tooltip.add(Component.literal(" +" + entry.getValue() + "% " + Component.translatable(BPConstants.MOVEMENT_SPEED_TAG_NAME).getString()).withStyle(ChatFormatting.BLUE));
                }
            }

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.JUMP_HEIGHT_TAG_NAME))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.JUMP_HEIGHT_TAG_NAME)).toList()) {
                    tooltip.add(Component.literal("+" + entry.getValue() + "% " + Component.translatable(BPConstants.JUMP_HEIGHT_TAG_NAME).getString()).withStyle(ChatFormatting.BLUE));
                }
            }
            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.NO_RUNE_GEM))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.NO_RUNE_GEM)).toList()) {
                    tooltip.add(Component.literal(Component.translatable(BPConstants.NO_RUNE_GEM).getString()).withStyle(ChatFormatting.BLUE));
                }
            }
        });

        super.appendHoverText(stack, world, tooltip, flags);
    }

    private void startFlying(Player player) {
        player.getAbilities().mayfly = true;
        player.onUpdateAbilities();


    }

    private void stopFlying(Player player) {
        if (player.isSpectator() || player.isCreative()) return;
        player.getAbilities().flying = false;
        player.getAbilities().mayfly = false;
        player.onUpdateAbilities();
    }


    public static int getShieldValueAccordingToRank(ItemStack stack, int defaultValue) {
        int toReturn = 0;
        if (stack.getItem() instanceof DivineCoreItem) {
            toReturn = DivineCoreItem.getLevel(stack) * defaultValue;
        }
        return toReturn;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        int level = getLevel(stack);
        int max = LEVELS[Math.min(LEVELS.length - 1, level + 1)];
        int curr = getMana_(stack);
        float percent = level == 0 ? 0F : (float) curr / (float) max;

        return Optional.of(new ManaBarTooltip(percent, level));
    }

    protected static void setMana(ItemStack stack, int mana) {
        if (mana > 0) {
            ItemNBTHelper.setInt(stack, TAG_MANA, mana);
        } else {
            ItemNBTHelper.removeEntry(stack, TAG_MANA);
        }
    }

    public static int getMana_(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
    }

    public static int getLevel(ItemStack stack) {
        long mana = getMana_(stack);
        for (int i = LEVELS.length - 1; i > 0; i--) {
            if (mana >= LEVELS[i]) {
                return i;
            }
        }

        return 0;
    }


    public static class ManaItem implements vazkii.botania.api.mana.ManaItem {
        private final ItemStack stack;

        public ManaItem(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public int getMana() {
            return getMana_(stack) * stack.getCount();
        }

        @Override
        public int getMaxMana() {
            return MAX_LEVEL_MANA * stack.getCount();
        }

        @Override
        public void addMana(int mana) {
            setMana(stack, Math.min(getMana() + mana, getMaxMana()) / stack.getCount());
        }

        @Override
        public boolean canReceiveManaFromPool(BlockEntity pool) {
            return true;
        }

        @Override
        public boolean canReceiveManaFromItem(ItemStack otherStack) {
            return !otherStack.is(BotaniaTags.Items.TERRA_PICK_BLACKLIST);
        }

        @Override
        public boolean canExportManaToPool(BlockEntity pool) {
            return false;
        }

        @Override
        public boolean canExportManaToItem(ItemStack otherStack) {
            return false;
        }

        @Override
        public boolean isNoExport() {
            return true;
        }
    }

    @Nonnull
    @Override
    public Rarity getRarity(@Nonnull ItemStack stack) {
        int level = getLevel(stack);
        if (stack.isEnchanted()) {
            level++;
        }

        if (level >= 5) { // SS rank/enchanted S rank
            return Rarity.EPIC;
        }
        if (level >= 3) { // A rank/enchanted B rank
            return Rarity.RARE;
        }
        return Rarity.UNCOMMON;
    }
}
