package yerova.botanicpledge.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import yerova.botanicpledge.common.recipes.ritual.BotanicRitualRecipe;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.List;

@JeiPlugin
public class BPRitualPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BotanicPledge.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BPRitualCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        List<BotanicRitualRecipe> botanicRitualRecipes = manager.getAllRecipesFor(BotanicRitualRecipe.Type.INSTANCE);
        registration.addRecipes(BPRitualCategory.BOTANIC_RITUAL_RECIPE_TYPE, botanicRitualRecipes);
    }

}
