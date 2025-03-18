package yerova.botanicpledge.setup;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.aura_node.essence.Essence;

public class BPEssences {

    public static final ResourceKey<Registry<Essence>> ESSENCES_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(BotanicPledge.MOD_ID, "essence"));

    public static final DeferredRegister<Essence> ESSENCES =
            DeferredRegister.create(ESSENCES_KEY, BotanicPledge.MOD_ID);

    public static final IForgeRegistry<Essence> ESSENCE_REGISTRY = ESSENCES.makeRegistry(
            () -> new RegistryBuilder<Essence>().disableSaving()
    ).get();



    public static final RegistryObject<Essence> AIR_ESSENCE = ESSENCES.register("air",
            () -> new Essence(Items.FEATHER, 0x87CEEB));

    public static final RegistryObject<Essence> FIRE_ESSENCE = ESSENCES.register("fire",
            () -> new Essence(Items.BLAZE_POWDER, 0xFF4500));
}
