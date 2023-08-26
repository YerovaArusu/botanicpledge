package yerova.botanicpledge.common.items.relic;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.IXplatAbstractions;
import yerova.botanicpledge.common.capabilities.BPAttribute;
import yerova.botanicpledge.common.capabilities.BPAttributeProvider;
import yerova.botanicpledge.common.entitites.projectiles.AsgardBladeEntity;
import yerova.botanicpledge.common.items.SoulAmulet;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.EntityUtils;
import yerova.botanicpledge.setup.BPItemTiers;

import java.util.*;

public class AsgardFractal extends SwordItem {
    public HashMap<LivingEntity, Integer> targetsNTime = new HashMap<>();
    public final int MAX_ENTITIES = 10;
    public final int MAX_TICK_AS_TARGET = 200;
    public float ATTACK_SPEED_MODIFIER;
    public float ATTACK_DAMAGE_MODIFIER;


    public AsgardFractal(int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(BPItemTiers.YGGDRALIUM_TIER, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        ATTACK_DAMAGE_MODIFIER = pAttackDamageModifier;
        ATTACK_SPEED_MODIFIER = pAttackSpeedModifier;
    }


    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player && player.getMainHandItem().equals(stack)) {
            if (!pLevel.isClientSide) {
                Entity entity = EntityUtils.getPlayerPOVHitResult(player.level, player, 24);

                if (entity instanceof LivingEntity entity1) {
                    if (targetsNTime.isEmpty() || (!EntityUtils.hasIdMatch(targetsNTime.keySet(), entity1) && targetsNTime.size() < MAX_ENTITIES)) {

                        if (entity instanceof Player enemy) {
                            for (SlotResult result: CuriosApi.getCuriosHelper().findCurios(player, "necklace")) {
                                if (!(result.stack().getItem() instanceof SoulAmulet || SoulAmulet.amuletContainsSoul(stack, enemy.getUUID()))) {
                                    entity1.setGlowingTag(true);
                                    targetsNTime.put(entity1, 0);
                                }
                            }
                        } else {
                            entity1.setGlowingTag(true);
                            targetsNTime.put(entity1, 0);
                        }

                    }
                }

                //Target Time Handling
                if (targetsNTime != null) {
                    for (LivingEntity e : targetsNTime.keySet()) {
                        int ticks = targetsNTime.get(e);
                        if (ticks < MAX_TICK_AS_TARGET || e.isAlive()) {
                            targetsNTime.put(e, ++ticks);
                        } else {
                            targetsNTime.remove(e);
                        }
                    }
                }


            } else {

            }

            //Relic Handler
            var relic = IXplatAbstractions.INSTANCE.findRelic(stack);
            if (relic != null) {
                relic.tickBinding(player);
            }
        }
        super.inventoryTick(stack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {


        stack.getCapability(BPAttributeProvider.ATTRIBUTE).ifPresent(attribute -> {


            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ATTACK_DAMAGE_TAG_NAME))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ATTACK_DAMAGE_TAG_NAME)).toList()) {
                    tooltip.add(new TextComponent("+ " + entry.getValue()+ " " + new TranslatableComponent(BPConstants.ATTACK_DAMAGE_TAG_NAME).getString()).withStyle(ChatFormatting.BLUE));
                }
            }

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ATTACK_SPEED_TAG_NAME))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ATTACK_SPEED_TAG_NAME)).toList()) {
                    tooltip.add(new TextComponent("+ " + entry.getValue()+" " + new TranslatableComponent(BPConstants.ATTACK_SPEED_TAG_NAME).getString()).withStyle(ChatFormatting.BLUE));
                }
            }

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.NO_RUNE_GEM))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.NO_RUNE_GEM)).toList()) {
                    tooltip.add(new TextComponent(new TranslatableComponent(BPConstants.NO_RUNE_GEM).getString()).withStyle(ChatFormatting.BLUE));
                }
            }
        });

        RelicImpl.addDefaultTooltip(stack, tooltip);
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {


        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        if (stack.getCapability(BPAttributeProvider.ATTRIBUTE).isPresent()) {
            BPAttribute attribute = stack.getCapability(BPAttributeProvider.ATTRIBUTE).resolve().get();

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ATTACK_DAMAGE_TAG_NAME))) {
                double attributeValue = 0.0;
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ATTACK_DAMAGE_TAG_NAME)).toList()) {
                    attributeValue += entry.getValue();
                }
                AttributeModifier modifier = new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage() + attributeValue, AttributeModifier.Operation.ADDITION);

                builder.put(Attributes.ATTACK_DAMAGE, modifier);
            } else {
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage(), AttributeModifier.Operation.ADDITION));
            }

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ATTACK_SPEED_TAG_NAME))) {
                double attributeValue = ATTACK_SPEED_MODIFIER;
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ATTACK_SPEED_TAG_NAME)).toList()) {
                    attributeValue = attributeValue + (entry.getValue()/100);
                }
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier",attributeValue, AttributeModifier.Operation.ADDITION));
            } else {
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", ATTACK_SPEED_MODIFIER, AttributeModifier.Operation.ADDITION));
            }
        }

        ImmutableMultimap<Attribute, AttributeModifier> modifiers = builder.build().isEmpty() ?
                (ImmutableMultimap<Attribute, AttributeModifier>) super.getAttributeModifiers(slot, stack) : builder.build();


        return slot == EquipmentSlot.MAINHAND ? modifiers : super.getDefaultAttributeModifiers(slot);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {

        if (targetsNTime != null) {
            LivingEntity[] attackables = targetsNTime.keySet().toArray(new LivingEntity[0]);
            for (int i = 0; i <= MAX_ENTITIES - 1; i++) {

                if (attackables.length > 0) {
                    if (attackables.length >= MAX_ENTITIES) {
                        summonProjectile(level, player, attackables[i]);
                    } else if (i < attackables.length) {
                        summonProjectile(level, player, attackables[i]);
                    } else if (i % (attackables.length) > 0) {
                        summonProjectile(level, player, attackables[i % attackables.length]);
                    } else {
                        summonProjectile(level, player, attackables[0]);
                    }
                }
            }
            if (!level.isClientSide) targetsNTime = new HashMap<>();
        }

        return super.use(level, player, pUsedHand);
    }

    public void summonProjectile(Level level, Player player, LivingEntity target) {
        double range = 4D;
        double j = -Math.PI + 2 * Math.PI * Math.random();
        double k;
        double x, y, z;

        k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
        x = player.getX() + range * Math.sin(k) * Math.cos(j);
        y = player.getY() + range * Math.cos(k);
        z = player.getZ() + range * Math.sin(k) * Math.sin(j);
        j += 2 * Math.PI * Math.random() * 0.08F + 2 * Math.PI * 0.17F;


        AsgardBladeEntity blade = new AsgardBladeEntity(level, player, target, player.getMainHandItem().getMaxDamage());
        blade.setDamage(this.getDamage());
        blade.setPos(x, y, z);
        blade.setVariety(1);
        blade.faceTarget(1);
        blade.setNoGravity(true);
        level.addFreshEntity(blade);
    }



    public static IRelic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }



}
