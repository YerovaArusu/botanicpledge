package yerova.botanicpledge.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.recipes.botanic_ritual.BotanicRitualRecipe;
import yerova.botanicpledge.common.recipes.ore_infusion.OreInfusionRecipe;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BPRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, BotanicPledge.MOD_ID);


    public static final RegistryObject<RecipeSerializer<BotanicRitualRecipe>> BOTANIC_RITUAL_RECIPE =
            SERIALIZERS.register("botanic_ritual", () -> BotanicRitualRecipe.Serializer.INSTANCE);


    public static final RegistryObject<RecipeSerializer<OreInfusionRecipe>> ORE_INFUSION_RECIPE =
            SERIALIZERS.register("ore_infusion", () -> OreInfusionRecipe.Serializer.INSTANCE);

}
