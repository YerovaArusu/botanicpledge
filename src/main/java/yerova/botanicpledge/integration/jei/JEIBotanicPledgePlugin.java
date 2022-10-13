package yerova.botanicpledge.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import yerova.botanicpledge.common.blocks.BlockInit;
import yerova.botanicpledge.common.recipes.ritual.BotanicRitualRecipe;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIBotanicPledgePlugin implements IModPlugin {

    public static final RecipeType<BotanicRitualRecipe> BOTANIC_RITUAL_RECIPE_RECIPE_TYPE = RecipeType.create(
            BotanicPledge.MOD_ID, "botanic_ritual", BotanicRitualRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BotanicPledge.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {


        registration.addRecipeCategories(
                new BotanicRitualRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {

        List<BotanicRitualRecipe> botanicRitualRecipes = new ArrayList<>();

        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        for (Recipe<?> i : manager.getRecipes()) {
            if (i instanceof BotanicRitualRecipe botanicRitualRecipe) {
                botanicRitualRecipes.add(botanicRitualRecipe);
            }
        }

        registry.addRecipes(BOTANIC_RITUAL_RECIPE_RECIPE_TYPE, botanicRitualRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {

        registry.addRecipeCatalyst(new ItemStack(BlockInit.RITUAL_CENTER.get()), BOTANIC_RITUAL_RECIPE_RECIPE_TYPE);
    }
}
