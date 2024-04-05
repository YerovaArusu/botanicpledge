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

public class AttributeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<Attribute> ATTRIBUTE = CapabilityManager.get(new CapabilityToken<>() {
    });

    private int maxRunes;
    private Attribute.Rune.EquipmentType requiredType;

    public AttributeProvider(int maxRunes, Attribute.Rune.EquipmentType requiredType) {
        this.maxRunes = maxRunes;
        this.requiredType = requiredType;
    }

    private Attribute attribute = null;
    private final LazyOptional<Attribute> optional = LazyOptional.of(this::createAttribute);

    private @NotNull Attribute createAttribute() {
        if (this.attribute == null) {
            this.attribute = new Attribute(maxRunes, requiredType);
        }
        return this.attribute;
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
        createAttribute().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createAttribute().loadNBTData(nbt);
    }
}
