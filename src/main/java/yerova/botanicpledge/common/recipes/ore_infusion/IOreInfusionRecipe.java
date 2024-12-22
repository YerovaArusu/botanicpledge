package yerova.botanicpledge.common.recipes.ore_infusion;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import yerova.botanicpledge.common.blocks.block_entities.OreInfusionBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualBaseBlockEntity;

import javax.annotation.Nullable;
import java.util.List;

public interface IOreInfusionRecipe extends Recipe<OreInfusionBlockEntity> {
    boolean isMatch(ItemStack reagent, OreInfusionBlockEntity oreInfusionBlockEntity, @Nullable Player player);

    ItemStack getResult(ItemStack reagent, OreInfusionBlockEntity oreInfusionBlockEntity);

    default boolean consumesMana() {
        return getManaCost() > 0;
    }

    int getManaCost();


}
