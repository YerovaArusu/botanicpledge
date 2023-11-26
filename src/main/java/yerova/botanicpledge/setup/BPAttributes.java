package yerova.botanicpledge.setup;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

public class BPAttributes {

    public static final UUID BASE_MANA_DAMAGE_UUID = UUID.fromString("6c8f773a-900c-47c4-a4b6-fbdec9f5753d");

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, BotanicPledge.MOD_ID);

    public static final RegistryObject<Attribute> MANA_DAMAGE = ATTRIBUTES.register("mana_damage",
            ()-> (Attribute) new RangedAttribute(BotanicPledge.MOD_ID + ".attribute.mana_damage", 0, 0, 1024).setSyncable(true));

    public static final RegistryObject<Attribute> MANA_DEFENSE = ATTRIBUTES.register("mana_defense",
            ()-> (Attribute) new RangedAttribute(BotanicPledge.MOD_ID + ".attribute.mana_defense", 0, 0, 1024).setSyncable(true));


}
