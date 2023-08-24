package yerova.botanicpledge.setup;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.enchantments.RuneCollectorEnchantment;

public class BPEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, BotanicPledge.MOD_ID);

    public static final RegistryObject<Enchantment> RUNE_COLLECTOR_ENCHANTMENT = ENCHANTMENTS.register("rune_collector",
            () -> new RuneCollectorEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
}
