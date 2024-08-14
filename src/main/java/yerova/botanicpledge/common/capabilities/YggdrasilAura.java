package yerova.botanicpledge.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
        if (chunk.getLevel() instanceof ServerLevel) {

            double noiseValue = SimplePerlinNoise.noise(chunk.getPos().x*0.1,chunk.getPos().z*0.1);

            double normalizedValue = (noiseValue + 1.0) / 2.0;

            return (int)(normalizedValue * 20);
        }
        return 1;
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
