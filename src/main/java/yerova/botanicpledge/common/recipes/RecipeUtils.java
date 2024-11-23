package yerova.botanicpledge.common.recipes;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import yerova.botanicpledge.common.recipes.botanic_ritual.BotanicRitualRecipe;
import yerova.botanicpledge.common.recipes.botanic_ritual.IBotanicRitualRecipe;
import yerova.botanicpledge.common.recipes.ore_infusion.OreInfusionRecipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeUtils {

    public static List<IBotanicRitualRecipe> getBotanicRitualRecipes(Level world) {
        RecipeManager manager = world.getRecipeManager();
        List<IBotanicRitualRecipe> recipes = new ArrayList<>(manager.getAllRecipesFor(BotanicRitualRecipe.Type.INSTANCE));
        return recipes;
    }

    public static List<OreInfusionRecipe> getOreInfusionRecipes(Level world) {
        RecipeManager manager = world.getRecipeManager();
        List<OreInfusionRecipe> recipes = new ArrayList<>(manager.getAllRecipesFor(OreInfusionRecipe.Type.INSTANCE));
        return recipes;
    }

}
