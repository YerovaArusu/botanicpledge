package yerova.botanicpledge.common.recipes.ritual;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.blocks.block_entities.RitualBaseBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.RitualCenterBlockEntity;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BotanicRitualRecipe implements IBotanicRitualRecipe {


    public Ingredient reagent; // Used in the arcane pedestal
    public ItemStack result; // Result item
    public List<Ingredient> pedestalItems; // Items part of the recipe
    public ResourceLocation id;
    public int manaCost;
    public boolean keepNbtOfReagent = false;
    public HashMap<String, Integer> additionalAttributes = null;
    public CompoundTag gemCompoundTag = null;

    public static final String RECIPE_ID = "botanic_ritual";

    public BotanicRitualRecipe(ItemStack result, Ingredient reagent, List<Ingredient> pedestalItems) {
        this.reagent = reagent;
        this.pedestalItems = pedestalItems;
        this.result = result;
        manaCost = 0;
        this.id = new ResourceLocation(BotanicPledge.MOD_ID, result.getItem().getRegistryName().getPath());
    }

    public BotanicRitualRecipe(ResourceLocation id, List<Ingredient> pedestalItems, Ingredient reagent, ItemStack result, int manaCost) {
        this(id, pedestalItems, reagent, result, manaCost, false);
    }

    public BotanicRitualRecipe(ResourceLocation id, List<Ingredient> pedestalItems, Ingredient reagent, ItemStack result, int cost, boolean keepNbtOfReagent) {
        this.reagent = reagent;
        this.pedestalItems = pedestalItems;
        this.result = result;
        manaCost = cost;
        this.id = id;
        this.keepNbtOfReagent = keepNbtOfReagent;
    }

    public BotanicRitualRecipe() {
        reagent = Ingredient.EMPTY;
        result = ItemStack.EMPTY;
        pedestalItems = new ArrayList<>();
        manaCost = 0;
        this.id = new ResourceLocation(BotanicPledge.MOD_ID, "empty");
    }

    @Override
    public boolean isMatch(List<ItemStack> pedestalItems, ItemStack reagent, RitualBaseBlockEntity ritualCenterBlockEntity, @Nullable Player player) {
        pedestalItems = pedestalItems.stream().filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toList());
        return doesReagentMatch(reagent) && this.pedestalItems.size() == pedestalItems.size() && doItemsMatch(pedestalItems, this.pedestalItems);
    }

    public boolean doesReagentMatch(ItemStack reag) {
        return this.reagent.test(reag);
    }

    @Override
    public ItemStack getResult(List<ItemStack> pedestalItems, ItemStack reagent, RitualBaseBlockEntity ritualCenterBlockEntity) {
        ItemStack stack = this.result.copy();
        if (keepNbtOfReagent && reagent.hasTag()) {

            stack.setTag(reagent.getTag());
            stack.setDamageValue(0);

        }
        return stack;
    }

    @Override
    public HashMap<String, Integer> getAdditionalAttributes() {
        return this.additionalAttributes;
    }


    // Function to check if both arrays are same
    public static boolean doItemsMatch(List<ItemStack> inputs, List<Ingredient> recipeItems) {
        StackedContents recipeitemhelper = new StackedContents();
        for (ItemStack i : inputs)
            recipeitemhelper.accountStack(i, 1);

        return inputs.size() == recipeItems.size() && (net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, recipeItems) != null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotanicRitualRecipe that = (BotanicRitualRecipe) o;
        return Objects.equals(reagent, that.reagent) &&
                Objects.equals(pedestalItems, that.pedestalItems);
    }


    @Override
    public int hashCode() {
        return Objects.hash(reagent, pedestalItems);
    }

    @Override
    public String toString() {
        return "BotanicRitualRecipe{" +
                "reagent=" + reagent +
                ", result=" + result +
                ", pedestalItems=" + pedestalItems +
                '}';
    }

    @Override
    public boolean consumesMana() {
        return getManaCost() > 0;
    }

    @Override
    public int getManaCost() {
        return manaCost;
    }


    @Override
    public boolean matches(RitualCenterBlockEntity tile, Level worldIn) {
        return isMatch(tile.getPedestalItems(), tile.heldStack, tile, null);
    }

    public boolean matches(RitualCenterBlockEntity tile, Level worldIn, @Nullable Player playerEntity) {
        return isMatch(tile.getPedestalItems(), tile.heldStack, tile, playerEntity);
    }

    @Override
    public ItemStack assemble(RitualCenterBlockEntity inv) {
        return this.result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
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
        return Registry.RECIPE_TYPE.get(new ResourceLocation(BotanicPledge.MOD_ID, RECIPE_ID));
    }

    public static class Type implements RecipeType<BotanicRitualRecipe> {
        private Type() {
        }

        public static final BotanicRitualRecipe.Type INSTANCE = new BotanicRitualRecipe.Type();
        public static final String ID = "botanic_ritual";
    }


    public static class Serializer implements RecipeSerializer<BotanicRitualRecipe> {
        public static final BotanicRitualRecipe.Serializer INSTANCE = new BotanicRitualRecipe.Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(BotanicPledge.MOD_ID, "botanic_ritual");


        @Override
        public BotanicRitualRecipe fromJson(ResourceLocation recipeId, JsonObject json) {


            //Center Piece
            Ingredient reagent = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "reagent"));

            //output item
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            //Mana Cost
            int cost = json.has("manaCost") ? GsonHelper.getAsInt(json, "manaCost") : 0;


            //Pedestal Items
            JsonArray pedestalItems = GsonHelper.getAsJsonArray(json, "pedestalItems");
            NonNullList<Ingredient> stacks = NonNullList.withSize(pedestalItems.size(), Ingredient.EMPTY);

            for (int i = 0; i < pedestalItems.size(); i++) {
                stacks.set(i, Ingredient.fromJson(pedestalItems.get(i)));


            }

            return new BotanicRitualRecipe(recipeId, stacks, reagent, output, cost);
        }

        @Nullable
        @Override
        public BotanicRitualRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int length = buffer.readInt();
            Ingredient reagent = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();
            List<Ingredient> stacks = new ArrayList<>();

            for (int i = 0; i < length; i++) {
                try {
                    stacks.add(Ingredient.fromNetwork(buffer));
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            int cost = buffer.readInt();
            boolean keepNbtOfReagent = buffer.readBoolean();
            return new BotanicRitualRecipe(recipeId, stacks, reagent, output, cost, keepNbtOfReagent);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, BotanicRitualRecipe recipe) {
            buf.writeInt(recipe.pedestalItems.size());
            recipe.reagent.toNetwork(buf);

            buf.writeItem(recipe.result);

            for (Ingredient i : recipe.pedestalItems) {
                i.toNetwork(buf);
            }
            buf.writeInt(recipe.manaCost);
            buf.writeBoolean(recipe.keepNbtOfReagent);
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
            return BotanicRitualRecipe.Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>) cls;
        }
    }


}
