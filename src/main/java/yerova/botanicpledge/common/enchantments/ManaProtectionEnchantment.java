package yerova.botanicpledge.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import yerova.botanicpledge.setup.BPEnchantments;

public class ManaProtectionEnchantment extends Enchantment{
    public ManaProtectionEnchantment(Enchantment.Rarity pRarity, EquipmentSlot... pApplicableSlots) {
        super(pRarity, EnchantmentCategory.ARMOR, pApplicableSlots);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }



    public static float getManaDamageProtection(ItemStack stack) {
        float protection = 0;
        int lvl = EnchantmentHelper.getItemEnchantmentLevel(BPEnchantments.MANA_EDGE_ENCHANTMENT.get(), stack);
        if (lvl > 0) protection = lvl * 0.1f;
        return protection;
    }
}
