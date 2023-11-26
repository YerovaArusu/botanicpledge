package yerova.botanicpledge.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import yerova.botanicpledge.setup.BPEnchantments;

public class ManaEdgeEnchantment extends Enchantment {
    public ManaEdgeEnchantment(Rarity pRarity, EquipmentSlot... pApplicableSlots) {
        super(pRarity, EnchantmentCategory.BREAKABLE, pApplicableSlots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    public static float getManaDamageBonus(ItemStack stack) {
        float dmg = 0;
        int lvl = EnchantmentHelper.getItemEnchantmentLevel(BPEnchantments.MANA_EDGE_ENCHANTMENT.get(), stack);
        if (lvl > 0) dmg = lvl * 0.75f;
        return dmg;
    }


}
