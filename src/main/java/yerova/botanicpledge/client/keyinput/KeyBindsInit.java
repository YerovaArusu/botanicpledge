package yerova.botanicpledge.client.keyinput;


import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yerova.botanicpledge.BotanicPledge;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class KeyBindsInit {

    public static KeyMapping switchSkillForwards;
    public static KeyMapping switchSkillBackwards;

    public static void register(final FMLClientSetupEvent event) {



        switchSkillForwards = create("switch_skill_forwards", KeyEvent.VK_V);
        ClientRegistry.registerKeyBinding(switchSkillForwards);

        switchSkillBackwards = create("switch_skill_backwards", KeyEvent.VK_C);
        ClientRegistry.registerKeyBinding(switchSkillBackwards);

    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + BotanicPledge.MOD_ID + "." + name, key, "key.category." + BotanicPledge.MOD_ID);
    }
}
