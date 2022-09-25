package yerova.botanicpledge.common.items.protectors;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;

public class GaiaProtector extends Item implements ICurioItem {
    public GaiaProtector(Properties properties) {
        super(properties);
    }

    private static final int maxShield = 200;
    private static final int defRegenPerTick = 30;
    private static final int maxCharge = 500_000;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        AttributedItemsUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack, maxShield, defRegenPerTick, maxCharge);
    }


}
