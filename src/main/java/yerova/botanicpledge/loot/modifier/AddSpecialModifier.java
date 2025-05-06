package yerova.botanicpledge.loot.modifier;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import yerova.botanicpledge.common.items.RuneGemItem;
import yerova.botanicpledge.loot.conditionn.RuneCollectorCondition;

import java.util.Map;

public class AddSpecialModifier extends LootModifier {

    public static final Supplier<Codec<AddSpecialModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
                    .apply(inst, AddSpecialModifier::new))
    );

    private final Item item;

    public AddSpecialModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Map<String, Float> rarityChances = null;

        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
            if (condition instanceof RuneCollectorCondition runeCondition) {
                rarityChances = runeCondition.getRarityChances();
            }
        }

        if (rarityChances == null) {
            return generatedLoot; // oder ein Fallback-Map verwenden
        }

        generatedLoot.add(RuneGemItem.getNewAttributedGemStack(context, rarityChances));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
