package yerova.botanicpledge.common.events;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.client.render.screen.ProtectorHUD;
import yerova.botanicpledge.setup.*;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerGameOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("hotbar", ProtectorHUD.PROTECTOR_HUD);
    }

    @SubscribeEvent
    public static void addToBotanicPLedgeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == BPTabs.MAIN_TAB.getKey()) BPItems.ITEMS.getEntries().forEach(item -> event.accept(item.get()));
    }


}
