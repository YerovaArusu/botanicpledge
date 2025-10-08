package yerova.botanicpledge.client.events;


import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import yerova.botanicpledge.client.KeyBindings;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.items.relic.FirstRelic;
import yerova.botanicpledge.common.items.relic.NineRealmGlove;
import yerova.botanicpledge.common.network.ItemButtonInteractionToServer;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.SyncSelectedEssenceIndexPacket;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {



    @SubscribeEvent
    public static void onKeyPresses(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        onInput(mc, event.getKey(), event.getAction());
    }


    @SubscribeEvent
    public static void onMouseClicked(InputEvent.MouseButton event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        onInput(mc, event.getButton(), event.getAction());
    }

    private static void onInput(Minecraft mc, int key, int action) {
        if (mc.screen == null) {
            boolean isCTRLPressed = GLFW.glfwGetKey(mc.getWindow().getWindow(),GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS;
            if (KeyBindings.INSTANCE.ABILITY_BUTTON.isDown() && mc.player != null) {
                Networking.sendToServer(new ItemButtonInteractionToServer(isCTRLPressed));
            }
        }
    }

    @SubscribeEvent
    public static void onScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        boolean isCtrlPressed = GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS;
        if (!isCtrlPressed) return;

        ItemStack stack = mc.player.getMainHandItem();
        if (!(stack.getItem() instanceof NineRealmGlove)) {
            stack = mc.player.getOffhandItem();
        }

        if (!(stack.getItem() instanceof NineRealmGlove)) {
            return;
        }


        double delta = event.getScrollDelta();
        if (delta != 0) {

            int newIndex = NineRealmGlove.cycleSelected(stack,delta>0);

            if (newIndex >= 0) {
                Networking.sendToServer(new SyncSelectedEssenceIndexPacket(newIndex));
            }


            event.setCanceled(true);

            Essence selected = NineRealmGlove.getSelectedEssence(stack);

            if (selected != null) {
                mc.player.displayClientMessage(Component.literal("Selected: " + selected.itemBase().getDefaultInstance().getDisplayName().getString()), true);
            }
        }
    }




    @SubscribeEvent
    public static void onToolTipRender(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();

        if (!(stack.getItem() instanceof FirstRelic) || FirstRelic.getRelics().resolve().orElse(new ArrayList<>()).stream().anyMatch(it -> stack.is(it.getItem()))) {
            if (stack.getTag() != null && stack.getTag().contains(BotanicPledge.MOD_ID + ".relic_items")) {
                event.getToolTip().add(1, Component.translatable("item.botanicpledge.first_relic.ability_desc"));
            }
        }


    }

}
