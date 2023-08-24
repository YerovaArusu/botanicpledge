package yerova.botanicpledge.setup;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.common.recipes.ritual.BotanicRitualRecipe;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BPRecipes {
    public static final RecipeType<BotanicRitualRecipe> BOTANIC_RITUAL_TYPE = new ModRecipeType<>();

    public static final RecipeSerializer<BotanicRitualRecipe> BOTANIC_RITUAL_SERIALIZER = new BotanicRitualRecipe.Serializer();


    @SubscribeEvent
    public static void register(final RegistryEvent.Register<RecipeSerializer<?>> evt) {
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(BotanicPledge.MOD_ID, BotanicRitualRecipe.RECIPE_ID), BOTANIC_RITUAL_TYPE);

        evt.getRegistry().register(BOTANIC_RITUAL_SERIALIZER.setRegistryName(new ResourceLocation(BotanicPledge.MOD_ID, BotanicRitualRecipe.RECIPE_ID)));

    }

    private static class ModRecipeType<T extends Recipe<?>> implements RecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }

}
