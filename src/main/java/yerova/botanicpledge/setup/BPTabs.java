package yerova.botanicpledge.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.items.BotanicPledgeTab;

public class BPTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BotanicPledge.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TABS.register("botanic_ritual", () -> new BotanicPledgeTab("botanicpledge"));
}
