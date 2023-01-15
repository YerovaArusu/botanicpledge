package yerova.botanicpledge.common.items.relic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;
import yerova.botanicpledge.common.utils.BPConstants;

public class MarinasCore extends DivineCoreItem {

    private static final int maxShield = 340;
    private static final int defRegenPerTick = 3;
    private static final int maxCharge = 1_000_000;

    public MarinasCore(Item.Properties properties) {
        super(properties);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        AttributedItemsUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack,
                getShieldValueAccordingToRank(stack, maxShield),
                getShieldValueAccordingToRank(stack, defRegenPerTick),
                getShieldValueAccordingToRank(stack, maxCharge));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if(stack.getTag() == null || !(stack.getTag().contains(BPConstants.STATS_TAG_NAME))){
            stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).merge(BPConstants.INIT_CORE_TAG(
                    getShieldValueAccordingToRank(stack, maxCharge),
                    getShieldValueAccordingToRank(stack, maxShield)));
        }
        return super.initCapabilities(stack, nbt);
    }

}
