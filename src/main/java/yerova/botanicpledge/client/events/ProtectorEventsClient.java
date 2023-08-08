package yerova.botanicpledge.client.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.client.render.screen.ProtectorHUD;
import yerova.botanicpledge.common.capabilities.CoreAttributeProvider;
import yerova.botanicpledge.common.items.relic.DivineCoreItem;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.setup.BotanicPledge;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ProtectorEventsClient {


    public static void onToolTipRender(RenderTooltipEvent.Pre evt) {

        if (evt.getItemStack().isEmpty() || !(evt.getItemStack().getItem() instanceof DivineCoreItem)) return;

        ItemStack stack = evt.getItemStack();
        int width = 160;
        int height = 5;
        int tooltipX = evt.getX() + 10;
        int tooltipY = evt.getY() - 15;

        stack.getCapability(CoreAttributeProvider.CORE_ATTRIBUTE).ifPresent(attribute -> {

            ProtectorHUD.drawBar(evt.getPoseStack(), attribute.getCurrentCharge(), attribute.getMaxCharge(),
                    tooltipX, tooltipY, width, height, 1);

            ProtectorHUD.drawBar(evt.getPoseStack(), attribute.getCurrentShield(), attribute.getMaxShield(),
                    tooltipX, tooltipY - height, width, height, 2);

        });
    }
}
