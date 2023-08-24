package yerova.botanicpledge.setup;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.items.BotanicPledgeTab;
import yerova.botanicpledge.common.items.RuneGemItem;
import yerova.botanicpledge.common.items.YggdraliumIngot;
import yerova.botanicpledge.common.items.relic.AsgardFractal;
import yerova.botanicpledge.common.items.relic.MariasCore;
import yerova.botanicpledge.common.items.relic.MarinasCore;
import yerova.botanicpledge.common.items.relic.YggdRamus;

public class BPItems {
    //Custom Rarity
    public static final Rarity UNIQUE = Rarity.create("Unique", ChatFormatting.AQUA);

    //Item Registry
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BotanicPledge.MOD_ID);


    //weapons

    public static final RegistryObject<Item> YGGD_RAMUS = ITEMS.register("yggd_ramus", () -> new YggdRamus(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)
    ));

    public static final RegistryObject<Item> ASGARD_FRACTAL = ITEMS.register("asgard_fractal", () -> new AsgardFractal(1,1,
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)
    ));


    //cores
    public static final RegistryObject<Item> MARIAS_CORE = ITEMS.register("marias_core", () -> new MariasCore(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)));

    public static final RegistryObject<Item> MARINAS_CORE = ITEMS.register("marinas_core", () -> new MarinasCore(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(BPItems.UNIQUE).stacksTo(1)));



    //Items
    public static final RegistryObject<Item> YGGDRALIUM_INGOT = ITEMS.register("yggdralium_ingot", () -> new YggdraliumIngot(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> YGGDRALIUM_SHARD = ITEMS.register("yggdralium_shard", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> YGGDRALIUM_NUGGET = ITEMS.register("yggdralium_nugget", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> WORLD_ASH_BRANCH = ITEMS.register("world_ash_branch", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));


    public static RegistryObject<Item> SOCKET_GEM = ITEMS.register("rune_gem", () -> new RuneGemItem(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.RARE)));


}
