package yerova.botanicpledge.common.items.protectors;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;


public class ManaProtector extends Item implements ICurioItem {

    private static final int maxShield = 10;
    private static final int defRegenPerTick = 1;
    private static final int maxCharge = 125_000;

    public ManaProtector(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        AttributedItemsUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack, maxShield, defRegenPerTick, maxCharge);
    }


}
