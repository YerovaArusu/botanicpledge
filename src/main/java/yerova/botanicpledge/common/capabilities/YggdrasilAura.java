package yerova.botanicpledge.common.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import yerova.botanicpledge.common.utils.SimplePerlinNoise;
import yerova.botanicpledge.setup.BotanicPledge;

public class YggdrasilAura {
    private int genPerInstance;
    private final LevelChunk chunk;

    public YggdrasilAura(LevelChunk chunk) {
        this.genPerInstance = prepareAuraValue(chunk);
        this.chunk = chunk;
    }

    public static int prepareAuraValue(LevelChunk chunk) {
        if (chunk.getLevel().isClientSide) return 0;
        if (chunk.getLevel() instanceof ServerLevel serverLevel) {
            double noiseValue = SimplePerlinNoise.noise(chunk.getPos().x * 0.1, chunk.getPos().z * 0.1);
            double normalizedNoiseValue = (noiseValue + 1.0) / 2.0;



            //float greennessFactor = calculateGreenness(biome, precipitation, chunkPos.getY());

            return (int) (normalizedNoiseValue * 20 /* * greennessFactor*/);
        }
        return 1;
    }

    private static float calculateGreenness(Biome biome, Biome.Precipitation precipitation, int altitude) {
        float greenness = 1.0f;

        float temperature = biome.getBaseTemperature();
        float rainfall = biome.getModifiedClimateSettings().downfall();

        greenness *= Math.max(0, Math.min(temperature, 1.0f));
        greenness *= Math.max(0, Math.min(rainfall, 1.0f));

        if (precipitation == Biome.Precipitation.RAIN) {
            greenness *= 1.2f;
        } else if (precipitation == Biome.Precipitation.SNOW) {
            greenness *= 0.8f;
        } else {
            greenness *= 0.9f;
        }

        float altitudeFactor = 1.0f - Math.min(altitude / 256.0f, 0.5f);
        greenness *= altitudeFactor;

        return Math.max(0.5f, Math.min(greenness, 2.0f));
    }

    public LevelChunk getChunk() {
        return chunk;
    }

    public int getGenPerInstance() {
        return genPerInstance;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt(BotanicPledge.MOD_ID + ".genPerInstance", genPerInstance);
    }

    public void loadNBTData(CompoundTag nbt) {
        genPerInstance = nbt.getInt(BotanicPledge.MOD_ID + ".genPerInstance");
    }
}
