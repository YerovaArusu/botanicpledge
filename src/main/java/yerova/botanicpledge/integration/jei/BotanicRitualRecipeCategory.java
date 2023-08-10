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
import net.minecraft.world.item.crafting.Ingredient;
import yerova.botanicpledge.common.recipes.ritual.BotanicRitualRecipe;
import yerova.botanicpledge.setup.BlockInit;
import yerova.botanicpledge.setup.BotanicPledge;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BotanicRitualRecipeCategory implements IRecipeCategory<BotanicRitualRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(BotanicPledge.MOD_ID, "botanic_ritual");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(BotanicPledge.MOD_ID, "textures/gui/core_altar/core_altar_jei_integration.png");

    private final IDrawable background;
    private final IDrawable icon;


    public BotanicRitualRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(BlockInit.RITUAL_CENTER.get()));
    }


    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends BotanicRitualRecipe> getRecipeClass() {
        return BotanicRitualRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TextComponent("Botanic Ritual");
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
    public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull BotanicRitualRecipe recipe, @Nonnull IFocusGroup focusGroup) {

        for (Point p : RecipeSlots().get(RecipeIngredientRole.INPUT)) {
            int indexOfPoint = RecipeSlots().get(RecipeIngredientRole.INPUT).indexOf(p);

            if (indexOfPoint != 8) {
                Ingredient recipeItem = Ingredient.EMPTY;

                if (recipe.pedestalItems.size() - 1 >= indexOfPoint) {
                    recipeItem = recipe.pedestalItems.get(indexOfPoint);
                }
                builder.addSlot(RecipeIngredientRole.INPUT, p.x, p.y).addIngredients(recipeItem);
            } else {
                builder.addSlot(RecipeIngredientRole.INPUT, p.x, p.y).addIngredients(recipe.reagent);
            }

        }
        for (Point p : RecipeSlots().get(RecipeIngredientRole.OUTPUT)) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, p.x, p.y).addItemStack(recipe.getResultItem());
        }


    }

    public final HashMap<RecipeIngredientRole, ArrayList<Point>> RecipeSlots() {
        HashMap<RecipeIngredientRole, ArrayList<Point>> slots = new HashMap<>();

        ArrayList<Point> InputPoints = new ArrayList<>();
        InputPoints.add(new Point(39, 13));
        InputPoints.add(new Point(63, 7));
        InputPoints.add(new Point(85, 13));

        InputPoints.add(new Point(34, 35));
        InputPoints.add(new Point(90, 35));

        InputPoints.add(new Point(39, 58));
        InputPoints.add(new Point(62, 63));
        InputPoints.add(new Point(85, 58));

        InputPoints.add(new Point(62, 35)); // reagent/center


        ArrayList<Point> OutputPoints = new ArrayList<>();
        OutputPoints.add(new Point(138, 35));

        slots.put(RecipeIngredientRole.INPUT, InputPoints);
        slots.put(RecipeIngredientRole.OUTPUT, OutputPoints);

        return slots;
    }


}
