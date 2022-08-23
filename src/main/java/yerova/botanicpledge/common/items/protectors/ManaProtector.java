package yerova.botanicpledge.common.items.protectors;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import yerova.botanicpledge.common.utils.ProtectorUtils;


public class ManaProtector extends Item implements ICurioItem  {
    public ManaProtector(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        ProtectorUtils.handleProtectorTick(slotContext.entity(), stack, 10, 1, 125000);
    }
}
