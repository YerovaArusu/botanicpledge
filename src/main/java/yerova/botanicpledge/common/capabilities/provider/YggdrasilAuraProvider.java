package yerova.botanicpledge.common.capabilities.provider;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.capabilities.YggdrasilAura;

public class YggdrasilAuraProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<YggdrasilAura> ESSENCE = CapabilityManager.get(new CapabilityToken<>() {
    });

    private YggdrasilAura auraChunk;
    private LevelChunk chunk;
    private final LazyOptional<YggdrasilAura> optional = LazyOptional.of(this::createEssence);

    public YggdrasilAuraProvider(LevelChunk chunk) {
        this.chunk = chunk;
    }

    private @NotNull YggdrasilAura createEssence() {
        if (this.auraChunk == null) {
            this.auraChunk = new YggdrasilAura(chunk);
        }
        return this.auraChunk;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ESSENCE) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createEssence().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createEssence().loadNBTData(nbt);

    }
}
