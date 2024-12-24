package yerova.botanicpledge.common.items.relic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.common.item.relic.RelicItem;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
    Inspiration for this Item comes from the Buddhist Relic in the Extra Botany (Version 1.16.5)
    https://legacy.curseforge.com/minecraft/mc-mods/extrabotany

    Of course my Solution is better because it can also save enchantments and other Tags applied to the selected
    Item.
 */
public class FirstRelic extends RelicItem {


    private static final LazyOptional<List<ItemStack>> relics = LazyOptional.of(() -> Arrays.asList(
            new ItemStack(BotaniaItems.kingKey),
            new ItemStack(BotaniaItems.infiniteFruit),
            new ItemStack(BotaniaItems.flugelEye),
            new ItemStack(BPItems.ASGARD_FRACTAL.get()),
            new ItemStack(BPItems.YGGD_RAMUS.get()),
            new ItemStack(BPItems.ULL_BOW.get())
    ));

    public static LazyOptional<List<ItemStack>> getRelics() {
        return relics;
    }


    public FirstRelic(Properties props) {
        super(props);
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    public static ArrayList<ItemStack> getRelicStacks(ItemStack stack) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        if (isPartOfFirstRelic(stack)) {
            CompoundTag tag = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".relic_items");
            for (int i = 1; i <= stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".relic_items").getAllKeys().size(); i++) {
                stacks.add(ItemStack.of(tag.getCompound(i + "_item")));
            }
        }
        return stacks;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {

        Component component = Component.translatable("item.botanicpledge.first_relic.ability_desc");
        tooltip.add(component);

        super.appendHoverText(stack, world, tooltip, flags);
    }

    public static void putRelicStacks(List<ItemStack> stacks, ItemStack heldItem) {
        CompoundTag tag = heldItem.getOrCreateTagElement(BotanicPledge.MOD_ID + ".relic_items");
        for (int i = 1; i <= stacks.size(); i++) {
            tag.put(i + "_item", stacks.get(i - 1).save(new CompoundTag()));
        }
    }

    public static void switchRelic(Player player, Level level, ItemStack heldItem) {

        if (!level.isClientSide) {
            int slot = player.getInventory().findSlotMatchingItem(heldItem);

            ArrayList<ItemStack> stacks = getRelicStacks(heldItem);
            clearRelicItemNBT(heldItem);
            stacks.add(heldItem);             //add new one at last index and
            player.getInventory().removeItem(heldItem);
            player.getInventory().add(slot, stacks.get(0));
            stacks.remove(0);           //remove first one, so one can cycle through the items
            putRelicStacks(stacks, player.getInventory().getItem(slot));

        }

    }

    public static boolean isPartOfFirstRelic(ItemStack stack) {
        if (stack.getItem() instanceof FirstRelic && !stack.getTag().contains(BotanicPledge.MOD_ID + ".relic_items")) {
            setupDefaultNBT(stack);
        }
        return (!stack.isEmpty() && stack.getTag() != null
                && stack.getTag().contains(BotanicPledge.MOD_ID + ".relic_items"));

    }

    public static void setupDefaultNBT(ItemStack stack) {
        putRelicStacks(relics.resolve().orElse(new ArrayList<>()), stack);
    }

    public static void clearRelicItemNBT(ItemStack stack) {
        if (!stack.isEmpty() && stack.getTag() != null
                && stack.getTag().contains(BotanicPledge.MOD_ID + ".relic_items")) {
            stack.getTag().remove(BotanicPledge.MOD_ID + ".relic_items");
        }
    }

}
