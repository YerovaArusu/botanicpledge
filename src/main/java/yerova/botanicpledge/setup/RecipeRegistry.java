package yerova.botanicpledge.setup;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.recipes.ritual.BotanicRitualRecipe;

public class RecipeRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BotanicPledge.MOD_ID);


    public static final RegistryObject<RecipeSerializer<BotanicRitualRecipe>> BOTANIC_RITUAL_RECIPE_SERIALIZER
            = SERIALIZERS.register("botanic_ritual", () -> BotanicRitualRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }

}
