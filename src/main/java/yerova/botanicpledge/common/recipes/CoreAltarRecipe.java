package yerova.botanicpledge.common.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import yerova.botanicpledge.BotanicPledge;

import javax.annotation.Nullable;

public class CoreAltarRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    public final CompoundTag extraNBT;

    public CoreAltarRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, CompoundTag extraNBT) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.extraNBT = extraNBT;


        //TODO: ADD another Value to be read out from json, so I can manipulate the itemstacks with NBT or Attributes
        //TODO: DO IT...
        //TODO TOMORROW!!!!!!!!
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        return recipeItems.get(0).test(container.getItem(0)) &&
                recipeItems.get(1).test(container.getItem(1)) &&
                recipeItems.get(2).test(container.getItem(2)) &&
                recipeItems.get(3).test(container.getItem(3)) &&
                recipeItems.get(4).test(container.getItem(4)) &&
                recipeItems.get(5).test(container.getItem(5)) &&
                recipeItems.get(6).test(container.getItem(6)) &&
                recipeItems.get(7).test(container.getItem(7)) &&
                recipeItems.get(8).test(container.getItem(8));
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_) {
        return output;

    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
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

    public static class Type implements RecipeType<CoreAltarRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "core_altering";
    }

    public static class Serializer implements RecipeSerializer<CoreAltarRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(BotanicPledge.MOD_ID, "core_altering");

        @Override
        public CoreAltarRecipe fromJson(ResourceLocation id, JsonObject json) {

            //Output read
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            //Ingredients read
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(9, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            //NBT read

            String statName = GsonHelper.getAsString(json,"statName");
            double statValue = GsonHelper.getAsDouble(json,"statValue");

            CompoundTag nbt = output.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats");

            nbt.putDouble(statName, statValue);


            return new CoreAltarRecipe(id, output, inputs, nbt);
        }

        @Override
        public CoreAltarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            CompoundTag extraNBT = buf.readNbt();

            return new CoreAltarRecipe(id, output, inputs, extraNBT);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CoreAltarRecipe recipe) {
            //Ingredients to Network
            buf.writeInt(recipe.getIngredients().size()); //Ingredients length
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            recipe.getResultItem().setTag(recipe.extraNBT);
            //Output To Network
            buf.writeItemStack(recipe.getResultItem(), false);

            //NBT to Network
            buf.writeNbt(recipe.extraNBT);
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>) cls;
        }
    }
}
