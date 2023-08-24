package yerova.botanicpledge.common.events;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.forge.CapabilityUtil;
import yerova.botanicpledge.common.capabilities.BPAttribute;
import yerova.botanicpledge.common.capabilities.BPAttributeProvider;
import yerova.botanicpledge.common.capabilities.CoreAttribute;
import yerova.botanicpledge.common.items.relic.*;
import yerova.botanicpledge.setup.BotanicPledge;
import yerova.botanicpledge.setup.BPItems;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID)
public class ForgeCommonInitializer {

    private static final Supplier<Map<Item, Function<ItemStack, IRelic>>> RELIC = Suppliers.memoize(() -> Map.of(
            BPItems.MARIAS_CORE.get(), DivineCoreItem::makeRelic,
            BPItems.MARINAS_CORE.get(), DivineCoreItem::makeRelic,
            BPItems.YGGD_RAMUS.get(), YggdRamus::makeRelic,
            BPItems.ASGARD_FRACTAL.get(), AsgardFractal::makeRelic
    ));



    private static final Supplier<Map<Item, Function<ItemStack, IManaItem>>> MANA_ITEM = Suppliers.memoize(() -> Map.of(
            BPItems.MARIAS_CORE.get(), DivineCoreItem.ManaItem::new,
            BPItems.MARINAS_CORE.get(), DivineCoreItem.ManaItem::new
    ));


    public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> e) {

        ItemStack stack = e.getObject();
        var makeRelic = RELIC.get().get(stack.getItem());
        if (makeRelic != null) {
            e.addCapability(prefix("relic"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.RELIC, makeRelic.apply(stack)));
        }

        var makeManaItem = MANA_ITEM.get().get(stack.getItem());
        if (makeManaItem != null) {
            e.addCapability(prefix("mana_item"),
                    CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_ITEM, makeManaItem.apply(stack)));
        }

    }

    @SubscribeEvent
    public static void addItemCaps(AttachCapabilitiesEvent<ItemStack> e) {


        ItemStack stack = e.getObject();

        if (stack.getItem() instanceof MariasCore)
            e.addCapability(new ResourceLocation(BotanicPledge.MOD_ID, "attributes"), MariasCore.getCoreAttribute());
        if (stack.getItem() instanceof MarinasCore)
            e.addCapability(new ResourceLocation(BotanicPledge.MOD_ID, "attributes"), MarinasCore.getCoreAttribute());
        if(stack.getItem() instanceof AsgardFractal){
            e.addCapability(new ResourceLocation(BotanicPledge.MOD_ID, "attributes"), new BPAttributeProvider());
        }

    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(CoreAttribute.class);
        event.register(BPAttribute.class);
    }

}
