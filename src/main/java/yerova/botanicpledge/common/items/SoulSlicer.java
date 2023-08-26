package yerova.botanicpledge.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.setup.BPItemTiers;

import java.util.List;

public class SoulSlicer extends SwordItem {
    public SoulSlicer(Properties pProperties) {
        super(BPItemTiers.SOUL_TIER,1,1,pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TextComponent("Single-Use only!").withStyle(ChatFormatting.DARK_RED));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return  0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(!pLevel.isClientSide) {
            pLevel.addFreshEntity(new ItemEntity(pLevel, pPlayer.getX(), pPlayer.getY(),pPlayer.getZ(), SoulShard.createSoulShard(pPlayer)));
            pPlayer.sendMessage(new TextComponent("Obtained a soul shard of your own").withStyle(ChatFormatting.AQUA), pPlayer.getUUID());
            pPlayer.hurt(DamageSource.indirectMagic(pPlayer, pPlayer), getDamage());
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {

        if(entity instanceof Player enemy) {
            player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(),player.getZ(), SoulShard.createSoulShard(enemy)));
            player.sendMessage(new TextComponent("Obtained a soul shard from " + enemy.getUUID()).withStyle(ChatFormatting.AQUA), player.getUUID());
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
