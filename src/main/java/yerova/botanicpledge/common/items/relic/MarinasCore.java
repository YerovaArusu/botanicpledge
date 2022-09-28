package yerova.botanicpledge.common.items.relic;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;

public class MarinasCore extends DivineCoreItem {

    private static final int maxShield = 3400;
    private static final int defRegenPerTick = 35;
    private static final int maxCharge = 1_000_000;

    public MarinasCore(Item.Properties properties) {
        super(properties);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        AttributedItemsUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack, maxShield, defRegenPerTick, maxCharge);
    }


}
