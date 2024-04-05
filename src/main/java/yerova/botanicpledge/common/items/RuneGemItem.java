package yerova.botanicpledge.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.capabilities.Attribute;
import yerova.botanicpledge.common.capabilities.AttributeProvider;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.items.relic.AsgardFractal;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.PlayerUtils;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.*;

public class RuneGemItem extends SimpleFoiledItem {
    public RuneGemItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return true;
    }

    public static double getSocketValueByChance(Attribute.Rune.StatType stat) {

        double value = switch (getRandomRarity(10000)) {
            case (BPConstants.RARITY_EPIC) -> switch (stat.name().toLowerCase()) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(7, 8);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(7, 8);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(4, 5);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(60, 75);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(60, 75);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(7, 8);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(60, 75);

                default -> 0.0;
            };
            case (BPConstants.RARITY_RARE) -> switch (stat.name().toLowerCase()) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(6, 7);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(6, 7);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(3, 4);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(45, 60);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(45, 60);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(6, 7);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(45, 60);

                default -> 0.0;
            };
            case (BPConstants.RARITY_UNCOMMON) -> switch (stat.name().toLowerCase()) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(5, 6);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(5, 6);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(2, 3);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(30, 45);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(30, 45);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(6, 7);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(30, 45);

                default -> 0.0;
            };
            case (BPConstants.RARITY_COMMON) -> switch (stat.name().toLowerCase()) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(0, 5);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(0, 5);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(0, 2);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(0, 30);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(7, 8);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(6, 7);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(0, 30);

                default -> 0.0;
            };
            default -> 0.0;
        };


        return Double.parseDouble(String.format(Locale.ENGLISH, "%1.2f", value));
    }

    public static String getRandomRarity(int bound) {
        int random = new Random().nextInt(bound);

        if (random <= bound / 250) {
            return BPConstants.RARITY_EPIC;
        } else if (random <= bound / 100) {
            return BPConstants.RARITY_RARE;
        } else if (random <= bound / 10) {
            return BPConstants.RARITY_UNCOMMON;
        } else return BPConstants.RARITY_COMMON;
    }

    public static Attribute.Rune.EquipmentType getRandomItemType() {
        return Attribute.Rune.EquipmentType.
                values()[new Random().nextInt(Attribute.Rune.EquipmentType.values().length) - 1];
    }

    public static Attribute.Rune.StatType getRandomAttribute(Attribute.Rune.EquipmentType itemType) {
        return switch (itemType) {
            case DIVINE_CORE -> Arrays.stream(Attribute.Rune.StatType.values())
                    .filter(stat -> stat != Attribute.Rune.StatType.ATTACK_SPEED &&
                            stat != Attribute.Rune.StatType.ATTACK_DAMAGE &&
                            stat != Attribute.Rune.StatType.NONE)
                    .findAny().orElse(Attribute.Rune.StatType.NONE);
            case SWORD -> Arrays.stream(Attribute.Rune.StatType.values())
                    .filter(stat -> stat == Attribute.Rune.StatType.ATTACK_SPEED || stat == Attribute.Rune.StatType.ATTACK_DAMAGE)
                    .findAny().orElse(Attribute.Rune.StatType.NONE);
            default -> Attribute.Rune.StatType.NONE;
        };
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        MutableComponent header = Component.literal("When applied on: ");
        header.append(Component.literal(getGemEquipmentType(pStack).name().toLowerCase()).withStyle(ChatFormatting.GOLD));
        header.append(":");
        pTooltipComponents.add(header);

        String attributeName = getGemAttributeType(pStack).toString().toLowerCase();

        MutableComponent textComponent;
        if (attributeName.equals(BPConstants.NO_RUNE_GEM)) {
            textComponent = Component.literal(Component.translatable(attributeName).getString());
        } else {
            textComponent = Component.literal(" + " + Component.translatable(attributeName).getString() + ": " + getGemAttributeValue(pStack));
            if (attributeName.equals(BPConstants.JUMP_HEIGHT_TAG_NAME) || attributeName.equals(BPConstants.MOVEMENT_SPEED_TAG_NAME)) {
                textComponent.append("%");
            }
        }
        pTooltipComponents.add(textComponent.withStyle(ChatFormatting.BLUE));

    }

    public static double getGemAttributeValue(ItemStack stack) {
        if (!(stack.getItem() instanceof RuneGemItem) && stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).get("gem_stat_value") == null) return 0.0;
        return stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getDouble("gem_stat_value");
    }


    public static Attribute.Rune.EquipmentType getGemEquipmentType(ItemStack stack) {
        if (stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).get("gem_equipment_type") != null) {
            return Optional.of(Attribute.Rune.EquipmentType.valueOf(stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME)
                            .getString("gem_equipment_type")
                            .toUpperCase()))
                    .orElse(Attribute.Rune.EquipmentType.NONE);
        }
        return Attribute.Rune.EquipmentType.NONE;
    }

    public static Attribute.Rune.StatType getGemAttributeType(ItemStack stack) {

        if (stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).get("gem_stat_type") != null) {
            return Optional.of(Attribute.Rune.StatType.valueOf(stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME)
                            .getString("gem_stat_type")
                            .toUpperCase()))
                    .orElse(Attribute.Rune.StatType.NONE);
        }
        return Attribute.Rune.StatType.NONE;
    }

    public static ItemStack getNewAttributedGemStack() {
        Attribute.Rune.EquipmentType et = getRandomItemType();
        Attribute.Rune.StatType st = getRandomAttribute(et);
        Attribute.Rune rune = new Attribute.Rune(et, getRandomAttribute(et), getSocketValueByChance(st));

        return rune.getAsStack();
    }

    public static double getRandomGemValueInRange(double start, double end) {
        return ((double) (end - start) / (double) BPConstants.GEM_POSSIBLE_VALUES) * ((double) new Random().nextInt(BPConstants.GEM_POSSIBLE_VALUES - 1) + 1);
    }
}
