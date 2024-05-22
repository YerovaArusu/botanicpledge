package yerova.botanicpledge.common.items.relic;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;
import yerova.botanicpledge.common.capabilities.Attribute;
import yerova.botanicpledge.common.capabilities.AttributeProvider;
import yerova.botanicpledge.common.entitites.projectiles.AsgardBladeEntity;
import yerova.botanicpledge.common.entitites.projectiles.YggdFocusEntity;
import yerova.botanicpledge.common.items.SoulAmulet;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.EntityUtils;
import yerova.botanicpledge.common.utils.PlayerUtils;
import yerova.botanicpledge.integration.curios.ItemHelper;
import yerova.botanicpledge.setup.BPItemTiers;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class AsgardFractal extends SwordItem {
    public HashMap<LivingEntity, Integer> targetsNTime = new HashMap<>();
    public static final int MAX_ENTITIES = 10;
    public final int MAX_TICK_AS_TARGET = 200;
    public static final int MAX_ABILITIES = 4;

    public AsgardFractal(int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(BPItemTiers.YGGDRALIUM_TIER, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }


    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player && player.getMainHandItem().equals(stack)) {
            if (!pLevel.isClientSide) {
                if (getCurrentSkill(stack) == 1) {
                    Entity entity = EntityUtils.getPlayerPOVHitResult(player.level(), player, 24);
                    if (entity instanceof LivingEntity entity1) {
                        if (targetsNTime.isEmpty() || (!EntityUtils.hasIdMatch(targetsNTime.keySet(), entity1) && targetsNTime.size() < MAX_ENTITIES)) {

                            if (entity instanceof Player enemy) {

                                ItemHelper.getCurio(player, "necklace").forEach(slotResult -> {
                                    if (!(slotResult.stack().getItem() instanceof SoulAmulet || SoulAmulet.amuletContainsSoul(stack, enemy.getUUID()))) {
                                        entity1.setGlowingTag(true);
                                        targetsNTime.put(entity1, 0);
                                    }
                                });

                            } else {
                                entity1.setGlowingTag(true);
                                targetsNTime.put(entity1, 0);
                            }
                        }
                    }
                }
                //Target Time Handling
                List<LivingEntity> entityList = targetsNTime.keySet().stream().toList();
                if (entityList != null) {
                    for (int i = 0; i < entityList.size(); i++) {
                        LivingEntity e = entityList.get(i);

                        int ticks = targetsNTime.get(e);
                        if (ticks < MAX_TICK_AS_TARGET || e.isAlive()) {
                            targetsNTime.put(e, ++ticks);
                        } else {
                            targetsNTime.remove(e);
                            e.setGlowingTag(false);
                        }
                    }
                }
            }

            //Relic Handler
            var relic = XplatAbstractions.INSTANCE.findRelic(stack);
            if (relic != null) {
                relic.tickBinding(player);
            }
        }

        super.inventoryTick(stack, pLevel, pEntity, pSlotId, pIsSelected);
    }


    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {

        //TODO: make a method that executes a dash-like ability even if no entity is around
        //TODO: Improve this, so when moving the player does deal damage to enemy collisions

        if (!player.isCrouching() && ManaItemHandler.instance().requestManaExact(player.getMainHandItem(), player, 500, true)) {
            Vec3 dist = player.position().add(entity.position().add(0,1,0).reverse()).reverse();
            player.addDeltaMovement(dist);
        }

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {

        ImmutableMultimap.Builder<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        if (stack.getCapability(AttributeProvider.ATTRIBUTE).isPresent()) {
            Attribute attribute = stack.getCapability(AttributeProvider.ATTRIBUTE).resolve().get();


            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, BPConstants.ATTACK_DAMAGE_TAG_NAME,
                    attribute.sumRunesOfType(Attribute.Rune.StatType.ATTACK_DAMAGE) +
                            getDefaultAttributeModifiers(slot).get(Attributes.ATTACK_DAMAGE).stream().mapToDouble(AttributeModifier::getAmount).sum(),
                    AttributeModifier.Operation.ADDITION));

            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, BPConstants.ATTACK_SPEED_TAG_NAME,
                    attribute.sumRunesOfType(Attribute.Rune.StatType.ATTACK_SPEED)/100 *
                            getDefaultAttributeModifiers(slot).get(Attributes.ATTACK_SPEED).stream().mapToDouble(AttributeModifier::getAmount).sum(),
                    AttributeModifier.Operation.ADDITION));
        }

        return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);

        stack.getCapability(AttributeProvider.ATTRIBUTE).ifPresent(attribute -> {

            tooltip.add(Component.literal("Current Selected Skill: " + getCurrentSkill(stack)).withStyle(ChatFormatting.GOLD));

            attribute.getAllRunes().forEach(rune -> {
                tooltip.add(Component.literal("+ " + rune.getValue() + " " + Component.translatable(rune.getStatType().name().toLowerCase()).getString()).withStyle(ChatFormatting.BLUE));
            });

            if (attribute.hasEmptySocket()) {
                tooltip.add(Component.literal(Component.translatable(BPConstants.NO_RUNE_GEM).getString() + ": " + (attribute.getMaxRunes() - attribute.getAllRunes().size())).withStyle(ChatFormatting.GOLD));
            }

        });
        RelicImpl.addDefaultTooltip(stack, tooltip);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {

        ItemStack stack = player.getItemInHand(pUsedHand);

        if (ManaItemHandler.instance().requestManaExact(player.getMainHandItem(), player, 10_000, true)) {
            HashMap<LivingEntity, Integer> targetsNTime = ((AsgardFractal) stack.getItem()).targetsNTime;

            if (!targetsNTime.isEmpty()) {
                LivingEntity[] attackables = targetsNTime.keySet().toArray(new LivingEntity[0]);
                for (int i = 0; i <= MAX_ENTITIES - 1; i++) {

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
                player.getCooldowns().addCooldown(stack.getItem(), 25);
                if (!level.isClientSide) ((AsgardFractal) stack.getItem()).targetsNTime = new HashMap<>();
            } else {
                for (int i = 0; i <= MAX_ENTITIES - 1; i++) {
                    BlockPos pos = ((BlockHitResult)player.pick(20.0D,0, false)).getBlockPos();
                    System.out.println(pos);

                    summonProjectile(level,player,pos);
                }
            }
        }
        return super.use(level, player, pUsedHand);
    }

    public static void summonProjectile(Level level, Player player, LivingEntity target) {
        if (player.getMainHandItem().getItem() instanceof AsgardFractal) {
            double range = 4D;
            double j = -Math.PI + 2 * Math.PI * Math.random();
            double k;
            double x, y, z;

            k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
            x = player.getX() + range * Math.sin(k) * Math.cos(j);
            y = player.getY() + range * Math.cos(k);
            z = player.getZ() + range * Math.sin(k) * Math.sin(j);

            float damage = ((AsgardFractal) player.getMainHandItem().getItem()).getDamage();

            AsgardBladeEntity blade = new AsgardBladeEntity(level, player, target, damage);
            blade.setDamage(damage);
            blade.setPos(x, y, z);
            blade.setNoGravity(true);
            level.addFreshEntity(blade);
        }
    }

    public static void summonProjectile(Level level, Player player, BlockPos targetPos) {
        if (player.getMainHandItem().getItem() instanceof AsgardFractal) {
            double range = 4D;
            double j = -Math.PI + 2 * Math.PI * Math.random();
            double k;
            double x, y, z;

            k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
            x = player.getX() + range * Math.sin(k) * Math.cos(j);
            y = player.getY() + range * Math.cos(k);
            z = player.getZ() + range * Math.sin(k) * Math.sin(j);

            float damage = ((AsgardFractal) player.getMainHandItem().getItem()).getDamage();

            AsgardBladeEntity blade = new AsgardBladeEntity(level, player, targetPos, damage);
            blade.facePosition(targetPos);
            blade.setDamage(damage);
            blade.setPos(x, y, z);
            blade.setTargetPos(targetPos);
            blade.setNoGravity(true);
            level.addFreshEntity(blade);
        }
    }


    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    public static void switchSkill(Player player, ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
        int i = 0;
        if (tag.contains(BPConstants.CURRENT_ABILITY_TAG)) {
            i = tag.getInt(BPConstants.CURRENT_ABILITY_TAG);
            if (!player.isCrouching()) {
                if (i >= MAX_ABILITIES) {
                    i = 0;
                } else i++;
            } else {
                if (i <= 0) {
                    i = MAX_ABILITIES;
                } else i--;
            }
        }
        player.displayClientMessage(Component.literal("Selected Ability: " + i).withStyle(ChatFormatting.GOLD), true);
        tag.putInt(BPConstants.CURRENT_ABILITY_TAG, i);
    }

    public static int getCurrentSkill(ItemStack stack) {
        return stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).contains(BPConstants.CURRENT_ABILITY_TAG) ?
                stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getInt(BPConstants.CURRENT_ABILITY_TAG) : 0;
    }


    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

}
