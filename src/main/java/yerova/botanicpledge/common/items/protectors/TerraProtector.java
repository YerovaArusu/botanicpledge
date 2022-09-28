package yerova.botanicpledge.common.items.protectors;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;


@Mod.EventBusSubscriber
public class TerraProtector extends Item implements ICurioItem {

    private static final int maxShield = 100;
    private static final int defRegenPerTick = 10;
    private static final int maxCharge = 250_000;

    public TerraProtector(Item.Properties unique) {
        super(unique);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        AttributedItemsUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack, maxShield, defRegenPerTick, maxCharge);
    }

}
