package yerova.botanicpledge.api;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import yerova.botanicpledge.common.recipes.ritual.IBotanicRitualRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BotanicPledgeAPI {
    private static final BotanicPledgeAPI BOTANIC_PLEDGE_API = new BotanicPledgeAPI();
    private List<IBotanicRitualRecipe> botanicRitualRecipes = new ArrayList<>();
    private Set<RecipeType<? extends IBotanicRitualRecipe>> botanicRitualRecipeTypes = ConcurrentHashMap.newKeySet();

    private BotanicPledgeAPI(){}

    public static BotanicPledgeAPI getInstance() {
        return BOTANIC_PLEDGE_API;
    }

    public Set<RecipeType<? extends IBotanicRitualRecipe>> getEnchantingRecipeTypes() {
        return botanicRitualRecipeTypes;
    }




    public List<IBotanicRitualRecipe> getBotanicRitualRecipes(Level world) {
        List<IBotanicRitualRecipe> recipes = new ArrayList<>(botanicRitualRecipes);
        RecipeManager manager = world.getRecipeManager();
        List<IBotanicRitualRecipe> recipesByType = new ArrayList<>(); // todo lazy init enchanting types
        for(RecipeType<? extends IBotanicRitualRecipe> type : botanicRitualRecipeTypes){
            recipesByType.addAll(manager.getAllRecipesFor(type));
        }
        recipes.addAll(recipesByType);
        return recipes;
    }
}
