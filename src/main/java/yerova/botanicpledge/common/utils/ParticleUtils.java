package yerova.botanicpledge.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import vazkii.botania.client.fx.WispParticleData;

public class ParticleUtils {
    public static void spawnMovingParticlesAbove(ServerLevel serverLevel, BlockPos blockPos, float r, float g, float b) {
        spawnMovingParticles(serverLevel, blockPos, blockPos.above(), r, g, b);
    }

    public static void spawnMovingParticles(ServerLevel serverLevel, BlockPos startPos,BlockPos endPos, float r, float g, float b) {

        double startX = startPos.getX() + 0.5;
        double startY = startPos.getY() + 0.5;
        double startZ = startPos.getZ() + 0.5;

        double endX = endPos.getX() + 0.5;
        double endY = endPos.getY() + 0.5;
        double endZ = endPos.getZ() + 0.5;

        for (int i = 0; i < 5; i++) {
            double progress = i / 5.0;
            double particleX = startX + (endX - startX) * progress;
            double particleY = startY + (endY - startY) * progress;
            double particleZ = startZ + (endZ - startZ) * progress;

            WispParticleData data = WispParticleData.wisp(0.3F * ((float) 0.5), r, g, b, true);

            serverLevel.sendParticles(data, particleX, particleY, particleZ, 1, 0, 0, 0, 0.01F);
        }
    }
}
