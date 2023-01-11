package yerova.botanicpledge.client.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.client.render.screen.ProtectorHUD;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.setup.BotanicPledge;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ProtectorEventsClient {

    @SubscribeEvent
    public static void onToolTipRender(RenderTooltipEvent.Pre evt) {


        if (evt.getItemStack().isEmpty()) return;


        ItemStack stack = evt.getItemStack();
        int width = 102;
        int height = 5;
        int tooltipX = evt.getX() + 10;
        int tooltipY = evt.getY() - 15;


        if (stack.hasTag() && stack.getTag().contains(BPConstants.STATS_TAG_NAME)) {
            CompoundTag shield = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
            if (shield.contains(BPConstants.CHARGE_TAG_NAME)) {
                ProtectorHUD.drawBar(evt.getPoseStack(), shield.getInt(BPConstants.CHARGE_TAG_NAME), shield.getInt(BPConstants.MAX_CHARGE_TAG_NAME),
                        tooltipX, tooltipY, width, height, 1);
            }


            if (shield.contains(BPConstants.SHIELD_TAG_NAME)) {
                ProtectorHUD.drawBar(evt.getPoseStack(), shield.getInt(BPConstants.SHIELD_TAG_NAME), shield.getInt(BPConstants.MAX_SHIELD_TAG_NAME),
                        tooltipX, tooltipY - height, width, height, 2);
            }
        }
    }

}
