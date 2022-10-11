package yerova.botanicpledge.client.events;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.mana.ManaItemHandler;
import yerova.botanicpledge.setup.BotanicPledge;
import yerova.botanicpledge.client.keyinput.KeyBindsInit;
import yerova.botanicpledge.common.items.YggdralScepter;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.YggdralScepterLeftClick;
import yerova.botanicpledge.common.network.YggdralScepterSwitchSkills;

@Mod.EventBusSubscriber(modid = BotanicPledge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class PlayerInputEvents {

    @SubscribeEvent
    public static void PlayerLeftClickEvent(PlayerInteractEvent.LeftClickEmpty evt) {
        if (evt.getPlayer().getMainHandItem().getItem() instanceof YggdralScepter) {
            if (ManaItemHandler.instance().requestManaExact(evt.getItemStack(), evt.getPlayer(), 10000, true)) {
                Networking.sendToServer(new YggdralScepterLeftClick());
            }
        }
    }

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
            if (KeyBindsInit.switchSkillForwards.isDown()) {
                Networking.sendToServer(new YggdralScepterSwitchSkills(1));
            }

            if (KeyBindsInit.switchSkillBackwards.isDown()) {
                Networking.sendToServer(new YggdralScepterSwitchSkills(0));
            }

        }
    }
}
