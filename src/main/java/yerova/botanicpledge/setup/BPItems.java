package yerova.botanicpledge.setup;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.items.*;
import yerova.botanicpledge.common.items.relic.*;

public class BPItems {
    //Custom Rarity
    public static final Rarity UNIQUE = Rarity.create("Unique", ChatFormatting.AQUA);

    //Item Registry
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BotanicPledge.MOD_ID);

    //weapons
    public static final RegistryObject<Item> YGGD_RAMUS = ITEMS.register("yggd_ramus", () -> new YggdRamus(
            new Item.Properties().fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)
    ));

    public static final RegistryObject<Item> ULL_BOW = ITEMS.register("ull_bow", () -> new UllBow(
            new Item.Properties().fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)
    ));


    public static final RegistryObject<Item> ASGARD_FRACTAL = ITEMS.register("asgard_fractal", () -> new AsgardFractal(23, 1,
            new Item.Properties().fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)
    ));

    public static final RegistryObject<Item> FIRST_RELIC = ITEMS.register("first_relic", () -> new FirstRelic(
            new Item.Properties().fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)
    ));


    //cores
    public static final RegistryObject<Item> MARIAS_CORE = ITEMS.register("vedrfolnir_core", () -> new VedrfolnirsCore(
            new Item.Properties().fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)));



    public static final RegistryObject<TerraShield> TERRA_SHIELD = ITEMS.register("terra_shield",
            ()-> new TerraShield((new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON).stacksTo(1)).durability(660)));

    public static final RegistryObject<ManaShield> MANA_SHIELD = ITEMS.register("mana_shield",
            ()-> new ManaShield((new Item.Properties().rarity(Rarity.COMMON).stacksTo(1)).durability(330)));

    public static final RegistryObject<Item> SOUL_AMULET = ITEMS.register("soul_amulet", () -> new SoulAmulet(
            new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> SOUL_Slicer = ITEMS.register("soul_slicer", () -> new SoulSlicer(
            new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> SOUL_SHARD = ITEMS.register("soul_shard", () -> new SoulShard(
            new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> CONQUERING_SASH = ITEMS.register("conquering_sash", () -> new ConqueringSashItem(
            new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> AESIR_RING = ITEMS.register("aesir_ring", () -> new RingOfAesir(
            new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    //Items
    public static final RegistryObject<Item> YGGDRALIUM_INGOT = ITEMS.register("yggdralium_ingot", () -> new Item(
            new Item.Properties().fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> YGGDRALIUM_SHARD = ITEMS.register("yggdralium_shard", () -> new Item(
            new Item.Properties().fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> YGGDRALIUM_NUGGET = ITEMS.register("yggdralium_nugget", () -> new Item(
            new Item.Properties().fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> WORLD_ASH_BRANCH = ITEMS.register("world_ash_branch", () -> new Item(
            new Item.Properties().fireResistant().rarity(Rarity.COMMON)));


    public static RegistryObject<Item> SOCKET_GEM = ITEMS.register("rune_gem", () -> new RuneGemItem(
            new Item.Properties().fireResistant().rarity(Rarity.RARE)));


}
