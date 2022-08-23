package yerova.botanicpledge.client.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.client.core.handler.ClientTickHandler;
import yerova.botanicpledge.BotanicPledge;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ProtectorUtilsClient {

    @SubscribeEvent
    public static void onToolTipRender(RenderTooltipEvent.Pre evt) {



        if (evt.getItemStack().isEmpty()) return;


        ItemStack stack = evt.getItemStack();
        int width = evt.getScreenWidth()/10;
        int height = 4;
        int tooltipX = evt.getX() + 10;
        int tooltipY = evt.getY() - 15;


        if (stack.hasTag()) {
            CompoundTag shield = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".shield");
            if (shield.contains("Charge")) {
                drawBar(evt.getPoseStack(), shield.getInt("Charge"), shield.getInt("MaxCharge"),
                        tooltipX, tooltipY, width, height, 0.52F);
            }


            if (shield.contains("Defense")) {
                drawBar(evt.getPoseStack(), shield.getInt("Defense"), shield.getInt("MaxDefense"),
                        tooltipX, tooltipY - height, width, height, 0.754F);
            }
        }


    }

    private static void drawBar(PoseStack ps, int value, int maxValue, int mouseX, int mouseY, int width, int height, float hue) {
        int BarWidth = (int) Math.ceil(width * (((1 / (double) maxValue) * (double) value)));

        RenderSystem.disableDepthTest();
        Gui.fill(ps, mouseX - 1, mouseY - height - 1, mouseX + width + 1, mouseY, 0xFF000000);
        Gui.fill(ps, mouseX, mouseY - height, mouseX + BarWidth, mouseY, 0xFF000000 | Mth.hsvToRgb(hue, ((float) Math.sin((ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) * 0.2) + 1F) * 0.3F + 0.4F, 1F));
        Gui.fill(ps, mouseX + BarWidth, mouseY - height, mouseX + width, mouseY, 0xFF555555);
    }
}
