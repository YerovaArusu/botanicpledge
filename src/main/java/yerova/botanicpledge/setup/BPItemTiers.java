package yerova.botanicpledge.setup;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class BPItemTiers {
    public static final ForgeTier YGGDRALIUM_TIER = new ForgeTier(5, 0, 1.6f, 16, 20,
            BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(BPItems.YGGDRALIUM_INGOT.get()));

    public static final ForgeTier SOUL_TIER = new ForgeTier(5, 1, 1.6f, 0, 20,
            BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(BPItems.YGGDRALIUM_INGOT.get()));
}
