package yerova.botanicpledge.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import yerova.botanicpledge.client.particle.ColorParticleTypeData;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.setup.BPParticles;

public class ManaSweepParticleData implements ParticleProvider<ColorParticleTypeData> {

    private final SpriteSet spriteSet;
    public static final String NAME = "mana_sweep_particles";

    public ManaSweepParticleData(SpriteSet sprite) {
        this.spriteSet = sprite;
    }

    @Override
    public Particle createParticle(ColorParticleTypeData data, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new ManaSweepParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, data.color.getRed(), data.color.getGreen(), data.color.getBlue(), data.alpha, data.size, data.age, this.spriteSet, data.disableDepthTest);
    }

    public static ParticleOptions createData(ParticleColor color) {
        return new ColorParticleTypeData(BPParticles.MANA_SWEEP_TYPE.get(), color, false);
    }

    public static ParticleOptions createData(ParticleColor color, boolean disableDepthTest, float size) {
        return new ColorParticleTypeData(BPParticles.MANA_SWEEP_TYPE.get(), color, disableDepthTest, size, 0.75f, 36);
    }


}
