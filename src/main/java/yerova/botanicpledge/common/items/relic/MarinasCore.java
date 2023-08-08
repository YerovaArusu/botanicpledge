package yerova.botanicpledge.common.items.relic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.utils.AttributedItemsUtils;
import yerova.botanicpledge.common.utils.BPConstants;

public class MarinasCore extends DivineCoreItem {

    private static final int maxShield = 340;
    private static final int defRegenPerTick = 3;
    private static final int maxCharge = 1_000_000;
    private static final int manaCost = 100;

    public MarinasCore(Item.Properties properties) {
        super(properties);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        AttributedItemsUtils.handleShieldRegenOnCurioTick(slotContext.entity(), stack);
    }

    public static CoreAttributeProvider getCoreAttribute() {
        return new CoreAttributeProvider(maxCharge, maxShield, defRegenPerTick,manaCost);
    }

}
