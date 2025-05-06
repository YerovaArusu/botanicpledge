package yerova.botanicpledge.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.capabilities.Attribute;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.setup.BPEnchantments;

import java.util.*;

public class RuneGemItem extends SimpleFoiledItem {
    public RuneGemItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return true;
    }

    public static double getSocketValueByChance(Attribute.Rune.StatType stat, RandomSource random) {

        double value = switch (getRandomRarity(10000)) {
            case (BPConstants.RARITY_EPIC) -> switch (stat.name().toLowerCase()) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(7, 8, random);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(7, 8, random);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(4, 5, random);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(60, 75, random);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(60, 75, random);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(7, 8, random);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(60, 75, random);

                default -> 0.0;
            };
            case (BPConstants.RARITY_RARE) -> switch (stat.name().toLowerCase()) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(6, 7, random);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(6, 7, random);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(3, 4, random);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(45, 60, random);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(45, 60, random);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(6, 7, random);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(45, 60, random);

                default -> 0.0;
            };
            case (BPConstants.RARITY_UNCOMMON) -> switch (stat.name().toLowerCase()) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(5, 6, random);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(5, 6, random);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(2, 3, random);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(30, 45, random);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(30, 45, random);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(6, 7, random);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(30, 45, random);

                default -> 0.0;
            };
            case (BPConstants.RARITY_COMMON) -> switch (stat.name().toLowerCase()) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(0, 5, random);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(0, 5, random);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(0, 2, random);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(0, 30, random);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(7, 8, random);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(6, 7, random);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(0, 30, random);

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

    public static Attribute.Rune.EquipmentType getRandomItemType(RandomSource random) {
        return Attribute.Rune.EquipmentType.values()[random.nextInt(Attribute.Rune.EquipmentType.values().length)];
    }

    public static Attribute.Rune.StatType getRandomAttribute(Attribute.Rune.EquipmentType itemType, RandomSource random) {
        List<Attribute.Rune.StatType> filtered = switch (itemType) {
            case DIVINE_CORE -> Arrays.stream(Attribute.Rune.StatType.values())
                    .filter(stat -> stat != Attribute.Rune.StatType.ATTACK_SPEED &&
                            stat != Attribute.Rune.StatType.ATTACK_DAMAGE &&
                            stat != Attribute.Rune.StatType.NONE)
                    .toList();
            case SWORD -> Arrays.stream(Attribute.Rune.StatType.values())
                    .filter(stat -> stat == Attribute.Rune.StatType.ATTACK_SPEED ||
                            stat == Attribute.Rune.StatType.ATTACK_DAMAGE)
                    .toList();
            default -> List.of();
        };
        return filtered.isEmpty() ? Attribute.Rune.StatType.NONE : filtered.get(random.nextInt(filtered.size()));
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
        if (!(stack.getItem() instanceof RuneGemItem) && stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).get("gem_stat_value") == null)
            return 0.0;
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

    public static ItemStack getNewAttributedGemStack(LootContext context, Map<String,Float> rarities) {

        RandomSource random = context.getRandom();

        Entity killer = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (!(killer instanceof LivingEntity living)) return ItemStack.EMPTY;

        int runeCollectorLevel = EnchantmentHelper.getEnchantmentLevel(BPEnchantments.RUNE_COLLECTOR_ENCHANTMENT.get(), living);

        Map<String, Float> scaledChances = new HashMap<>();

        float totalChance = 0f;
        for (Map.Entry<String, Float> entry : rarities.entrySet()) {
            float scaledChance = entry.getValue() * runeCollectorLevel;
            scaledChances.put(entry.getKey(), scaledChance);
            totalChance += scaledChance;
        }

        if (totalChance <= 0f) return ItemStack.EMPTY;

        float roll = random.nextFloat() * totalChance;
        float cumulative = 0f;

        Attribute.Rune.EquipmentType random_et = getRandomItemType(random);
        Attribute.Rune.StatType random_st = null;

        for (Map.Entry<String, Float> entry : scaledChances.entrySet()) {
            cumulative += entry.getValue();
            if (roll <= cumulative) {
                random_st = getRandomAttribute(random_et, random);
                break;
            }
        }

        Attribute.Rune rune = new Attribute.Rune(random_et, random_st, getSocketValueByChance(random_st, random));

        return rune.getAsStack();
    }

    public static double getRandomGemValueInRange(double start, double end, RandomSource random) {
        int steps = BPConstants.GEM_POSSIBLE_VALUES;
        return ((end - start) / steps) * (random.nextInt(steps - 1) + 1);
    }

}
