package yerova.botanicpledge.common.items.relic;

/*
    Heavily Inspired by the Failnaught Bow from Extra Botany:
    https://github.com/ExtraMeteorP/Extra-Botany/blob/1.16/src/main/java/com/meteor/extrabotany/common/items/relic/ItemFailnaught.java
 */

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;
import yerova.botanicpledge.common.entitites.projectiles.YggdrafoliumEntity;
import yerova.botanicpledge.common.utils.EntityUtils;

import java.util.List;
import java.util.function.Consumer;

public class UllBow extends BowItem {

    private static final int MANA_PER_DAMAGE = 160;

    public UllBow(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {

    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        Relic relic = XplatAbstractions.INSTANCE.findRelic(stack);
        if (entity instanceof Player player) {

            int i = (int) ((getUseDuration(stack) - count) * 1F);
            if (i < 8)
                return;
            int rank = (i - 8) / 5;
            if (relic.isRightPlayer(player) && ManaItemHandler.INSTANCE.requestManaExact(stack, player, Math.min(800, 350 + rank * 20), true)) {

                HitResult result = EntityUtils.raytrace(player, 16, true);
                BlockPos targetPos = result.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) result).getEntity().getOnPos() : ((BlockHitResult) result).getBlockPos();


                float damage = Math.min(rank, 100);

                int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
                if (j > 0) {
                    damage = (float) (damage * (j * 0.5 + 5));
                }


                YggdrafoliumEntity sword = new YggdrafoliumEntity(player.level(), player, targetPos, damage);
                sword.setPos(player.getX(), player.getY() + 1, player.getZ());
                sword.faceTarget(1.0F);
                sword.setColor(0x08e8de);
                sword.setGravity(0F);
                sword.setDeltaMovement(sword.getDeltaMovement().scale(2));
                sword.setExplosive(false);


                player.level().addFreshEntity(sword);


                player.level().playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.ARROW_SHOOT,
                        SoundSource.NEUTRAL, 1.0F, 0.5F);
            }
        }
    }


    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return ToolCommons.damageItemIfPossible(stack, amount, entity, MANA_PER_DAMAGE);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.success(itemstack);
    }


    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        RelicImpl.addDefaultTooltip(pStack, pTooltipComponents);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player) {

            if (stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, MANA_PER_DAMAGE * 2, true))
                stack.setDamageValue(stack.getDamageValue() - 1);

            //Relic Handler
            var relic = XplatAbstractions.INSTANCE.findRelic(stack);
            if (relic != null) {
                relic.tickBinding(player);
            }
        }

    }


    @Override
    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return Integer.MAX_VALUE;
    }


}
