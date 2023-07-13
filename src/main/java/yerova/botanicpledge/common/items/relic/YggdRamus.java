package yerova.botanicpledge.common.items.relic;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.IXplatAbstractions;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.client.particle.custom.ManaSweepParticleData;
import yerova.botanicpledge.common.entitites.projectiles.YggdFocus;
import yerova.botanicpledge.common.entitites.projectiles.YggdrafoliumEntity;
import yerova.botanicpledge.common.items.TierInit;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.LeftClickable;
import yerova.botanicpledge.common.utils.PlayerUtils;

import java.util.List;

public class YggdRamus extends SwordItem implements LeftClickable {

    public final int MANA_COST_PER_SHOT = 4000;
    public final int SUMMON_AMOUNT_PER_CLICK = 4;


    public YggdRamus(Properties pProperties) {
        super(TierInit.YGGDRALIUM_TIER, -4, 0, pProperties);

    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && entity instanceof Player player) {

            //Relic Handler
            var relic = IXplatAbstractions.INSTANCE.findRelic(stack);
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
        Player player = PlayerUtils.getStackOwner(world, stack);
        if (!DivineCoreItem.playerHasCoreWithRankEquipped(player, BPConstants.CORE_RANK_REQUIRED_FOR_YGGD_RAMUS)) {
            tooltip.add(new TranslatableComponent("tooltip_stat_low_level").withStyle(ChatFormatting.DARK_RED));
        }

        RelicImpl.addDefaultTooltip(stack, tooltip);

        tooltip.add(new TranslatableComponent("switch_weapon_mode_tooltip", new TextComponent("V").withStyle(ChatFormatting.YELLOW)));
        super.appendHoverText(stack, world, tooltip, flags);

    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!DivineCoreItem.playerHasCoreWithRankEquipped(player, BPConstants.CORE_RANK_REQUIRED_FOR_YGGD_RAMUS))
            return super.use(level, player, hand);

        if (YggdRamus.isRanged(player.getMainHandItem())) {
            shootProjectiles(player);
        }
        if (!(YggdRamus.isRanged(player.getMainHandItem()))) {
            //Do stuff if not on ranged mode

            if (player.isShiftKeyDown()) {
                if (player.isOnGround()) {
                    for (LivingEntity entity : YggdRamus.getEntitiesAround(player.getOnPos(), 6, level)) {
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0, 1D, 0));

                        entity.hurt(DamageSource.playerAttack(player), getDamage()/3);
                    }
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
        }


        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!DivineCoreItem.playerHasCoreWithRankEquipped(pContext.getPlayer(), BPConstants.CORE_RANK_REQUIRED_FOR_YGGD_RAMUS))
            return InteractionResult.FAIL;
        Player player = pContext.getPlayer();

        Vec3 targetPos = player.position().add(player.getLookAngle().scale(5D));
        YggdFocus focus = new YggdFocus(pContext.getLevel(), player);
        focus.setPos(targetPos.x, targetPos.y + 2, targetPos.z);
        if (!pContext.getLevel().isClientSide)
            pContext.getLevel().addFreshEntity(focus);
        player.getCooldowns().addCooldown(this, 40);
        return InteractionResult.SUCCESS;

    }

    public static IRelic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (DivineCoreItem.playerHasCoreWithRankEquipped(player, BPConstants.CORE_RANK_REQUIRED_FOR_YGGD_RAMUS)) {
            if (YggdRamus.isRanged(player.getMainHandItem()))    {
                //TODO: do something on Ranged Mode
            }else if (!(YggdRamus.isRanged(player.getMainHandItem()))) {
                this.sweepAttack(player.getLevel(), player, 0.4F);
            }
        }


        return super.onLeftClickEntity(stack, player, entity);
    }


    @Override
    public void LeftClick(Level level, Player player, ItemStack stack) {
        if (!DivineCoreItem.playerHasCoreWithRankEquipped(player, BPConstants.CORE_RANK_REQUIRED_FOR_YGGD_RAMUS))
            return;

        if (YggdRamus.isRanged(player.getMainHandItem())) {
            //TODO: do something on Ranged Mode
        } else if (!(YggdRamus.isRanged(player.getMainHandItem()))) {
            this.sweepAttack(player.getLevel(), player, 0.4F);
        }
    }

    public static List<LivingEntity> getEntitiesAround(BlockPos source, float range, Level world) {
        return world.getEntitiesOfClass(LivingEntity.class,
                new AABB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range,
                        source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
    }

    public void shootProjectiles(LivingEntity player) {

        HitResult result = raytrace(player, 16, true);
        BlockPos targetPos = result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) result).getEntity().getOnPos() : ((BlockHitResult) result).getBlockPos();

        double range = 4D;
        double j = -Math.PI + 2 * Math.PI * Math.random();
        double k;
        double x, y, z;
        for (int i = 0; i < this.SUMMON_AMOUNT_PER_CLICK - 1; i++) {
            if (ManaItemHandler.instance().requestManaExact(player.getMainHandItem(), ((Player) player), MANA_COST_PER_SHOT, true)) {
                YggdrafoliumEntity sword = new YggdrafoliumEntity(player.level, player, targetPos, this.getDamage() / 8);
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

                player.level.addFreshEntity(sword);
            }
        }
    }

    public void sweepAttack(Level level, Player player, double knockbackStrength) {

        for (LivingEntity enemy : level.getEntitiesOfClass(LivingEntity.class, this.getSweepHitBox(player.getMainHandItem(), player))) {
            if (enemy != player && player.canHit(enemy, 0)) { // Original check was dist < 3, range is 3, so vanilla used padding=0


                enemy.knockback(knockbackStrength, (double) Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(player.getYRot() * ((float) Math.PI / 180F))));
                enemy.hurt(DamageSource.playerAttack(player), this.getDamage());
                YggdRamus.appendFireAspect(player, enemy);


            }
        }
        double d0 = (double) (-Mth.sin(player.getYRot() * ((float) Math.PI / 180F)));
        double d1 = (double) Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
        if(level.isClientSide) {
            level.addParticle(ManaSweepParticleData.createData(new ParticleColor(66,214,227)),
                    player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 1.0D, 1.0D,1.0D );
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

    public static HitResult raytrace(Entity e, double distance, boolean fluids) {
        return e.pick(distance, 1, fluids);
    }


}
