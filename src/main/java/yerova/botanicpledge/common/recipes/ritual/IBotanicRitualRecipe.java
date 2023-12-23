package yerova.botanicpledge.common.recipes.ritual;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import yerova.botanicpledge.common.blocks.block_entities.RitualBaseBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;

import javax.annotation.Nullable;
import java.util.List;

public interface IBotanicRitualRecipe extends Recipe<RitualCenterBlockEntity> {
    boolean isMatch(List<ItemStack> pedestalItems, ItemStack reagent, RitualBaseBlockEntity ritualBaseBlockEntity, @Nullable Player player);

    ItemStack getResult(List<ItemStack> pedestalItems, ItemStack reagent, RitualBaseBlockEntity ritualBaseBlockEntity);

    default boolean consumesMana() {
        return getManaCost() > 0;
    }

    int getManaCost();

}
