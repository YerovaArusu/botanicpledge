package yerova.botanicpledge.client.events;


import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.client.KeyBindings;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.ItemButtonInteractionToServer;
import yerova.botanicpledge.setup.BotanicPledge;

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
            if (KeyBindings.INSTANCE.switchSkillButton.isDown() && mc.player != null) {
                Networking.sendToServer(new ItemButtonInteractionToServer());
            }
        }

    }

}
