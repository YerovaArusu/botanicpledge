package yerova.botanicpledge.client.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.common.network.LeftClick;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.utils.LeftClickable;
import yerova.botanicpledge.setup.BotanicPledge;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerInputEvents {

    @SubscribeEvent
    public static void PlayerLeftClickEvent(PlayerInteractEvent.LeftClickEmpty evt) {
        if (evt.getItemStack().getItem() instanceof LeftClickable) {
            Networking.sendToServer(new LeftClick(evt.getPlayer().getMainHandItem()));
        }
    }

}
