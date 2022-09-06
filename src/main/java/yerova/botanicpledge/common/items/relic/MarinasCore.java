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

public class MarinasCore extends ItemRelic implements ICurioItem {

    private static final int maxDefense = 3400;
    private static final int defRegenPerTick = 35;
    private static final int maxCharge = 1_000_000;
    public MarinasCore(Item.Properties properties) {
        super(properties);
    }

    public static IRelic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ICurioItem.super.curioTick(slotContext, stack);
        ProtectorUtils.handleProtectorTick(slotContext.entity(), stack, maxDefense, defRegenPerTick, maxCharge);
    }
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        CompoundTag shield = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".shield");

        shield.putInt("Defense", 0);
        shield.putInt("Charge", 0);

        shield.putInt("MaxDefense", maxDefense);
        shield.putInt("MaxCharge", maxCharge);


        if (nbt!=null) {
            nbt.merge(shield);
        } else {
            nbt = shield;
        }

        return super.initCapabilities(stack, nbt);
    }
}
