package yerova.botanicpledge.client.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.setup.BotanicPledge;

public class ParticleInit {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, BotanicPledge.MOD_ID);

    public static final RegistryObject<SimpleParticleType> YGGDRAL_PARTICLES = PARTICLE_TYPES.register("yggdral_particle", () -> new SimpleParticleType(true));
}
