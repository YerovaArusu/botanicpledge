package yerova.botanicpledge.mixin;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.item.AncientWillContainer;
import vazkii.botania.client.integration.jei.crafting.AncientWillRecipeWrapper;
import vazkii.botania.common.item.AncientWillItem;
import vazkii.botania.common.item.BotaniaItems;
import yerova.botanicpledge.setup.BPItems;

import java.util.ArrayList;
import java.util.List;


@Mixin(AncientWillRecipeWrapper.class)
public abstract class MixinAncientWillRecipeWrapper {

    @Inject(at = @At(value = "RETURN"), method = "setRecipe", cancellable = true, remap = false)
    private void onSetRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ICraftingGridHelper helper, @NotNull IFocusGroup focusGroup, CallbackInfo ci) {

        var foci = focusGroup.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.INPUT)
                .filter(f -> f.getTypedValue().getIngredient().getItem() instanceof AncientWillItem)
                .map(f -> f.getTypedValue().getIngredient())
                .toList();

        var willStacks = !foci.isEmpty() ? foci : List.of(
                new ItemStack(BotaniaItems.ancientWillAhrim),
                new ItemStack(BotaniaItems.ancientWillDharok),
                new ItemStack(BotaniaItems.ancientWillGuthan),
                new ItemStack(BotaniaItems.ancientWillTorag),
                new ItemStack(BotaniaItems.ancientWillVerac),
                new ItemStack(BotaniaItems.ancientWillKaril)
        );

        var outputStacks = new ArrayList<ItemStack>();
        for (var will : !foci.isEmpty() ? foci : willStacks) {
            var stack = new ItemStack(BotaniaItems.terrasteelHelm);
            ((AncientWillContainer) stack.getItem()).addAncientWill(stack, ((AncientWillItem) will.getItem()).type);
            outputStacks.add(stack);
        }

        for (var will : !foci.isEmpty() ? foci : willStacks) {
            var stack = new ItemStack(BPItems.YGGDRASIL_HELMET.get());
            ((AncientWillContainer) stack.getItem()).addAncientWill(stack, ((AncientWillItem) will.getItem()).type);
            outputStacks.add(stack);
        }

        helper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK,
                List.of(List.of(new ItemStack(BotaniaItems.terrasteelHelm), new ItemStack(BPItems.YGGDRASIL_HELMET.get())), willStacks), 0, 0);
        helper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, outputStacks);

    }


}
