package yerova.botanicpledge.setup;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class TierInit {
    public static final ForgeTier YGGDRALIUM_TIER = new ForgeTier(5, 0, 1.6f, 16, 20,
            BlockTags.NEEDS_DIAMOND_TOOL, () -> Ingredient.of(ItemInit.YGGDRALIUM_INGOT.get()));
}
