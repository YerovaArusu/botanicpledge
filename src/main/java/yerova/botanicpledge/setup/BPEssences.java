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

    public static final RegistryObject<Essence> AIR_ESSENCE = ESSENCES.register("air",
            () -> new Essence(Items.FEATHER, 0x87CEEB));

    public static final RegistryObject<Essence> FIRE_ESSENCE = ESSENCES.register("fire",
            () -> new Essence(Items.BLAZE_POWDER, 0xFF4500));

    public static final RegistryObject<Essence> EARTH_ESSENCE = ESSENCES.register("earth",
            () -> new Essence(Items.DIRT, 0x8B4513)); // Brown (Earthy)

    public static final RegistryObject<Essence> WATER_ESSENCE = ESSENCES.register("water",
            () -> new Essence(Items.WATER_BUCKET, 0x1E90FF)); // Blue (Deep Water)

    public static final RegistryObject<Essence> LIGHT_ESSENCE = ESSENCES.register("light",
            () -> new Essence(Items.GLOWSTONE_DUST, 0xFFFFE0)); // Light Yellow (Glowing)

    public static final RegistryObject<Essence> DARK_ESSENCE = ESSENCES.register("dark",
            () -> new Essence(Items.ENDER_PEARL, 0x4B0082)); // Dark Purple (Mystic)

    public static final RegistryObject<Essence> LIFE_ESSENCE = ESSENCES.register("life",
            () -> new Essence(Items.APPLE, 0x00FF00)); // Green (Life Essence)

    public static final RegistryObject<Essence> DEATH_ESSENCE = ESSENCES.register("death",
            () -> new Essence(Items.BONE, 0x696969)); // Gray (Decay)

    public static final RegistryObject<Essence> ARCANE_ESSENCE = ESSENCES.register("arcane",
            () -> new Essence(Items.BOOK, 0x9370DB)); // Lavender (Magical Essence)

    public static final RegistryObject<Essence> VOID_ESSENCE = ESSENCES.register("void",
            () -> new Essence(Items.OBSIDIAN, 0x000000)); // Black (Abyssal Power)

}
