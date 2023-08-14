package yerova.botanicpledge.common.items.relic;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import vazkii.botania.common.handler.EquipmentHandler;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;


public class MariasCore extends DivineCoreItem {

    private static final int maxShield = 600;
    private static final int defRegenPerTick = 4;
    private static final int maxCharge = 2_000_000;
    private static final int manaCost = 100;


    public MariasCore(Properties properties) {
        super(properties);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        AttributedItemsUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack);
        stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {
            attribute.setMaxCharge(getShieldValueAccordingToRank(stack, maxCharge));
            attribute.setMaxShield(getShieldValueAccordingToRank(stack, maxShield));
            attribute.setDefRegenPerTick(getShieldValueAccordingToRank(stack, defRegenPerTick));
        });
    }

    public static CoreAttributeProvider getCoreAttribute() {
        return new CoreAttributeProvider(maxCharge, maxShield, defRegenPerTick, manaCost);
    }


}
