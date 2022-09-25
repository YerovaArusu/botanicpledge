package yerova.botanicpledge.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.recipes.CoreAltarRecipe;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIBotanicPledgePlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BotanicPledge.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new
                CoreAltarRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<CoreAltarRecipe> recipes = rm.getAllRecipesFor(CoreAltarRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(CoreAltarRecipeCategory.UID, CoreAltarRecipe.class), recipes);
    }
}
