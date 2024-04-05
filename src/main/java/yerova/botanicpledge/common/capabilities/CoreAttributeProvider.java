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

public class CoreAttributeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<CoreAttribute> CORE_ATTRIBUTE = CapabilityManager.get(new CapabilityToken<>() {
    });


    private final int maxCharge;
    private final int maxShield;
    private final int defRegen;
    private final int manaCost;
    private final int maxRunes;
    private final Attribute.Rune.EquipmentType requiredType;

    public CoreAttributeProvider(int maxCharge, int maxShield, int defRegen, int manaCost, int maxRunes, Attribute.Rune.EquipmentType requiredType) {
        this.maxCharge = maxCharge;
        this.maxShield = maxShield;
        this.defRegen = defRegen;
        this.manaCost = manaCost;
        this.maxRunes = maxRunes;
        this.requiredType = requiredType;
    }

    private CoreAttribute coreAttribute = null;
    private final LazyOptional<CoreAttribute> optional = LazyOptional.of(this::createCoreAttribute);

    private @NotNull CoreAttribute createCoreAttribute() {
        if (this.coreAttribute == null) {
            this.coreAttribute = new CoreAttribute(maxCharge, maxShield, defRegen, manaCost, maxRunes, requiredType);
        }
        return this.coreAttribute;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CORE_ATTRIBUTE) return optional.cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createCoreAttribute().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createCoreAttribute().loadNBTData(nbt);
    }
}
