package yerova.botanicpledge.common.recipes.ore_infusion;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.blocks.block_entities.OreInfusionBlockEntity;
import yerova.botanicpledge.setup.BotanicPledge;

public class OreInfusionRecipe implements IOreInfusionRecipe {

    private final Ingredient reagent;
    private final ItemStack result;
    private final int manaCost;
    private final ResourceLocation id;

    public OreInfusionRecipe(ItemStack result, Ingredient reagent, int manaCost, ResourceLocation id) {
        this.reagent = reagent;
        this.result = result;
        this.manaCost = manaCost;
        this.id = id;
    }

    @Override
    public boolean isMatch(ItemStack stack, OreInfusionBlockEntity oreInfusionBlockEntity, @Nullable Player player) {
        return this.reagent.test(stack);
    }

    public Ingredient getReagent() {
        return reagent;
    }

    @Override
    public ItemStack getResult(ItemStack reagent, OreInfusionBlockEntity oreInfusionBlockEntity) {
        return this.result.copy();
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }

    @Override
    public boolean matches(OreInfusionBlockEntity pContainer, Level pLevel) {
        if (pLevel.isClientSide) return false;
        return isMatch(pContainer.heldStack, pContainer, null);
    }

    @Override
    public ItemStack assemble(OreInfusionBlockEntity pContainer, RegistryAccess pRegistryAccess) {
        return this.result;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.result == null ? ItemStack.EMPTY : result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<OreInfusionRecipe> {
        public static final OreInfusionRecipe.Type INSTANCE = new OreInfusionRecipe.Type();
        public static final ResourceLocation ID = new ResourceLocation(BotanicPledge.MOD_ID, "ore_infusion");
    }


    public static class Serializer implements RecipeSerializer<OreInfusionRecipe> {
        public static final OreInfusionRecipe.Serializer INSTANCE = new OreInfusionRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(BotanicPledge.MOD_ID, "ore_infusion");


        @Override
        public OreInfusionRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            if (json.has("conditions")) {
                JsonArray conditions = GsonHelper.getAsJsonArray(json, "conditions");
                for (int i = 0; i < conditions.size(); i++) {
                    JsonObject conditionJson = conditions.get(i).getAsJsonObject();
                    ICondition condition = CraftingHelper.getCondition(conditionJson);
                    if (!condition.test(ICondition.IContext.EMPTY)) {
                        return null;
                    }
                }
            }

            Ingredient reagent = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "reagent"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            int cost = json.has("manaCost") ? GsonHelper.getAsInt(json, "manaCost") : 0;
            return new OreInfusionRecipe(output, reagent, cost, recipeId);
        }

        @Nullable
        @Override
        public OreInfusionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient reagent = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();
            int cost = buffer.readInt();
            return new OreInfusionRecipe(output, reagent, cost,recipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, OreInfusionRecipe recipe) {
            recipe.reagent.toNetwork(buf);
            buf.writeItem(recipe.result);
            buf.writeInt(recipe.manaCost);
        }


        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>) cls;
        }
    }

    @Override
    public String toString() {
        return "OreInfusionRecipe{" +
                "reagent=" + reagent +
                ", result=" + result +
                ", manaCost=" + manaCost +
                ", id=" + id +
                '}';
    }
}
