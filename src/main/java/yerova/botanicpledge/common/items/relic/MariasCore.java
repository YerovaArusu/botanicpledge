package yerova.botanicpledge.common.items.relic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;


public class MariasCore extends DivineCoreItem{

    private final int maxShield = 4000;
    private final int defRegenPerTick = 40;
    private final int maxCharge = 1_000_000;


    public MariasCore(Properties properties) {
        super(properties);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        AttributedItemsUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack, maxShield, defRegenPerTick, maxCharge);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if(stack.getTag() == null || !(stack.getTag().contains(BPConstants.STATS_TAG_NAME))){
            stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).merge(BPConstants.INIT_CORE_TAG(maxCharge, maxShield));
        }
        return super.initCapabilities(stack, nbt);
    }

}
