package yerova.botanicpledge.setup;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.item.material.RuneItem;
import yerova.botanicpledge.common.items.*;
import yerova.botanicpledge.common.items.armor.YggdrasilsteelArmor;
import yerova.botanicpledge.common.items.armor.YggdrasilsteelHelmet;
import yerova.botanicpledge.common.items.block_items.YggdrasilPylonItem;
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


    public static final RegistryObject<Item> ASGARD_FRACTAL = ITEMS.register("yggdrasil_warden", () -> new AsgardFractal(23, 1,
            new Item.Properties().fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)
    ));

    public static final RegistryObject<Item> FIRST_RELIC = ITEMS.register("first_relic", () -> new FirstRelic(
            new Item.Properties().fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)
    ));


    //cores
    public static final RegistryObject<Item> MARIAS_CORE = ITEMS.register("vedrfolnir_core", () -> new VedrfolnirsCore(
            new Item.Properties().fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)));


    public static final RegistryObject<TerraShield> TERRA_SHIELD = ITEMS.register("terra_shield",
            () -> new TerraShield((new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON).stacksTo(1)).durability(660)));

    public static final RegistryObject<ManaShield> MANA_SHIELD = ITEMS.register("mana_shield",
            () -> new ManaShield((new Item.Properties().rarity(Rarity.COMMON).stacksTo(1)).durability(330)));

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

    public static final RegistryObject<Item> YGGDRASIL_MONOCLE = ITEMS.register("yggdrasil_monocle", () -> new YggdrasilMonocle(
            new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> YGGDRASIL_HELMET = ITEMS.register("yggdrasilsteel_helmet",
            () -> new YggdrasilsteelHelmet(new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> YGGDRASIL_CHESTPLATE = ITEMS.register("yggdrasilsteel_chestplate",
            () -> new YggdrasilsteelArmor(ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> YGGDRASIL_LEGGINGS = ITEMS.register("yggdrasilsteel_leggings",
            () -> new YggdrasilsteelArmor(ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> YGGDRASIL_BOOTS = ITEMS.register("yggdrasilsteel_boots",
            () -> new YggdrasilsteelArmor(ArmorItem.Type.BOOTS, new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)));


    public static final RegistryObject<Item> YGGDRASILSTEEL_BAND_OF_MANA = ITEMS.register("yggdrasilsteel_band_of_mana", () -> new YggdrasilsteelBandOfMana(
            new Item.Properties().fireResistant().rarity(Rarity.COMMON).stacksTo(1)));

    public static final RegistryObject<Item> YGGDRASILSTEEL_BAND_OF_AURA = ITEMS.register("yggdrasilsteel_band_of_aura", () -> new YggdrasilsteelBandOfAura(
            new Item.Properties().fireResistant().rarity(Rarity.COMMON).stacksTo(1)));

    //Items
    public static final RegistryObject<Item> YGGDRASIL_HEART = ITEMS.register("yggdrasil_heart", () -> new YggdrasilHeart(
            new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)   
    ));
    public static final RegistryObject<Item> YGGDRASIL_TEMPLATE = ITEMS.register("yggdrasilsteel_template", () -> new YggdrasilHeart(
            new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)
    ));

    public static final RegistryObject<Item> YGGDRALIUM_INGOT = ITEMS.register("yggdrasilsteel_ingot", () -> new Yggdrasilsteel(
            new Item.Properties().fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> YGGDRALIUM_SHARD = ITEMS.register("yggdrasil_shard", () -> new Item(
            new Item.Properties().fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> YGGDRALIUM_NUGGET = ITEMS.register("yggdrasilsteel_nugget", () -> new Item(
            new Item.Properties().fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> WORLD_ASH_BRANCH = ITEMS.register("world_ash_branch", () -> new Item(
            new Item.Properties().fireResistant().rarity(Rarity.COMMON)));


    public static RegistryObject<Item> SOCKET_GEM = ITEMS.register("rune_gem", () -> new RuneGemItem(
            new Item.Properties().fireResistant().rarity(Rarity.RARE)));

    public static RegistryObject<Item> YGGDRASIL_PYLON = ITEMS.register("yggdrasil_pylon",
            () -> new YggdrasilPylonItem(new Item.Properties()));


    //Runes
    public static RegistryObject<Item> YGGDRASIL_RUNE = ITEMS.register("rune_yr", () -> new RuneItem(new Item.Properties()));
    public static RegistryObject<Item> LOKI_RUNE = ITEMS.register("rune_laguz", () -> new RuneItem(new Item.Properties()));
    public static RegistryObject<Item> ODIN_RUNE = ITEMS.register("rune_ansuz", () -> new RuneItem(new Item.Properties()));
    public static RegistryObject<Item> THOR_RUNE = ITEMS.register("rune_thurisaz", () -> new RuneItem(new Item.Properties()));
    public static RegistryObject<Item> ULL_RUNE = ITEMS.register("rune_ingwaz", () -> new RuneItem(new Item.Properties()));

}
