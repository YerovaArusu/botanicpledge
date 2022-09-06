package yerova.botanicpledge.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DivineCorePlayerStatsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<DivineCorePlayerStats> DC_PLAYER_STATS = CapabilityManager.get(new CapabilityToken<>() {});

    private DivineCorePlayerStats dcPlayerStats = null;
    private final LazyOptional<DivineCorePlayerStats> opt = LazyOptional.of(this::createDivineCorePlayerStats);

    private @NotNull DivineCorePlayerStats createDivineCorePlayerStats() {
        if(dcPlayerStats == null) {
            dcPlayerStats = new DivineCorePlayerStats(0, 0, 0, 0, 0, 0, 0, 0, 0);
        }
        return dcPlayerStats;
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == DC_PLAYER_STATS) {
            return  opt.cast();
        }
        return LazyOptional.empty();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {return getCapability(cap);}

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createDivineCorePlayerStats().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createDivineCorePlayerStats().loadNBTData(nbt);
    }
}

