package yerova.botanicpledge.common.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.recipes.ritual.BotanicRitualRecipe;

public class RecipesInit {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BotanicPledge.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CoreAltarRecipe>> CORE_ALTAR_SERIALIZER = SERIALIZERS.register("core_altering",() -> CoreAltarRecipe.Serializer.INSTANCE);


    public static final RegistryObject<RecipeSerializer<BotanicRitualRecipe>> RITUAL_SERIALIZER = SERIALIZERS.register("botanic_ritual", () -> BotanicRitualRecipe.Serializer.INSTANCE);
}
