package yerova.botanicpledge.client.particle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import yerova.botanicpledge.client.particle.custom.YggdralParticleData;

import java.util.concurrent.ThreadLocalRandom;

public class ParticleUtils {
    public static void spawnYggdralParticleSphere(Level world, BlockPos pos){

        for(int i =0; i< 5; i++){
            Vec3 particlePos = new Vec3(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 0, 0.5);
            particlePos = particlePos.add(ParticleUtils.pointInSphere());
            world.addParticle(YggdralParticleData.createData(ParticleUtils.defaultParticleColor()),
                    particlePos.x(), particlePos.y(), particlePos.z(),
                    pos.getX()  +0.5, pos.getY() + 1  , pos.getZ() +0.5);
        }
    }

    // https://karthikkaranth.me/blog/generating-random-points-in-a-sphere/
    public static Vec3 pointInSphere(){
        double u = Math.random();
        double v = Math.random();
        double theta = u * 2.0 * Math.PI;
        double phi = Math.acos(2.0 * v - 1.0);
        double r = Math.cbrt(Math.random());
        double sinTheta = Math.sin(theta);
        double cosTheta = Math.cos(theta);
        double sinPhi = Math.sin(phi);
        double cosPhi = Math.cos(phi);
        double x = r * sinPhi * cosTheta;
        double y = r * sinPhi * sinTheta;
        double z = r * cosPhi;
        return new Vec3(x,y,z);
    }

    public static ParticleColor defaultParticleColor(){
        return new ParticleColor(255, 25, 180);
    }

    public static ParticleColor.IntWrapper defaultParticleColorWrapper(){
        return new ParticleColor.IntWrapper(255, 25, 180);
    }

    public static double inRange(double min, double max){
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}
