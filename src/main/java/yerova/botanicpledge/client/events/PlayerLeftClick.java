package yerova.botanicpledge.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.mana.ManaItemHandler;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.items.YggdralScepter;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.YggdralScepterLeftClick;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerLeftClick {

    @SubscribeEvent
    public static void PlayerLeftClickEvent(PlayerInteractEvent.LeftClickEmpty evt) {
        if (evt.getPlayer().getMainHandItem().getItem() instanceof YggdralScepter) {
            if (ManaItemHandler.instance().requestManaExact(evt.getItemStack(), evt.getPlayer(), 10000, true)) {
                Networking.sendToServer(new YggdralScepterLeftClick());
            }
        }
    }
}
