package yerova.botanicpledge.setup;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.aura_node.essence.Essence;

public class    BPEssences {

    public static final ResourceKey<Registry<Essence>> ESSENCES_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(BotanicPledge.MOD_ID, "essence"));

    public static final DeferredRegister<Essence> ESSENCES =
            DeferredRegister.create(ESSENCES_KEY, BotanicPledge.MOD_ID);

    public static final IForgeRegistry<Essence> ESSENCE_REGISTRY = ESSENCES.makeRegistry(
            () -> new RegistryBuilder<Essence>().disableSaving()
    ).get();

    // Empty Dummy to avoid Errors
    public static final RegistryObject<Essence> EMPTY_ESSENCE = ESSENCES.register("empty", () -> new Essence(ItemStack.EMPTY.getItem(), 0x000000));

    public static final RegistryObject<Essence> ASGARD_ESSENCE = ESSENCES.register("asgard_essence",
            () -> new Essence(BPItems.ASGARD_ESSENCE.get(), 0xFFD700));

    public static final RegistryObject<Essence> VANAHEIM_ESSENCE = ESSENCES.register("vanaheim_essence",
            () -> new Essence(BPItems.VANAHEIM_ESSENCE.get(), 0x7CFC00));

    public static final RegistryObject<Essence> ALFHEIM_ESSENCE = ESSENCES.register("alfheim_essence",
            () -> new Essence(BPItems.ALFHEIM_ESSENCE.get(), 0xF8F8FF));

    public static final RegistryObject<Essence> MIDGARD_ESSENCE = ESSENCES.register("midgard_essence",
            () -> new Essence(BPItems.MIDGARD_ESSENCE.get(), 0x228B22));

    public static final RegistryObject<Essence> JOTUNHEIM_ESSENCE = ESSENCES.register("jotunheim_essence",
            () -> new Essence(BPItems.JOTUNHEIM_ESSENCE.get(), 0x708090));

    public static final RegistryObject<Essence> SVARTALFHEIM_ESSENCE = ESSENCES.register("svartalfheim_essence",
            () -> new Essence(BPItems.SVARTALFHEIM_ESSENCE.get(), 0x2F4F4F));

    public static final RegistryObject<Essence> NIFLHEIM_ESSENCE = ESSENCES.register("niflheim_essence",
            () -> new Essence(BPItems.NIFLHEIM_ESSENCE.get(), 0xADD8E6));

    public static final RegistryObject<Essence> MUSPELHEIM_ESSENCE = ESSENCES.register("muspelheim_essence",
            () -> new Essence(BPItems.MUSPELHEIM_ESSENCE.get(), 0xFF4500));

    public static final RegistryObject<Essence> HELHEIM_ESSENCE = ESSENCES.register("helheim_essence",
            () -> new Essence(BPItems.HELHEIM_ESSENCE.get(), 0x4B0082));


}
