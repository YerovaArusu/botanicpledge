package yerova.botanicpledge.client.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class ManaSweepParticle extends TextureSheetParticle {
    public float colorR = 0;
    public float colorG = 0;
    public float colorB = 0;
    public float initScale = 0;
    public float initAlpha = 0;

    private final SpriteSet sprites;

    public boolean disableDepthTest;

    public ManaSweepParticle(ClientLevel worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime, SpriteSet sprite, boolean disableDepthTest) {
        super(worldIn, x, y, z, 0, 0, 0);
        this.hasPhysics = false;

        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        if (this.colorR > 1.0) {
            this.colorR = this.colorR / 255.0f;
        }
        if (this.colorG > 1.0) {
            this.colorG = this.colorG / 255.0f;
        }
        if (this.colorB > 1.0) {
            this.colorB = this.colorB / 255.0f;
        }
        this.setColor(colorR, colorG, colorB);
        this.sprites = sprite;
        this.lifetime = 4;
        float f = this.random.nextFloat() * 0.6F + 0.4F;
        this.quadSize = 1.0F - (float) scale * 0.5F;
        this.setSpriteFromAge(sprite);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }


    @Override
    public int getLightColor(float pTicks) {
        return 15728880;
    }


    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public boolean isAlive() {
        return this.age < this.lifetime;
    }

}
