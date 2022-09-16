package yerova.botanicpledge.common.items.relic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.utils.ProtectorUtils;

public class MarinasCore extends DivineCoreItem {

    private static final int maxDefense = 3400;
    private static final int defRegenPerTick = 35;
    private static final int maxCharge = 1_000_000;
    public MarinasCore(Item.Properties properties) {
        super(properties);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        ProtectorUtils.handleProtectorTick(slotContext.entity(), stack, maxDefense, defRegenPerTick, maxCharge);
    }

}
