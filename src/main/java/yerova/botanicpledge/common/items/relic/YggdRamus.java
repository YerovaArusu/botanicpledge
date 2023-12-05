package yerova.botanicpledge.common.items.relic;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.client.particle.custom.ManaSweepParticleData;
import yerova.botanicpledge.common.entitites.projectiles.YggdFocusEntity;
import yerova.botanicpledge.common.entitites.projectiles.YggdrafoliumEntity;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.EntityUtils;
import yerova.botanicpledge.setup.BPItemTiers;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class YggdRamus extends SwordItem {

    public final int MANA_COST_FOR_THROW_INTO_AIR = 40_000;
    public final int MANA_COST_PER_SHOT = 10_000;
    public final int MANA_COST_COLLECT_ENEMIES = 20_000;
    public final int SUMMON_AMOUNT_PER_CLICK = 4;


    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }



    public YggdRamus(Properties pProperties) {
        super(BPItemTiers.YGGDRALIUM_TIER, -4, 0, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && entity instanceof Player player) {


            //Relic Handler
            var relic = XplatAbstractions.INSTANCE.findRelic(stack);
            if (relic != null) {
                relic.tickBinding(player);
            }

            if (YggdRamus.isRanged(stack)) {
                //TODO: set A much slower attack speed
            }
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        if (world == null) return;
        if (!(DivineCoreItem.getLevel(stack) < BPConstants.CORE_RANK_REQUIRED_FOR_YGGD_RAMUS)) {
            tooltip.add(Component.literal("tooltip_stat_low_level").withStyle(ChatFormatting.DARK_RED));
        }

        RelicImpl.addDefaultTooltip(stack, tooltip);

        tooltip.add(Component.translatable("switch_weapon_mode_tooltip", Component.literal("V").withStyle(ChatFormatting.YELLOW)));
        super.appendHoverText(stack, world, tooltip, flags);

    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!(DivineCoreItem.getLevel(player.getMainHandItem()) < BPConstants.CORE_RANK_REQUIRED_FOR_YGGD_RAMUS))
            return super.use(level, player, hand);

        if (YggdRamus.isRanged(player.getMainHandItem())) {
            shootProjectilesAbility(player);
        }
        if (!(YggdRamus.isRanged(player.getMainHandItem()))) {
            //Do stuff if not on ranged mode
            shootEnemiesIntoSkyAbility(level, player, 6);
        }


        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!(DivineCoreItem.getLevel(pContext.getItemInHand()) < BPConstants.CORE_RANK_REQUIRED_FOR_YGGD_RAMUS))
            return InteractionResult.FAIL;
        collectEnemiesAbility(pContext.getPlayer(), pContext.getLevel());

        return InteractionResult.SUCCESS;
    }

    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!(DivineCoreItem.getLevel(stack) < BPConstants.CORE_RANK_REQUIRED_FOR_YGGD_RAMUS)) {
            if (YggdRamus.isRanged(player.getMainHandItem())) {
                //TODO: do something on Ranged Mode
            } else if (!(YggdRamus.isRanged(player.getMainHandItem()))) {
                this.sweepAttack(player.level(), player, 0.4F);
            }
        }


        return super.onLeftClickEntity(stack, player, entity);
    }


    public List<LivingEntity> getAttackableEnemiesAroundUser(Player player, Level level, int radius) {
        final TargetingConditions alertableTargeting = TargetingConditions.forCombat().range(radius).ignoreLineOfSight().selector(new AttackableEntitiesSelector());
        return level.getNearbyEntities(LivingEntity.class, alertableTargeting, player,
                new AABB(player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                        player.getX() + radius, player.getY() + radius, player.getZ() + radius));
    }

    public static class AttackableEntitiesSelector implements Predicate<LivingEntity> {
        public boolean test(LivingEntity pEntity) {
            if (pEntity instanceof Mob) {
                return true;
            } else if (pEntity instanceof Player player) {
                return !(player.getMainHandItem().getItem() instanceof YggdRamus && player.getUUID().
                        equals(Objects.requireNonNull(XplatAbstractions.INSTANCE.findRelic(player.getMainHandItem())).getSoulbindUUID()));
            } else return false;
        }
    }


    public void shootProjectilesAbility(LivingEntity player) {

        HitResult result = EntityUtils.raytrace(player, 16, true);
        BlockPos targetPos = result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) result).getEntity().getOnPos() : ((BlockHitResult) result).getBlockPos();

        double range = 4D;
        double j = -Math.PI + 2 * Math.PI * Math.random();
        double k;
        double x, y, z;
        for (int i = 0; i < this.SUMMON_AMOUNT_PER_CLICK - 1; i++) {
            if (ManaItemHandler.instance().requestManaExact(player.getMainHandItem(), ((Player) player), MANA_COST_PER_SHOT, true)) {
                YggdrafoliumEntity sword = new YggdrafoliumEntity(player.level(), player, targetPos, this.getDamage() / 8);
                k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
                x = player.getX() + range * Math.sin(k) * Math.cos(j);
                y = player.getY() + range * Math.cos(k);
                z = player.getZ() + range * Math.sin(k) * Math.sin(j);
                j += 2 * Math.PI * Math.random() * 0.08F + 2 * Math.PI * 0.17F;
                sword.setPos(x, y, z);
                sword.faceTarget(1.0F);

                sword.setColor(0x08e8de);
                sword.setStartingMana(MANA_COST_PER_SHOT);
                sword.setMinManaLoss(1);
                sword.setManaLossPerTick(1F);
                sword.setMana(MANA_COST_PER_SHOT);
                sword.setGravity(0F);
                sword.setDeltaMovement(sword.getDeltaMovement().scale(0.8));

                sword.setSourceLens(player.getItemInHand(player.getUsedItemHand()).copy());

                player.level().addFreshEntity(sword);
            }
        }
    }

    public void sweepAttack(@NotNull Level level, @NotNull Player player, double knockbackStrength) {
        if (!level.isClientSide) {
            for (LivingEntity enemy : level.getEntitiesOfClass(LivingEntity.class, this.getSweepHitBox(player.getMainHandItem(), player))) {
                if (enemy != level.getPlayerByUUID(Objects.requireNonNull(Objects.requireNonNull(XplatAbstractions.INSTANCE.findRelic(player.getMainHandItem())).getSoulbindUUID())) && player.canAttack(enemy)) { // Original check was dist < 3, range is 3, so vanilla used padding=0

                    enemy.knockback(knockbackStrength, (double) Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(player.getYRot() * ((float) Math.PI / 180F))));
                    hurtEnemy(player.getMainHandItem(), enemy, player);

                    YggdRamus.appendFireAspect(player, enemy);
                }
            }
            double d0 = (double) (-Mth.sin(player.getYRot() * ((float) Math.PI / 180F)));
            double d1 = (double) Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
            if (level.isClientSide) {
                level.addParticle(ManaSweepParticleData.createData(new ParticleColor(66, 214, 227)),
                        player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 1.0D, 1.0D, 1.0D);
            }
        }
    }

    @SoftImplement("IForgeItem")
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return reequipAnimation(oldStack, newStack);
    }

    @SoftImplement("IForgeItem")
    private boolean reequipAnimation(ItemStack before, ItemStack after) {
        return !after.is(this) || isRanged(before) != isRanged(after);
    }

    @NotNull
    public AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player) {
        return player.getBoundingBox().inflate(3.0D, 1D, 3.0D);
    }

    public static boolean isRanged(ItemStack stack) {
        boolean returner = false;
        if (stack.getItem() instanceof YggdRamus) {
            returner = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getBoolean(BPConstants.TAG_RANGED_MODE);
        }
        return returner;
    }

    public static void setRanged(ItemStack stack, boolean enabled) {
        if (stack.getItem() instanceof YggdRamus) {
            CompoundTag stats = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
            stats.putBoolean(BPConstants.TAG_RANGED_MODE, enabled);
        }
    }

    public static void appendFireAspect(Player player, Entity entity) {
        int enchantmentLvl = EnchantmentHelper.getFireAspect(player);
        if (enchantmentLvl > 0 && !entity.isOnFire()) {
            entity.setSecondsOnFire(enchantmentLvl * 4);
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }



    public void shootEnemiesIntoSkyAbility(Level level, Player player, int radius) {

        if (player.isShiftKeyDown() && ManaItemHandler.instance().requestManaExact(player.getMainHandItem(), player, MANA_COST_FOR_THROW_INTO_AIR, true) || player.isCreative()) {
            for (LivingEntity entity : getAttackableEnemiesAroundUser(player, level, radius)) {

                //Lift and damage
                entity.hurt(level.damageSources().playerAttack(player), getDamage() / 2);
                YggdRamus.appendFireAspect(player, entity);
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, 1.0, 0));

            }
            player.setDeltaMovement(player.getDeltaMovement().add(0, 1.0D, 0));

            //Particles
            if (level.isClientSide)
                for (int i = 0; i < 360; i += 30) {
                    double r = 3D;
                    double x = player.getX() + r * Math.cos(Math.toRadians(i));
                    double y = player.getY() + 0.5D;
                    double z = player.getZ() + r * Math.sin(Math.toRadians(i));
                    for (int j = 0; j < 6; j++)
                        level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0.12F * j, 0);
                }
        }
    }

    public void collectEnemiesAbility(Player player, Level level) {
        if (ManaItemHandler.instance().requestManaExact(player.getMainHandItem(), player, MANA_COST_COLLECT_ENEMIES, true) || player.isCreative()) {
            Vec3 targetPos = player.position().add(player.getLookAngle().scale(5D));
            YggdFocusEntity focus = new YggdFocusEntity(level, player);
            focus.setPos(targetPos.x, targetPos.y + 2, targetPos.z);
            if (!level.isClientSide)
                level.addFreshEntity(focus);
            player.getCooldowns().addCooldown(this, 40);
        }
    }

}
