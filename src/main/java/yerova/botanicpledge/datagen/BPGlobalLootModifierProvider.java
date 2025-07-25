package yerova.botanicpledge.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import yerova.botanicpledge.loot.AddGaiaItemModifier;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

public class BPGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public BPGlobalLootModifierProvider(PackOutput output) {
        super(output, BotanicPledge.MOD_ID);
    }

    @Override
    protected void start() {
        add("yggdrasilsteel_template_from_gaia_2",  new AddGaiaItemModifier(new LootItemCondition[]{}, BPItems.YGGDRASILSTEEL_INGOT.get()));
    }
}
