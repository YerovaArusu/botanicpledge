package yerova.botanicpledge.client.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.client.render.screen.ProtectorHUD;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ProtectorEventsClient {

    @SubscribeEvent
    public static void onToolTipRender(RenderTooltipEvent.Pre evt) {


        if (evt.getItemStack().isEmpty()) return;


        ItemStack stack = evt.getItemStack();
        int width = evt.getScreenWidth() / 10;
        int height = 4;
        int tooltipX = evt.getX() + 10;
        int tooltipY = evt.getY() - 15;


        if (stack.hasTag()) {
            CompoundTag shield = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats");
            if (shield.contains("Charge")) {
                ProtectorHUD.drawBar(evt.getPoseStack(), shield.getInt("Charge"), shield.getInt("MaxCharge"),
                        tooltipX, tooltipY, width, height, 0.52F);
            }


            if (shield.contains("Shield")) {
                ProtectorHUD.drawBar(evt.getPoseStack(), shield.getInt("Shield"), shield.getInt("MaxShield"),
                        tooltipX, tooltipY - height, width, height, 0.754F);
            }
        }
    }

}
