package yerova.botanicpledge.common.items.relic;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.utils.BPItemUtils;


public class MariasCore extends DivineCoreItem {

    private static final int maxShield = 600;
    private static final int defRegenPerTick = 4;
    private static final int maxCharge = 2_000_000;
    private static final int manaCost = 100;


    public MariasCore(Item.Properties properties) {
        super(properties);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        BPItemUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack);
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
