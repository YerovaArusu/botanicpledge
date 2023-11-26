package yerova.botanicpledge.common.items.magic;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.setup.BPAttributes;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.List;
import java.util.UUID;

public class MagicDamageItem extends Item {
    private float attackDamage = 0;
    public float manaDamage;
    public float attackSpeed;
    public static final UUID BASE_MANA_DAMAGE_UUID = UUID.fromString("6c8f773a-900c-47c4-a4b6-fbdec9f5753d");

    public Lazy<? extends Multimap<Attribute, AttributeModifier>> ATTRIBUTE_LAZY_MAP = Lazy.of(() -> {
        Multimap<Attribute, AttributeModifier> map;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        //Checking if the Forge 'Reach' Attribute is present
        if (BPAttributes.MANA_DAMAGE.isPresent()) {
            builder.put(BPAttributes.MANA_DAMAGE.get(), new AttributeModifier(BASE_MANA_DAMAGE_UUID, "Weapon modifier", manaDamage, AttributeModifier.Operation.ADDITION));
        }
        map = builder.build();
        return map;
    });


    public MagicDamageItem(Properties pProperties, float attackDamage, float manaDamage, float attackSpeed) {
        super(pProperties);
        this.attackDamage = attackDamage;
        this.manaDamage = manaDamage;
        this.attackSpeed = attackSpeed;


    }


    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.ATTRIBUTE_LAZY_MAP.get() : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }


}
