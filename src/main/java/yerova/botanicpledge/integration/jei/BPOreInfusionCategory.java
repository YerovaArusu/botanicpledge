package yerova.botanicpledge.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import yerova.botanicpledge.common.recipes.ore_infusion.OreInfusionRecipe;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BotanicPledge;

import javax.annotation.Nonnull;

public class BPOreInfusionCategory implements IRecipeCategory<OreInfusionRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(BotanicPledge.MOD_ID, "ore_infusion");
    public final static ResourceLocation TEXTURE = new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/ore_infusion/ore_infusion_jei_integration.png");
    public static final RecipeType<OreInfusionRecipe> ORE_INFUSION_RECIPE_RECIPE_TYPE = new RecipeType<>(UID, OreInfusionRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public BPOreInfusionCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BPBlocks.ORE_INFUSION.get()));
    }

    @Override
    public RecipeType<OreInfusionRecipe> getRecipeType() {
        return ORE_INFUSION_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Ore Infusion");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull OreInfusionRecipe recipe, @Nonnull IFocusGroup focusGroup) {

        builder.addSlot(RecipeIngredientRole.INPUT, 57, 34).addIngredients(recipe.getReagent());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 112,34).addItemStack(recipe.getResultItem(null));


    }
}
