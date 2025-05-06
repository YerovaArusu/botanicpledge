package yerova.botanicpledge.loot.conditionn;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import yerova.botanicpledge.setup.BPEnchantments;
import yerova.botanicpledge.setup.BPLootItemConditions;


import java.util.*;

public class RuneCollectorCondition implements LootItemCondition {


    private final Map<String, Float> rarityChances;

    public RuneCollectorCondition(Map<String, Float> rarityChances) {
        this.rarityChances = rarityChances;
    }

    public boolean test(LootContext context) {
        Entity killer = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (!(killer instanceof LivingEntity living)) return false;

        int runeCollectorLevel = EnchantmentHelper.getEnchantmentLevel(BPEnchantments.RUNE_COLLECTOR_ENCHANTMENT.get(), living);
        return runeCollectorLevel > 0;
    }


    public Map<String, Float> getRarityChances() {
        return rarityChances;
    }

    @Override
    public LootItemConditionType getType() {
        return BPLootItemConditions.RANDOM_CHANCE_WITH_RUNE_COLLECTOR.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.KILLER_ENTITY);
    }


    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<RuneCollectorCondition> {

        @Override
        public void serialize(JsonObject json, RuneCollectorCondition condition, JsonSerializationContext context) {
            JsonObject rarityObj = new JsonObject();
            condition.rarityChances.forEach(rarityObj::addProperty);
            json.add("rarity_chances", rarityObj);
        }

        @Override
        public RuneCollectorCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            JsonObject rarityObj = GsonHelper.getAsJsonObject(json, "rarity_chances");
            Map<String, Float> rarityChances = new HashMap<>();

            for (Map.Entry<String, JsonElement> entry : rarityObj.entrySet()) {
                rarityChances.put(entry.getKey(), GsonHelper.convertToFloat(entry.getValue(), entry.getKey()));
            }

            return new RuneCollectorCondition(rarityChances);
        }
    }
}
