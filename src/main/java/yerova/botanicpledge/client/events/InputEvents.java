package yerova.botanicpledge.client.events;


import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yerova.botanicpledge.client.KeyBindsInit;
import yerova.botanicpledge.common.items.relic.YggdRamus;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.YggRamusSwitchSkillToServer;
import yerova.botanicpledge.setup.BotanicPledge;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {


    @SubscribeEvent
    public static void onKeyPresses(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        onInput(mc, event.getKey(), event.getAction());
    }


    @SubscribeEvent
    public static void onMouseClicked(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        onInput(mc, event.getButton(), event.getAction());
    }

    private static void onInput(Minecraft mc, int key, int action) {
        if (mc.screen == null) {
            if (KeyBindsInit.yggdRamusSwitchMode.isDown() && mc.player != null && mc.player.getMainHandItem().getItem() instanceof YggdRamus) {
                Networking.sendToServer(new YggRamusSwitchSkillToServer());
            }
        }

    }

}
