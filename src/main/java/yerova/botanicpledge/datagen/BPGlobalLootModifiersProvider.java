package yerova.botanicpledge.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import yerova.botanicpledge.loot.conditionn.RuneCollectorCondition;
import yerova.botanicpledge.loot.modifier.AddSpecialModifier;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.Map;

public class BPGlobalLootModifiersProvider extends GlobalLootModifierProvider {

    public BPGlobalLootModifiersProvider(PackOutput output) {
        super(output, BotanicPledge.MOD_ID);
    }

    @Override
    protected void start() {
        addRuneModifier(
                "rune_gem_from_zombie",
                new ResourceLocation("minecraft", "entities/zombie"),
                0.2f,
                0.80f,
                0.15f,
                0.04f,
                0.01f
        );
    }

    private void addRuneModifier(String name, ResourceLocation lootTable, float dropChance, float common, float uncommon, float rare, float epic) {
        Map<String, Float> chances = Map.of(
                "common", common,
                "uncommon", uncommon,
                "rare", rare,
                "epic", epic
        );

        add(name, new AddSpecialModifier(
                new LootItemCondition[]{
                        new LootTableIdCondition.Builder(lootTable).build(),
                        LootItemKilledByPlayerCondition.killedByPlayer().build(),
                        LootItemRandomChanceCondition.randomChance(dropChance).build(),
                        new RuneCollectorCondition(chances)
                },
                BPItems.SOCKET_GEM.get()
        ));
    }


}
