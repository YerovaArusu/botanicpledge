package yerova.botanicpledge.setup;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.forge.block.ForgeSpecialFlowerBlock;
import yerova.botanicpledge.common.blocks.*;
import yerova.botanicpledge.common.items.BotanicPledgeTab;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

import static vazkii.botania.common.block.BotaniaBlocks.livingwood;

public class BPBlocks {
    private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> NO_SPAWN = (state, world, pos, et) -> false;


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BotanicPledge.MOD_ID);

    public static final RegistryObject<Block> YGGDRAL_SPREADER = registerBlock("yggdral_spreader",
            () -> new YggdralSpreader(YggdralSpreader.Variant.YGGDRAL, BlockBehaviour.Properties.copy(livingwood).isValidSpawn(NO_SPAWN)), BotanicPledgeTab.BOTANIC_PLEDGE_TAB);

    public static final RegistryObject<Block> MANA_BUFFER = registerBlock("mana_buffer",
            () -> new ManaBufferBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).noOcclusion()), BotanicPledgeTab.BOTANIC_PLEDGE_TAB);

    public static final RegistryObject<Block> RITUAL_CENTER = registerBlock("ritual_center",
            () -> new RitualCenterBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).noOcclusion()), BotanicPledgeTab.BOTANIC_PLEDGE_TAB);


    public static final RegistryObject<Block> RITUAL_PEDESTAL = registerBlock("ritual_pedestal",
            () -> new RitualPedestalBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).noOcclusion()), BotanicPledgeTab.BOTANIC_PLEDGE_TAB);

    public static final RegistryObject<Block> MODIFICATION_TABLE = registerBlock("modification_altar",
            () -> new ModificationAltarBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK)), BotanicPledgeTab.BOTANIC_PLEDGE_TAB);


    //Flower
    public static final RegistryObject<Block> THUNDER_LILY = registerBlock("thunder_lily", () ->
            new ForgeSpecialFlowerBlock(MobEffects.ABSORPTION, 10, BlockBehaviour.Properties.copy(Blocks.POPPY), BPBlockEntities.THUNDER_LILY_BLOCK_ENTITY::get), BotanicPledgeTab.BOTANIC_PLEDGE_TAB);


    public static final RegistryObject<Block> YGGDRALIUM_BLOCK = registerBlock("yggdralium_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(9f).requiresCorrectToolForDrops()),
            BotanicPledgeTab.BOTANIC_PLEDGE_TAB);

    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block,
                                                                     CreativeModeTab tab, String tooltipKey) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab, tooltipKey);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab, String tooltipKey) {
        return BPItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()) {
            @Override
            public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                pTooltip.add(Component.translatable(tooltipKey));
            }
        });
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return BPItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }
}
