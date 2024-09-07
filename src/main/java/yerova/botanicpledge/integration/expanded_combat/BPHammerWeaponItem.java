package yerova.botanicpledge.integration.expanded_combat;

import com.userofbricks.expanded_combat.api.material.Material;
import com.userofbricks.expanded_combat.item.ECHammerWeaponItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.botania.api.mana.ManaItemHandler;

import static yerova.botanicpledge.common.items.TerraShield.MANA_PER_DAMAGE;

public class BPHammerWeaponItem extends ECHammerWeaponItem {
    public BPHammerWeaponItem(Material material, Properties properties) {
        super(material, properties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player) {
            if (!pLevel.isClientSide && pStack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExact(pStack, player, MANA_PER_DAMAGE * 4, true)) {
                pStack.setDamageValue(pStack.getDamageValue() - 1);
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }
}