package yerova.botanicpledge.common.enchantments;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import yerova.botanicpledge.common.items.RuneGemItem;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.setup.BPItems;

public class RuneCollectorEnchantment extends Enchantment {


    public RuneCollectorEnchantment(Rarity pRarity, EquipmentSlot... pApplicableSlots) {
        super(pRarity, EnchantmentCategory.WEAPON, pApplicableSlots);
    }


    @Override
    public int getMaxLevel() {
        return 5;
    }


    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity entity, int pLevel) {
        int bound = pLevel > 1 ? 10_000 - (pLevel - 1) * 800 : 10_000;

        if (!entity.isAlive() && entity instanceof Zombie && pAttacker instanceof Player player && !player.level().isClientSide &&
                RuneGemItem.getRandomRarity(bound).equals(BPConstants.RARITY_UNCOMMON) && player.getUUID() != null) {

            player.level().addFreshEntity(new ItemEntity(player.level(), entity.getX(), entity.getY(), entity.getZ(),
                    RuneGemItem.getNewAttributedGemStack()));
        }
        super.doPostAttack(pAttacker, entity, pLevel);
    }


    @Override
    public boolean isTreasureOnly() {
        return true;
    }


}
