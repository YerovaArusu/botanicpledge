package yerova.botanicpledge.setup;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.loot.modifier.AddItemModifier;
import yerova.botanicpledge.loot.modifier.AddSpecialModifier;

public class BPLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS
            = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BotanicPledge.MOD_ID);


    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_SPECIAL_ITEM =
            LOOT_MODIFIERS.register("add_special_item", AddSpecialModifier.CODEC);



}
