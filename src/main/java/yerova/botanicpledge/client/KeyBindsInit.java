package yerova.botanicpledge.client;


import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yerova.botanicpledge.setup.BotanicPledge;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class KeyBindsInit {

    //Change Mode of YggdRamus
    public static KeyMapping yggdRamusSwitchMode;


    public static void register(final FMLClientSetupEvent event) {

        yggdRamusSwitchMode = create("yggd_ramus_switch_mode", KeyEvent.VK_V);
        ClientRegistry.registerKeyBinding(yggdRamusSwitchMode);

    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + BotanicPledge.MOD_ID + "." + name, key, "key.category." + BotanicPledge.MOD_ID);
    }

}
