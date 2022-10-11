package yerova.botanicpledge.common.events;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.client.particle.ParticleInit;
import yerova.botanicpledge.client.particle.custom.YggdralParticles;
import yerova.botanicpledge.setup.BotanicPledge;
import yerova.botanicpledge.common.entitites.EntityInit;
import yerova.botanicpledge.common.entitites.marina_boss.MarinaEntity;
import yerova.botanicpledge.common.recipes.CoreAltarRecipe;
import yerova.botanicpledge.common.recipes.ritual.BotanicRitualRecipe;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(EntityInit.MARINA.get(), MarinaEntity.setAttributes());
    }

    @SubscribeEvent
    public static void registerModifierSerializers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) { //@Nonnull nicht vergessen später wieder zu adden
        event.getRegistry().registerAll();
    }

    @SubscribeEvent
    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, CoreAltarRecipe.Type.ID, CoreAltarRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, BotanicRitualRecipe.Type.ID, BotanicRitualRecipe.Type.INSTANCE);

    }

    @SubscribeEvent
    public static void registerParticleFactories(final ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleInit.YGGDRAL_PARTICLES.get(), YggdralParticles.Provider::new);

    }



    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
    }

}
