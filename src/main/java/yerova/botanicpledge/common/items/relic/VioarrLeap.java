package yerova.botanicpledge.common.items.relic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.relic.RelicImpl;
import vazkii.botania.xplat.XplatAbstractions;
import yerova.botanicpledge.setup.BPItemTiers;

import java.util.List;

public class VioarrLeap extends SwordItem {
    public VioarrLeap(int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(BPItemTiers.YGGDRALIUM_TIER, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        MinecraftForge.EVENT_BUS.addListener(this::onLeftClick);
    }

    public void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {

        Player player = event.getEntity();

        if (player.getItemInHand(event.getHand()).getItem() instanceof VioarrLeap && !player.isCrouching() && ManaItemHandler.instance().requestManaExactForTool(player.getMainHandItem(), player, 500, true)) {

            Vec3 playerLook = player.getViewVector(1).multiply(2,2,2);
            Vec3 dashVec = new Vec3(playerLook.x(), playerLook.y(), playerLook.z());
            player.setDeltaMovement(dashVec);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player && player.getMainHandItem().equals(stack)) {
            if (!pLevel.isClientSide) {


                //Relic Handler
                var relic = XplatAbstractions.INSTANCE.findRelic(stack);
                if (relic != null) {
                    relic.tickBinding(player);
                }
            }
        }
        super.inventoryTick(stack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        RelicImpl.addDefaultTooltip(pStack, pTooltipComponents);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {

        if (!player.isCrouching() && ManaItemHandler.instance().requestManaExactForTool(player.getMainHandItem(), player, 500, true)) {
            Vec3 playerLook = player.getViewVector(1).multiply(2,2,2);
            Vec3 dashVec = new Vec3(playerLook.x(), playerLook.y(), playerLook.z());
            player.setDeltaMovement(dashVec);
        }

        return super.onLeftClickEntity(stack, player, entity);
    }


    public static Relic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
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
