package yerova.botanicpledge.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import yerova.botanicpledge.client.particle.ColorParticleTypeData;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.setup.ParticleSetup;

public class YggdralParticleData implements ParticleProvider<ColorParticleTypeData> {
    private final SpriteSet spriteSet;
    public static final String NAME = "yggdral_particles";

    public YggdralParticleData(SpriteSet sprite) {
        this.spriteSet = sprite;
    }

    @Override
    public Particle createParticle(ColorParticleTypeData data, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new YggdralParticles(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, data.color.getRed(), data.color.getGreen(), data.color.getBlue(), data.alpha, data.size, data.age, this.spriteSet, data.disableDepthTest);
    }

    public static ParticleOptions createData(ParticleColor color) {
        return new ColorParticleTypeData(ParticleSetup.YGGDRAL_TYPE, color, false);
    }

    public static ParticleOptions createData(ParticleColor color, boolean disableDepthTest) {
        return new ColorParticleTypeData(ParticleSetup.YGGDRAL_TYPE, color, disableDepthTest, 0.25f, 0.75f, 36);
    }

    public static ParticleOptions createData(ParticleColor color, boolean disableDepthTest, float size, float alpha, int age) {
        return new ColorParticleTypeData(color, disableDepthTest, size, alpha, age);
    }

    public static ParticleOptions createData(ParticleColor color, float size, float alpha, int age) {
        return new ColorParticleTypeData(color, false, size, alpha, age);
    }
}
