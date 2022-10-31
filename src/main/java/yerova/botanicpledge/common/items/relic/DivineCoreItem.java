package yerova.botanicpledge.common.items.relic;


import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DivineCoreItem extends ItemRelic implements ICurioItem {
    public static final DamageSource HEALTH_SET_DMG_SRC = new DamageSource("health_set");
    private static final String TAG_CORE_UUID = "coreUUID";

    public DivineCoreItem(Properties props) {
        super(props);
    }

    public static ArrayList<Attribute> attributeList() {
        ArrayList<Attribute> list = new ArrayList<Attribute>();
        list.add(Attributes.ARMOR);
        list.add(Attributes.ARMOR_TOUGHNESS);
        list.add(Attributes.MAX_HEALTH);
        list.add(Attributes.ATTACK_DAMAGE);
        list.add(Attributes.KNOCKBACK_RESISTANCE);
        list.add(Attributes.MOVEMENT_SPEED);
        list.add(Attributes.ATTACK_SPEED);


        return list;
    }

    public static ArrayList<String> attributeNameList() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("armor");
        list.add("armor_toughness");
        list.add("max_health");
        list.add("attack_damage");
        list.add("knockback_resistance");
        list.add("movement_speed");
        list.add("attack_speed");

        return list;
    }

    public static IRelic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    public static UUID getCoreUUID(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTagElement(AttributedItemsUtils.TAG_STATS_SUBSTAT);

        // Legacy handling
        String tagCoreUuidMostLegacy = "coreUUIDMost";
        String tagCoreUuidLeastLegacy = "coreUUIDLeast";
        if (tag.contains(tagCoreUuidMostLegacy) && tag.contains(tagCoreUuidLeastLegacy)) {
            UUID uuid = new UUID(tag.getLong(tagCoreUuidMostLegacy), tag.getLong(tagCoreUuidLeastLegacy));
            tag.putUUID(TAG_CORE_UUID, uuid);
        }

        if (!tag.hasUUID(TAG_CORE_UUID)) {
            UUID uuid = UUID.randomUUID();
            tag.putUUID(TAG_CORE_UUID, uuid);
        }

        return tag.getUUID(TAG_CORE_UUID);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {


        if (slotContext.entity() instanceof Player) {
            Player player = ((Player) slotContext.entity());

            for (int i = 0; i < attributeList().size(); i++) {
                double addValue = stack.getOrCreateTagElement(AttributedItemsUtils.TAG_STATS_SUBSTAT).getInt(attributeNameList().get(i));
                AttributeModifier statModifier = new AttributeModifier(getCoreUUID(stack), "Divine Core", addValue, AttributeModifier.Operation.ADDITION);

                if (!player.getAttribute(attributeList().get(i)).hasModifier(statModifier)) {
                    player.getAttribute(attributeList().get(i)).addPermanentModifier(statModifier);
                }
            }

            if (stack.getTag().contains(AttributedItemsUtils.TAG_STATS_SUBSTAT)
                    && stack.getOrCreateTagElement(AttributedItemsUtils.TAG_STATS_SUBSTAT).contains("may_fly")) {

                this.startFlying(player);

            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {

        if (slotContext.entity() instanceof Player) {
            Player player = ((Player) slotContext.entity());

            if (newStack.getItem() != stack.getItem()) {
                for (int i = 0; i < attributeList().size(); i++) {

                    double reducerValue = stack.getOrCreateTagElement(AttributedItemsUtils.TAG_STATS_SUBSTAT).getInt(attributeNameList().get(i));
                    AttributeModifier statModifier = new AttributeModifier(getCoreUUID(stack), "Divine Core", reducerValue, AttributeModifier.Operation.ADDITION);
                    AttributeInstance statAttribute = player.getAttribute(attributeList().get(i));

                    if (statAttribute.hasModifier(statModifier)) {
                        statAttribute.removeModifier(statModifier);

                        if (attributeNameList().get(i).equals(attributeNameList().get(2))) {

                            if (player.getHealth() > slotContext.entity().getMaxHealth()) {
                                player.hurt(HEALTH_SET_DMG_SRC, slotContext.entity().getAbsorptionAmount() + (float) reducerValue);
                            }
                        }
                    }
                }

                if (stack.getTag().contains(AttributedItemsUtils.TAG_STATS_SUBSTAT)
                        && stack.getOrCreateTagElement(AttributedItemsUtils.TAG_STATS_SUBSTAT).contains("may_fly")) {

                    this.stopFlying(player);

                }
            }
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        if (Screen.hasShiftDown()) {
            CompoundTag statsTag = stack.getOrCreateTagElement(AttributedItemsUtils.TAG_STATS_SUBSTAT);
            for (String s : statsTag.getAllKeys()) {
                if (attributeNameList().contains(s)) {
                    tooltip.add(new TextComponent("+" + statsTag.getDouble(s) + " " + new TranslatableComponent(s).getString()).withStyle(ChatFormatting.BLUE));
                }
                if (s.equals("jump_height")) {
                    tooltip.add(new TextComponent("+" + statsTag.getDouble("jump_height") + " " + new TranslatableComponent("jump_height").getString()).withStyle(ChatFormatting.BLUE));
                }
                if (s.equals("may_fly")) {
                    tooltip.add(new TextComponent(new TranslatableComponent("may_fly").getString()).withStyle(ChatFormatting.BLUE));
                }
            }
        } else {
            tooltip.add(new TranslatableComponent("show_tooltip_stats", new TextComponent("LShift").withStyle(ChatFormatting.BLUE)));
        }


        super.appendHoverText(stack, world, tooltip, flags);
    }

    private void startFlying(Player player) {
        player.getAbilities().mayfly = true;
        player.onUpdateAbilities();
    }

    private void stopFlying(Player player) {
        player.getAbilities().flying = false;
        player.getAbilities().mayfly = false;
        player.onUpdateAbilities();
    }
}
