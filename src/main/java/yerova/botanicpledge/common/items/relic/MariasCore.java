package yerova.botanicpledge.common.items.relic;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.common.utils.ProtectorUtils;


public class MariasCore extends ItemRelic implements ICurioItem {
    public Multimap<Attribute, AttributeModifier> defaultModifiers;
    public MariasCore(Properties properties) {
        super(properties);
    }

    public static IRelic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ProtectorUtils.handleProtectorTick(slotContext.entity(), stack, 4000, 40, 1000000);
    }
}
