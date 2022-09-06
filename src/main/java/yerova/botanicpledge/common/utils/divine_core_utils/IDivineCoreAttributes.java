package yerova.botanicpledge.common.utils.divine_core_utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;

public interface IDivineCoreAttributes {
    Random rand = new Random();

    List<Attribute> attributesList = new ArrayList<>();
    List<String> descriptionAttributesList = new ArrayList<>();

    String NBT_ATTRIBUTES = "Attributes";
    String NBT_UUID = "UUID";

    static void init() {
        Attributes();
    }

    static void Attributes(){
        addNewAttribute(0, Attributes.ARMOR_TOUGHNESS, "ArmorToughness");
        addNewAttribute(1, Attributes.ARMOR, "Armor");
        addNewAttribute(2, Attributes.MAX_HEALTH, "Health_Boost");
        addNewAttribute(3, Attributes.KNOCKBACK_RESISTANCE, "KockbackResistance");
        addNewAttribute(4, Attributes.ATTACK_DAMAGE, "DamageBonus");
    }

    static void addNewAttribute(int index, Attribute attribute, String description)
    {
        attributesList.add(index, attribute);
        descriptionAttributesList.add(index, description);
    }

    static Attribute getAttribute(int index){
        return attributesList.get(index);
    }

    static int getRandomAttributeIndex()
    {
        return rand.nextInt(attributesList.size());
    }
}
