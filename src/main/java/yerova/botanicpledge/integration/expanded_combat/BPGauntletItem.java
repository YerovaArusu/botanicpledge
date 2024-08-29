package yerova.botanicpledge.integration.expanded_combat;

import com.userofbricks.expanded_combat.api.material.Material;
import com.userofbricks.expanded_combat.item.ECGauntletItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import vazkii.botania.api.mana.ManaItemHandler;

import static yerova.botanicpledge.common.items.TerraShield.MANA_PER_DAMAGE;

public class BPGauntletItem extends ECGauntletItem {
    public BPGauntletItem(Properties properties, Material materialIn) {
        super(properties, materialIn);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        Level world = entity.level();
        if (entity instanceof Player player) {
            if (!world.isClientSide && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, MANA_PER_DAMAGE * 4, true)) {
                stack.setDamageValue(stack.getDamageValue() - 1);
            }
        }


        super.curioTick(slotContext, stack);
    }
}
