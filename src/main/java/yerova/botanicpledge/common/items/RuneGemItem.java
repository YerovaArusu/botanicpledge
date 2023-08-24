package yerova.botanicpledge.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.capabilities.BPAttributeProvider;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.items.relic.AsgardFractal;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.PlayerUtils;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RuneGemItem extends SimpleFoiledItem {
    public RuneGemItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return true;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            if (pPlayer.getOffhandItem().getItem() instanceof RuneGemItem) {
                if (pPlayer.getMainHandItem().getItem() instanceof DivineCoreItem
                        && RuneGemItem.getGemType(pPlayer.getOffhandItem()).equals(BPConstants.GEM_TYPE_CORE)) {

                    pPlayer.getMainHandItem().getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {

                        if (attribute.setSocketAttribute(attribute.getEmptySocketIndex(),
                                getGemAttributeName(pPlayer.getOffhandItem()), RuneGemItem.getGemAttributeValue(pPlayer.getOffhandItem()))) {

                            PlayerUtils.removeItemFromInventory(pPlayer, pPlayer.getOffhandItem(), 1);
                        }
                    });
                }
                if (pPlayer.getMainHandItem().getItem() instanceof AsgardFractal
                        && RuneGemItem.getGemType(pPlayer.getOffhandItem()).equals(BPConstants.GEM_TYPE_SWORD)) {
                    pPlayer.getMainHandItem().getCapability(BPAttributeProvider.ATTRIBUTE).ifPresent(attribute -> {


                        if (attribute.setSocketAttribute(attribute.getEmptySocketIndex(),
                                getGemAttributeName(pPlayer.getOffhandItem()), RuneGemItem.getGemAttributeValue(pPlayer.getOffhandItem()))) {

                            PlayerUtils.removeItemFromInventory(pPlayer, pPlayer.getOffhandItem(), 1);
                        }
                    });
                }

            }


        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static double getSocketValueByChance(String AttributeType) {

        double value = switch (getRandomRarity(10000)) {
            case (BPConstants.RARITY_EPIC) -> switch (AttributeType) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(7, 8);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(7, 8);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(4, 5);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(60, 75);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(60, 75);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(7, 8);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(60, 75);

                default -> 0.0;
            };
            case (BPConstants.RARITY_RARE) -> switch (AttributeType) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(6, 7);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(6, 7);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(3, 4);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(45, 60);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(45, 60);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(6, 7);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(45, 60);

                default -> 0.0;
            };
            case (BPConstants.RARITY_UNCOMMON) -> switch (AttributeType) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(5, 6);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(5, 6);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(2, 3);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(30, 45);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(30, 45);

                case (BPConstants.ATTACK_DAMAGE_TAG_NAME) -> getRandomGemValueInRange(6, 7);
                case (BPConstants.ATTACK_SPEED_TAG_NAME) -> getRandomGemValueInRange(30, 45);

                default -> 0.0;
            };
            case (BPConstants.RARITY_COMMON) -> switch (AttributeType) {
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

        if (random <= bound/250) {
            return BPConstants.RARITY_EPIC;
        } else if (random <= bound/100) {
            return BPConstants.RARITY_RARE;
        } else if (random <= bound/10) {
            return BPConstants.RARITY_UNCOMMON;
        } else return BPConstants.RARITY_COMMON;
    }

    public static String getRandomItemType() {
        int rN = new Random().nextInt(2);
        String toReturn = BPConstants.GEM_TYPE_CORE;
        if (rN == 1) toReturn = BPConstants.GEM_TYPE_SWORD;
        return toReturn;
    }

    public static String getRandomAttribute(String itemType) {
        int rN;
        String toReturn = BPConstants.NO_RUNE_GEM;
        switch (itemType) {
            case (BPConstants.GEM_TYPE_CORE) -> {
                rN = new Random().nextInt(6);
                if (rN == 1) toReturn = BPConstants.ARMOR_TAG_NAME;
                if (rN == 2) toReturn = BPConstants.ARMOR_TOUGHNESS_TAG_NAME;
                if (rN == 3) toReturn = BPConstants.MAX_HEALTH_TAG_NAME;
                if (rN == 4) toReturn = BPConstants.JUMP_HEIGHT_TAG_NAME;
                if (rN == 5) toReturn = BPConstants.MOVEMENT_SPEED_TAG_NAME;
            }
            case (BPConstants.GEM_TYPE_SWORD) -> {
                rN = new Random().nextInt(2);
                if (rN == 0) toReturn = BPConstants.ATTACK_DAMAGE_TAG_NAME;
                if (rN == 1) toReturn = BPConstants.ATTACK_SPEED_TAG_NAME;
            }
        }
        return toReturn;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        TextComponent header = new TextComponent("When applied on ");
        header.append(new TranslatableComponent(getGemType(pStack)).withStyle(ChatFormatting.GOLD));
        header.append(":");
        pTooltipComponents.add(header);

        String attributeName = getGemAttributeName(pStack);
        TextComponent textComponent;
        if (attributeName.equals(BPConstants.NO_RUNE_GEM)) {
            textComponent = new TextComponent(new TranslatableComponent(attributeName).getString());
        } else {
            textComponent = new TextComponent(" + " + new TranslatableComponent(attributeName).getString() + ": " + getGemAttributeValue(pStack));
            if (attributeName.equals(BPConstants.JUMP_HEIGHT_TAG_NAME) || attributeName.equals(BPConstants.MOVEMENT_SPEED_TAG_NAME)) {
                textComponent.append("%");
            }
        }
        pTooltipComponents.add(textComponent.withStyle(ChatFormatting.BLUE));
    }

    public static double getGemAttributeValue(ItemStack stack) {
        if (!(stack.getItem() instanceof RuneGemItem)) return 0.0;
        double toReturn = 0.0;
        for (String s : stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getAllKeys()
                .stream().filter(s -> !s.equals(BPConstants.GEM_TYPE_TAG_NAME)).toList()) {
            double tmp = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getDouble(s);
            if (tmp >= 0.0) {
                toReturn = tmp;
                break;
            }
        }
        return toReturn;
    }

    public static String getGemType(ItemStack stack) {
        if (!(stack.getItem() instanceof RuneGemItem)) return BPConstants.NO_RUNE_GEM;
        String toReturn = BPConstants.NO_RUNE_GEM;
        if (stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).contains(BPConstants.GEM_TYPE_TAG_NAME)) {
            toReturn = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getString(BPConstants.GEM_TYPE_TAG_NAME);
        }
        return toReturn;
    }

    public static String getGemAttributeName(ItemStack stack) {
        if (!(stack.getItem() instanceof RuneGemItem)) return BPConstants.NO_RUNE_GEM;
        String toReturn = BPConstants.NO_RUNE_GEM;
        for (String s : stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getAllKeys()) {
            if (!s.equals(BPConstants.GEM_TYPE_TAG_NAME)) {
                toReturn = s;
                break;
            }
        }
        ;
        return toReturn;
    }

    public static ItemStack getNewAttributedGemStack(ItemStack result) {
        CompoundTag tag = result.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
        String gemType = getRandomItemType();
        String attributeName = getRandomAttribute(gemType);

        tag.putString(BPConstants.GEM_TYPE_TAG_NAME, gemType);
        tag.putDouble(attributeName, getSocketValueByChance(attributeName));
        return result;
    }

    public static double getRandomGemValueInRange(double start, double end) {
        return ((double) (end - start) / (double) BPConstants.GEM_POSSIBLE_VALUES) * ((double) new Random().nextInt(BPConstants.GEM_POSSIBLE_VALUES + 1) - 1);
    }

}
