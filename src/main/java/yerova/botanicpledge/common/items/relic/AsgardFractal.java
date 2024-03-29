package yerova.botanicpledge.common.items.relic;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;
import yerova.botanicpledge.common.capabilities.BPAttributeProvider;
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
import java.util.Map;

public class AsgardFractal extends SwordItem {
    public HashMap<LivingEntity, Integer> targetsNTime = new HashMap<>();
    public static final int MAX_ENTITIES = 10;
    public final int MAX_TICK_AS_TARGET = 200;
    public float ATTACK_SPEED_MODIFIER;
    public float ATTACK_DAMAGE_MODIFIER;
    public static final int MAX_ABILITIES = 4;


    public AsgardFractal(int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(BPItemTiers.YGGDRALIUM_TIER, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        ATTACK_DAMAGE_MODIFIER = pAttackDamageModifier;
        ATTACK_SPEED_MODIFIER = pAttackSpeedModifier;
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
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {

        RelicImpl.addDefaultTooltip(stack, tooltip);
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);


        stack.getCapability(BPAttributeProvider.ATTRIBUTE).ifPresent(attribute -> {

            tooltip.add(Component.literal("Current Selected Skill: " + getCurrentSkill(stack)).withStyle(ChatFormatting.GOLD));

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ATTACK_DAMAGE_TAG_NAME))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ATTACK_DAMAGE_TAG_NAME)).toList()) {
                    tooltip.add(Component.literal("+ " + entry.getValue() + " " + Component.translatable(BPConstants.ATTACK_DAMAGE_TAG_NAME).getString()).withStyle(ChatFormatting.BLUE));
                }
            }

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.ATTACK_SPEED_TAG_NAME))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.ATTACK_SPEED_TAG_NAME)).toList()) {
                    tooltip.add(Component.literal("+ " + entry.getValue() + " " + Component.translatable(BPConstants.ATTACK_SPEED_TAG_NAME).getString()).withStyle(ChatFormatting.BLUE));
                }
            }

            if (attribute.getAttributesNamesAndValues().stream().anyMatch(entry -> entry.getKey().equals(BPConstants.NO_RUNE_GEM))) {
                for (Map.Entry<String, Double> entry : attribute.getAttributesNamesAndValues().stream().filter(e -> e.getKey().equals(BPConstants.NO_RUNE_GEM)).toList()) {
                    tooltip.add(Component.literal(Component.translatable(BPConstants.NO_RUNE_GEM).getString()).withStyle(ChatFormatting.BLUE));
                }
            }
        });

    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {

        if (ManaItemHandler.instance().requestManaExact(player.getMainHandItem(), player, 80_000, true)) {
            activateCurrentSkill(level, player, player.getMainHandItem());
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
            blade.setVariety(1);
            blade.faceTarget(1);
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

    public static void activateCurrentSkill(Level level, Player player, ItemStack stack) {
        if (!(stack.getItem() instanceof AsgardFractal)) return;
        switch (getCurrentSkill(stack)) {
            case 1 -> {
                player.displayClientMessage(Component.literal("Activated \"Shoot Blades\""), true);
                HashMap<LivingEntity, Integer> targetsNTime = ((AsgardFractal) stack.getItem()).targetsNTime;

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
                        } else
                            player.displayClientMessage(Component.literal("No Attackable Enemies found").withStyle(ChatFormatting.DARK_RED), true);
                    }
                    player.getCooldowns().addCooldown(stack.getItem(), 25);
                    if (!level.isClientSide) ((AsgardFractal) stack.getItem()).targetsNTime = new HashMap<>();
                }

            }
            case 2 -> {
                player.displayClientMessage(Component.literal("Activated \"Enemy Collector\""), true);
                HitResult result = EntityUtils.raytrace(player, 16, true);
                YggdFocusEntity focus = new YggdFocusEntity(level, player);
                focus.setPos(result.getLocation().x, result.getLocation().y, result.getLocation().z);

                player.getCooldowns().addCooldown(stack.getItem(), 25);

                if (!level.isClientSide)
                    level.addFreshEntity(focus);
            }
            case 3 -> {
                player.displayClientMessage(Component.literal("Activated \"Push Back\""), true);

                for (LivingEntity entity : EntityUtils.getAttackableEnemiesAround(player, level, 8, new EntityUtils.AttackableEntitiesSelector())) {
                    if (entity instanceof Mob) {
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 2, false, false));
                    }
                    if (entity == player)
                        continue;

                    Vec3 vect = entity.position().subtract(player.position());
                    entity.setDeltaMovement(vect.scale(4.5D));
                    entity.hurt(level.damageSources().playerAttack(player), 20);
                }

                player.getCooldowns().addCooldown(stack.getItem(), 40);

            }

            case 4 -> {
                player.displayClientMessage(Component.literal("Activated \"Swing Attack\""), true);
                PlayerUtils.sweepAttack(level, player, stack, 0.4F);
                player.getCooldowns().addCooldown(stack.getItem(), 10);
            }
            default -> {
                player.displayClientMessage(Component.literal("No Ability Selected:"), true);
            }

        }
    }
}
