package yerova.botanicpledge.common.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.bauble.ManaseerMonocleItem;
import yerova.botanicpledge.client.render.entities.ShieldBlockEntityWithoutLevelRenderer;

import java.util.function.Consumer;

public class ManaShield extends ShieldItem {

    public static final int MANA_PER_DAMAGE = 30;

    public ManaShield(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ShieldBlockEntityWithoutLevelRenderer.instance;
            }
        });
    }

    @SoftImplement("IForgeItem")
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @SoftImplement("IForgeItem")
    private boolean reequipAnimation(ItemStack before, ItemStack after) {
        return !after.is(this);
    }


    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.is(BotaniaItems.manaSteel);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 7200;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player) {
            if (!world.isClientSide && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, MANA_PER_DAMAGE * 8, true)) {
                stack.setDamageValue(stack.getDamageValue() - 1);
            }
        }
    }

}
