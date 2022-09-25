package yerova.botanicpledge.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.blocks.BlockInit;
import yerova.botanicpledge.common.recipes.CoreAltarRecipe;

import javax.annotation.Nonnull;

public class CoreAltarRecipeCategory implements IRecipeCategory<CoreAltarRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(BotanicPledge.MOD_ID, "core_altering");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/core_altar/core_altar_jei_integration.png");

    private final IDrawable background;
    private final IDrawable icon;


    public CoreAltarRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(BlockInit.CORE_ALTAR.get()));
    }


    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CoreAltarRecipe> getRecipeClass() {
        return CoreAltarRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TextComponent("Core Altar");
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull CoreAltarRecipe recipe, @Nonnull IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 39, 13).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 7).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 85, 13).addIngredients(recipe.getIngredients().get(2));

        builder.addSlot(RecipeIngredientRole.INPUT, 34, 35).addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 35).addIngredients(recipe.getIngredients().get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 90, 35).addIngredients(recipe.getIngredients().get(5));

        builder.addSlot(RecipeIngredientRole.INPUT, 39, 58).addIngredients(recipe.getIngredients().get(6));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 63).addIngredients(recipe.getIngredients().get(7));
        builder.addSlot(RecipeIngredientRole.INPUT, 85, 58).addIngredients(recipe.getIngredients().get(8));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 138, 35).addItemStack(recipe.getResultItem());
    }


}
