package yerova.botanicpledge.client.particle.custom;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import vazkii.botania.client.fx.FXWisp;
import vazkii.botania.client.fx.WispParticleData;
import yerova.botanicpledge.client.particle.ColorParticleTypeData;

public class ManaSweepParticleType extends ParticleType<ColorParticleTypeData> {
    public ManaSweepParticleType() {
        super(false, ColorParticleTypeData.DESERIALIZER);
    }

    @Override
    public Codec<ColorParticleTypeData> codec() {
        return ColorParticleTypeData.CODEC;
    }
}
