package yerova.botanicpledge.common.recipes.ritual;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import yerova.botanicpledge.setup.BPRecipes;

import java.util.ArrayList;
import java.util.List;

public class RecipeUtils {

    public static List<IBotanicRitualRecipe> getBotanicRitualRecipes(Level world) {
        RecipeManager manager = world.getRecipeManager();
        List<IBotanicRitualRecipe> recipes = new ArrayList<>(manager.getAllRecipesFor(BotanicRitualRecipe.Type.INSTANCE));
        return recipes;
    }

}
