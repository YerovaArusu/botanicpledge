package yerova.botanicpledge.config;

import com.userofbricks.expanded_combat.config.ConfigName;
import com.userofbricks.expanded_combat.config.MaterialConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.resources.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.xplat.XplatAbstractions;
import yerova.botanicpledge.setup.BotanicPledge;

@Config(name= BotanicPledge.MOD_ID)
public class BPConfig implements ConfigData {

    @ConfigEntry.Category("Materials") @ConfigEntry.Gui.CollapsibleObject @ConfigName("Yggdrasilsteel Settings")
    public MaterialConfig yggdrasilsteel = new MaterialConfig.Builder()
            .craftingItem(new ResourceLocation(BotanicPledge.MOD_ID, "yggdrasilsteel_ingot"))
            .toolDurability(8192).gauntletArmorAmount(8)
            .gauntletAttackDamage(16f).armorToughness(2.5f).addedShieldDurability(1024)
            .baseProtectionAmmount(8).afterBasePercentReduction(0.8f)
            .repairItem(new ResourceLocation(BotanicPledge.MOD_ID, "yggdrasilsteel_ingot"))
            .defenseEnchantability(20).offenseEnchantability(20)
            .build();

}
