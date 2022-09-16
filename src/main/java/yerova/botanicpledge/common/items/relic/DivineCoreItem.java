package yerova.botanicpledge.common.items.relic;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.BotanicPledge;
import yerova.botanicpledge.common.utils.divine_core_utils.IDivineCoreAttributes;


import java.util.ArrayList;
import java.util.UUID;

public class DivineCoreItem extends ItemRelic implements ICurioItem {

    private static ArrayList<Attribute> attributeList() {
        ArrayList<Attribute> list = new ArrayList<Attribute>();
        list.add(Attributes.ARMOR);
        list.add(Attributes.ARMOR_TOUGHNESS);
        list.add(Attributes.MAX_HEALTH);
        list.add(Attributes.ATTACK_DAMAGE);
        list.add(Attributes.KNOCKBACK_RESISTANCE);
        list.add(Attributes.MOVEMENT_SPEED);
        list.add(Attributes.ATTACK_SPEED);


        return list;
    }

    private static ArrayList<String> attributeNameList() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("armor");
        list.add("armor_toughness");
        list.add("max_health");
        list.add("attack_damage");
        list.add("knockback_resistance");
        list.add("movement_speed");
        list.add("attack_speed");

        return list;
    }

    public static IRelic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }


    public DivineCoreItem(Properties props) {
        super(props);
    }

    //TODO: Do that thing
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {

        if (prevStack.getEquipmentSlot() == stack.getEquipmentSlot()) {
            return;
        }
        for (int i = 0; i < attributeList().size(); i++) {

            double baseValue = slotContext.entity().getAttribute(attributeList().get(i)).getBaseValue();
            double addValue = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats").getInt(attributeNameList().get(i));

            slotContext.entity().getAttribute(attributeList().get(i)).setBaseValue(baseValue + addValue);
        }

        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }


    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {

        for (int i = 0; i < attributeList().size(); i++) {
            double currentValue = slotContext.entity().getAttribute(attributeList().get(i)).getBaseValue();
            double reducerValue = stack.getOrCreateTagElement(BotanicPledge.MOD_ID + ".stats").getInt(attributeNameList().get(i));

            if (currentValue >= reducerValue) {
                slotContext.entity().getAttribute(attributeList().get(i)).setBaseValue(currentValue - reducerValue);
            }

            if (attributeNameList().get(i).equals(attributeNameList().get(2))) {
                slotContext.entity().hurt(new DamageSource(""), (float) reducerValue);
            }
        }


        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }


}
