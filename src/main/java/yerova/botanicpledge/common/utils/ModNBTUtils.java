package yerova.botanicpledge.common.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ModNBTUtils {

    public static boolean hasModTag(ItemStack stack, String toSearchFor) {
        return stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).contains(toSearchFor);
    }

    public static void manipulateModTagValue(ItemStack stack, String tagName, int value) {
        getModTag(stack).putInt(tagName, getModTag(stack).getInt(tagName) + value);
    }

    public static void manipulateModTagValue(ItemStack stack, String tagName, double value) {
        getModTag(stack).putDouble(tagName, getModTag(stack).getDouble(tagName) + value);
    }

    public static CompoundTag getModTag(ItemStack stack) {
        return stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
    }
}
