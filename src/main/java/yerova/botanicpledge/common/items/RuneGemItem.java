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
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.PlayerUtils;

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
            if (pPlayer.getOffhandItem().getItem() instanceof DivineCoreItem &&
                    pPlayer.getMainHandItem().getItem() instanceof RuneGemItem) {

                pPlayer.getOffhandItem().getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {

                    attribute.setSocketAttribute(attribute.getEmptySocketIndex(), getGemAttributeName(pPlayer.getMainHandItem()), RuneGemItem.getGemAttributeValue(pPlayer.getMainHandItem()));
                    PlayerUtils.removeItemFromInventory(pPlayer, pPlayer.getMainHandItem(), 1);
                });
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static double getSocketValueByChance(String AttributeType) {
        double value = switch (getRandomRarity()) {
            case (BPConstants.RARITY_EPIC) -> switch (AttributeType) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(7,8);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(7,8);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(4,5);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(60,75);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(60,75);
                default -> 0.0;
            };
            case (BPConstants.RARITY_RARE) -> switch (AttributeType) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(6,7);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(6,7);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(3,4);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(45,60);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(45,60);
                default -> 0.0;
            };
            case (BPConstants.RARITY_UNCOMMON) -> switch (AttributeType) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(5,6);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(5,6);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(2,3);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(30,45);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(30,45);
                default -> 0.0;
            };
            case (BPConstants.RARITY_COMMON) -> switch (AttributeType) {
                case (BPConstants.ARMOR_TAG_NAME) -> getRandomGemValueInRange(0,5);
                case (BPConstants.ARMOR_TOUGHNESS_TAG_NAME) -> getRandomGemValueInRange(0,5);
                case (BPConstants.MAX_HEALTH_TAG_NAME) -> getRandomGemValueInRange(0,2);
                case (BPConstants.JUMP_HEIGHT_TAG_NAME) -> getRandomGemValueInRange(0,30);
                case (BPConstants.MOVEMENT_SPEED_TAG_NAME) -> getRandomGemValueInRange(7,8);
                default -> 0.0;
            };
            default -> 0.0;
        };

        return Double.parseDouble(String.format(Locale.ENGLISH, "%1.2f", value));
    }

    public static String getRandomRarity() {
        int random = new Random().nextInt(10000);

        if (random <= 40) {
            return BPConstants.RARITY_EPIC;
        } else if (random <= 100) {
            return BPConstants.RARITY_RARE;
        } else if (random <= 1000) {
            return BPConstants.RARITY_UNCOMMON;
        } else return BPConstants.RARITY_COMMON;
    }

    public static String getRandomAttribute() {
        int rN = new Random().nextInt(6);
        String toReturn = BPConstants.ARMOR_TAG_NAME;

        if (rN == 1) toReturn = BPConstants.ARMOR_TAG_NAME;
        if (rN == 2) toReturn = BPConstants.ARMOR_TOUGHNESS_TAG_NAME;
        if (rN == 3) toReturn = BPConstants.MAX_HEALTH_TAG_NAME;
        if (rN == 4) toReturn = BPConstants.JUMP_HEIGHT_TAG_NAME;
        if (rN == 5) toReturn = BPConstants.MOVEMENT_SPEED_TAG_NAME;
        return toReturn;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(new TextComponent("When applied on Core Item:").withStyle(ChatFormatting.GRAY));

        String attributeName = getGemAttributeName(pStack);
        TextComponent textComponent = new TextComponent(" + " + new TranslatableComponent(attributeName).getString() + ": " + getGemAttributeValue(pStack));
        if (attributeName.equals(BPConstants.JUMP_HEIGHT_TAG_NAME) || attributeName.equals(BPConstants.MOVEMENT_SPEED_TAG_NAME)) {
            textComponent.append("%");
        }
        pTooltipComponents.add(textComponent.withStyle(ChatFormatting.BLUE));
    }

    public static double getGemAttributeValue(ItemStack stack) {
        if (!(stack.getItem() instanceof RuneGemItem)) return 0.0;
        double toReturn = 0.0;
        for (String s : stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getAllKeys()) {
            double tmp = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getDouble(s);
            if (tmp >= 0.0) {
                toReturn = tmp;
                break;
            }
        }
        return toReturn;
    }

    public static String getGemAttributeName(ItemStack stack) {
        if (!(stack.getItem() instanceof RuneGemItem)) return BPConstants.NO_RUNE_GEM;
        String toReturn = BPConstants.NO_RUNE_GEM;
        for (String s : stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getAllKeys()) {
            toReturn = s;
            break;
        }
        ;
        return toReturn;
    }

    public static ItemStack getNewAttributedGemStack(ItemStack result) {
        CompoundTag tag = result.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
        String attributeName = getRandomAttribute();
        tag.putDouble(attributeName, getSocketValueByChance(attributeName));
        return result;
    }

    public static double getRandomGemValueInRange(double start, double end) {
        return ((end - start)/BPConstants.GEM_POSSIBLE_VALUES) * (new Random().nextInt(BPConstants.GEM_POSSIBLE_VALUES-1)+1);
    }

}
