package yerova.botanicpledge.common.events;

import com.google.common.base.Suppliers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.forge.CapabilityUtil;
import yerova.botanicpledge.common.items.ItemInit;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ForgeCommonInitializer {

    private static final Supplier<Map<Item, Function<ItemStack, IRelic>>> RELIC = Suppliers.memoize(() -> Map.of(
            ItemInit.MARIAS_CORE.get(), DivineCoreItem::makeRelic,
            ItemInit.MARINAS_CORE.get(), DivineCoreItem::makeRelic
    ));

    public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> e) {
        var stack = e.getObject();
        var makeRelic = RELIC.get().get(stack.getItem());
        if (makeRelic != null) {
            e.addCapability(prefix("relic"),
                    CapabilityUtil.makeProvider(BotaniaForgeCapabilities.RELIC, makeRelic.apply(stack)));
        }
    }
}
