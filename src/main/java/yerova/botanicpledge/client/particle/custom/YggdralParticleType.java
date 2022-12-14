package yerova.botanicpledge.client.particle.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import yerova.botanicpledge.client.particle.ColorParticleTypeData;

public class YggdralParticleType extends ParticleType<ColorParticleTypeData> {
    public YggdralParticleType() {
        super(false, ColorParticleTypeData.DESERIALIZER);
    }

    @Override
    public Codec<ColorParticleTypeData> codec() {
        return ColorParticleTypeData.CODEC;
    }
}
