package yerova.botanicpledge.common.items.protectors;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.utils.ProtectorUtils;

public class YggdralProtector extends Item implements ICurioItem{

    private static final int maxDefense = 2_000;
    private static final int defRegenPerTick = 20;
    private static final int maxCharge = 1_000_000;
    public YggdralProtector(Properties properties) {
        super(properties);
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
