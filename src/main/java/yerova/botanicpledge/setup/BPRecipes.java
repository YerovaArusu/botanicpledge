package yerova.botanicpledge.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.recipes.ritual.BotanicRitualRecipe;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BPRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, BotanicPledge.MOD_ID);


    public static final RegistryObject<RecipeSerializer<BotanicRitualRecipe>> BOTANIC_RITUAL_RECIPE =
            SERIALIZERS.register("botanic_ritual", () -> BotanicRitualRecipe.Serializer.INSTANCE);

}
