package yerova.botanicpledge.common.items.relic;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.common.utils.ProtectorUtils;

public class MarinasCore extends ItemRelic implements ICurioItem {
    public MarinasCore(Item.Properties properties) {
        super(properties);
    }

    public static IRelic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        ProtectorUtils.handleProtectorTick(slotContext.entity(), stack, 3500, 35, 1000000);
    }
}
