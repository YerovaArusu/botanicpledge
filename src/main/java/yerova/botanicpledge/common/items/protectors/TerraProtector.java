package yerova.botanicpledge.common.items.protectors;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import yerova.botanicpledge.common.utils.ProtectorUtils;


@Mod.EventBusSubscriber
public class TerraProtector extends Item implements ICurioItem{
    public TerraProtector(Item.Properties unique) {
        super(unique);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        ProtectorUtils.handleProtectorTick(slotContext.entity(), stack, 100, 10, 250000);
    }
}
