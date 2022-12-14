package yerova.botanicpledge.setup;


import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.client.particle.ColorParticleTypeData;
import yerova.botanicpledge.client.particle.custom.ManaSweepParticleData;
import yerova.botanicpledge.client.particle.custom.ManaSweepParticleType;
import yerova.botanicpledge.client.particle.custom.YggdralParticleData;
import yerova.botanicpledge.client.particle.custom.YggdralParticleType;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleSetup {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, BotanicPledge.MOD_ID);

    public static final RegistryObject<ParticleType<ColorParticleTypeData>> YGGDRAL_TYPE = PARTICLES.register(YggdralParticleData.NAME, YggdralParticleType::new);
    public static final RegistryObject<ParticleType<ColorParticleTypeData>> MANA_SWEEP_TYPE = PARTICLES.register(ManaSweepParticleData.NAME, ManaSweepParticleType::new);


    @SubscribeEvent
    public static void RegisterFactories(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(YGGDRAL_TYPE.get(), YggdralParticleData::new);
        Minecraft.getInstance().particleEngine.register(MANA_SWEEP_TYPE.get(), ManaSweepParticleData::new);
    }
}
