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

public class BPAttributeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<BPAttribute> ATTRIBUTE = CapabilityManager.get(new CapabilityToken<>() {
    });


    public BPAttributeProvider() {

    }

    private BPAttribute bPAttribute = null;
    private final LazyOptional<BPAttribute> optional = LazyOptional.of(this::createBPAttribute);

    private @NotNull BPAttribute createBPAttribute() {
        if (this.bPAttribute == null) {
            this.bPAttribute = new BPAttribute();
        }
        return this.bPAttribute;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ATTRIBUTE) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createBPAttribute().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createBPAttribute().loadNBTData(nbt);
    }
}
