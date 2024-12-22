package yerova.botanicpledge.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import yerova.botanicpledge.client.render.blocks.OreInfusionRenderer;
import yerova.botanicpledge.common.blocks.block_entities.OreInfusionBlockEntity;
import yerova.botanicpledge.common.recipes.botanic_ritual.BotanicRitualRecipe;
import yerova.botanicpledge.common.recipes.ore_infusion.OreInfusionRecipe;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.List;

@JeiPlugin
public class BPRitualPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(BotanicPledge.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BPRitualCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BPOreInfusionCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        List<BotanicRitualRecipe> botanicRitualRecipes = manager.getAllRecipesFor(BotanicRitualRecipe.Type.INSTANCE);
        registration.addRecipes(BPRitualCategory.BOTANIC_RITUAL_RECIPE_TYPE, botanicRitualRecipes);

        List<OreInfusionRecipe> oreInfusionRecipes = manager.getAllRecipesFor(OreInfusionRecipe.Type.INSTANCE);
        registration.addRecipes(BPOreInfusionCategory.ORE_INFUSION_RECIPE_RECIPE_TYPE, oreInfusionRecipes);
    }

}
