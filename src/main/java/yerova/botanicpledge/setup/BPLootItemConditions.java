package yerova.botanicpledge.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.loot.conditionn.RuneCollectorCondition;

public class BPLootItemConditions {

    public static final DeferredRegister<LootItemConditionType> LOOT_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, BotanicPledge.MOD_ID);

    public static final RegistryObject<LootItemConditionType> RANDOM_CHANCE_WITH_RUNE_COLLECTOR = LOOT_TYPES.register("random_chance_with_rune_collector", () -> new LootItemConditionType(new RuneCollectorCondition.Serializer()));



}
