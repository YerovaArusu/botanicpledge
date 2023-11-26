package yerova.botanicpledge.setup;


import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import yerova.botanicpledge.client.particle.ColorParticleTypeData;
import yerova.botanicpledge.client.particle.custom.*;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BPParticels {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, BotanicPledge.MOD_ID);

    public static final RegistryObject<ParticleType<ColorParticleTypeData>> YGGDRAL_TYPE = PARTICLES.register(YggdralParticleData.NAME, YggdralParticlesType::new);
    public static final RegistryObject<ParticleType<ColorParticleTypeData>> MANA_SWEEP_TYPE = PARTICLES.register(ManaSweepParticleData.NAME, ManaSweepParticleType::new);




}
