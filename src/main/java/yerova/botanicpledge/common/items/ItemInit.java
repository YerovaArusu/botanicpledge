package yerova.botanicpledge.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.common.blocks.BlockInit;
import yerova.botanicpledge.common.items.block_items.ManaYggdralBufferBlockItem;
import yerova.botanicpledge.common.items.block_items.RitualCenterBlockItem;
import yerova.botanicpledge.common.items.block_items.RitualPedestalBlockItem;
import yerova.botanicpledge.common.items.protectors.GaiaProtector;
import yerova.botanicpledge.common.items.protectors.ManaProtector;
import yerova.botanicpledge.common.items.protectors.TerraProtector;
import yerova.botanicpledge.common.items.protectors.YggdralProtector;
import yerova.botanicpledge.common.items.relic.MariasCore;
import yerova.botanicpledge.common.items.relic.MarinasCore;
import yerova.botanicpledge.common.items.relic.YggdRamus;
import yerova.botanicpledge.setup.BotanicPledge;

public class ItemInit {
    //Custom Rarity
    public static final Rarity UNIQUE = Rarity.create("Unique", ChatFormatting.AQUA);

    //Item Registry
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BotanicPledge.MOD_ID);


    //Protectors
    public static final RegistryObject<Item> MANA_PROTECTOR = ITEMS.register("mana_protector", () -> new ManaProtector(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON).stacksTo(1)));

    public static final RegistryObject<Item> TERRA_PROTECTOR = ITEMS.register("terra_protector", () -> new TerraProtector(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.RARE).stacksTo(1)));

    public static final RegistryObject<Item> GAIA_PROTECTOR = ITEMS.register("gaia_protector", () -> new GaiaProtector(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.EPIC).stacksTo(1)));

    public static final RegistryObject<Item> YGGDRAL_PROTECTOR = ITEMS.register("yggdral_protector", () -> new YggdralProtector(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(ItemInit.UNIQUE).stacksTo(1)));

    //weapons

    public static final RegistryObject<Item> YGGD_RAMUS = ITEMS.register("yggd_ramus", () -> new YggdRamus(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(ItemInit.UNIQUE).stacksTo(1)));


    //cores
    public static final RegistryObject<Item> MARIAS_CORE = ITEMS.register("marias_core", () -> new MariasCore(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(ItemInit.UNIQUE).stacksTo(1)));

    public static final RegistryObject<Item> MARINAS_CORE = ITEMS.register("marinas_core", () -> new MarinasCore(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(ItemInit.UNIQUE).stacksTo(1)));

    //crystals
    public static RegistryObject<Item> RED_CRYSTAL = ITEMS.register("red_crystal", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> BLUE_CRYSTAL = ITEMS.register("blue_crystal", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> GREEN_CRYSTAL = ITEMS.register("green_crystal", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> BLACK_CRYSTAL = ITEMS.register("black_crystal", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> WHITE_CRYSTAL = ITEMS.register("white_crystal", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> YELLOW_CRYSTAL = ITEMS.register("yellow_crystal", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> ORANGE_CRYSTAL = ITEMS.register("orange_crystal", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> TURQUOISE_CRYSTAL = ITEMS.register("turquoise_crystal", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    //Items
    public static final RegistryObject<Item> YGGDRALIUM_INGOT = ITEMS.register("yggdralium_ingot", () -> new YggdraliumIngot(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> YGGDRALIUM_SHARD = ITEMS.register("yggdralium_shard", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> YGGDRALIUM_NUGGET = ITEMS.register("yggdralium_nugget", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    public static RegistryObject<Item> WORLD_ASH_BRANCH = ITEMS.register("world_ash_branch", () -> new Item(
            new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant().rarity(Rarity.COMMON)));

    //BlockItems
    public static final RegistryObject<Item> MANA_YGGDRAL_BUFFER_BLOCK_ITEM = ITEMS.register("mana_yggdral_buffer_block_item",
            () -> new ManaYggdralBufferBlockItem(BlockInit.MANA_YGGDRAL_BUFFER.get(), new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant()));

    public static final RegistryObject<Item> RITUAL_CORE_BLOCK_ITEM = ITEMS.register("ritual_center_block_item",
            () -> new RitualCenterBlockItem(BlockInit.RITUAL_CENTER.get(), new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant()));

    public static final RegistryObject<Item> RITUAL_PEDESTAL_BLOCK_ITEM = ITEMS.register("ritual_pedestal_block_item",
            () -> new RitualPedestalBlockItem(BlockInit.RITUAL_PEDESTAL.get(), new Item.Properties().tab(BotanicPledgeTab.BOTANIC_PLEDGE_TAB).fireResistant()));
}
