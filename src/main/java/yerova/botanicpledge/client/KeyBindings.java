package yerova.botanicpledge.client;


import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yerova.botanicpledge.setup.BotanicPledge;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public final class KeyBindings {

    public static final KeyBindings INSTANCE = new KeyBindings();

    private KeyBindings() {

    }

    public final KeyMapping switchSkillButton = create("yggd_ramus_switch_mode", KeyEvent.VK_V);

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + BotanicPledge.MOD_ID + "." + name, key, "category.botanicpledge." + BotanicPledge.MOD_ID);
    }

}
