package yerova.botanicpledge.common.utils.divine_core_utils;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.RelicImpl;


import java.util.UUID;

public class DivineCoreItem extends ItemRelic implements IDivineCoreAttributes {

    public static final String NBT_ATTRIBUTES = "Attributes";
    public static final String NBT_UUID = "UUID";

    public static Multimap<Attribute, AttributeModifier> DivineCoreAttributes = HashMultimap.create();

    public DivineCoreItem(Properties props) {
        super(props);
    }

        //TODO: Do that thing
    public void updateDivineCoreAttributes(ItemStack stack, Multimap<Attribute, AttributeModifier> divineCoreAttributes) {

    }

    public static IRelic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    public static int getDivineCoreAttributes(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBT_ATTRIBUTES);
    }



}
