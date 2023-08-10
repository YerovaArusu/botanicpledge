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

    public static void setModTagValue(ItemStack stack, String tagName, int value) {
        getModTag(stack).putInt(tagName, value);
    }

    public static void setModTagValue(ItemStack stack, String tagName, double value) {
        getModTag(stack).putDouble(tagName, value);
    }

    public static void setModTagValue(ItemStack stack, String tagName, boolean bool) {
        getModTag(stack).putBoolean(tagName, bool);
    }

    public static CompoundTag getModTag(ItemStack stack) {
        return stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
    }
}
