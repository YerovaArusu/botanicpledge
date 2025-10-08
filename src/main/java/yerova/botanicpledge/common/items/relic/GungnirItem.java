package yerova.botanicpledge.common.items.relic;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.List;

public class GungnirItem extends TridentItem {



    public GungnirItem(Properties props) {
        super(props);
    }


    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && entity instanceof Player player) {
            var relic = XplatAbstractions.INSTANCE.findRelic(stack);
            if (relic != null) {
                relic.tickBinding(player);
            }
        }
    }


    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            ThrownTrident throwntrident = new ThrownTrident(pLevel, player, pStack);
            throwntrident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float)1 * 0.5F, 1.0F);
            if (player.getAbilities().instabuild) {
                throwntrident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            pLevel.addFreshEntity(throwntrident);
            pLevel.playSound((Player)null, throwntrident, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild) {
                player.getInventory().removeItem(pStack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flags) {
        RelicImpl.addDefaultTooltip(stack, tooltip);
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }
}
