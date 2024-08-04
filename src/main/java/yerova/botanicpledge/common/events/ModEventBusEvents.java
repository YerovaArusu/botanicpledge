package yerova.botanicpledge.common.events;

import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.item.CustomCreativeTabContents;
import yerova.botanicpledge.client.render.screen.ProtectorHUD;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BPTabs;
import yerova.botanicpledge.setup.BotanicPledge;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerGameOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("hotbar", ProtectorHUD.PROTECTOR_HUD);
    }

    @SubscribeEvent
    public static void addToBotanicPLedgeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == BPTabs.MAIN_TAB.getKey())
            BPItems.ITEMS.getEntries().forEach(item -> {

                if (item.get() instanceof CustomCreativeTabContents tab) {
                    tab.addToCreativeTab(item.get(), event);
                } else {
                    event.accept(item.get());
                }
            });
    }


}
