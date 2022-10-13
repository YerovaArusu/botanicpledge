package yerova.botanicpledge.setup;


import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import yerova.botanicpledge.client.particle.ColorParticleTypeData;
import yerova.botanicpledge.client.particle.custom.YggdralParticleData;
import yerova.botanicpledge.client.particle.custom.YggdralParticlesType;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleSetup {
    @ObjectHolder(BotanicPledge.MOD_ID + ":" + YggdralParticleData.NAME)
    public static ParticleType<ColorParticleTypeData> YGGDRAL_TYPE;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
        IForgeRegistry<ParticleType<?>> r = event.getRegistry();

        r.register(new YggdralParticlesType().setRegistryName(YggdralParticleData.NAME));


    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent evt) {
        Minecraft.getInstance().particleEngine.register(YGGDRAL_TYPE, YggdralParticleData::new);


    }


}
