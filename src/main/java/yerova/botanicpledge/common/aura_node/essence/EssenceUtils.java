package yerova.botanicpledge.common.aura_node.essence;

import net.minecraft.world.item.ItemStack;

import static yerova.botanicpledge.setup.BPEssences.ESSENCE_REGISTRY;

public class EssenceUtils {

    public static boolean isEssence(ItemStack stack) {
        return ESSENCE_REGISTRY.getValues().stream().anyMatch(essence -> essence.getItemBase().equals(stack.getItem()));
    }
}
