package yerova.botanicpledge.common.blocks;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import yerova.botanicpledge.common.blocks.block_entities.BlockEntityInit;
import yerova.botanicpledge.common.items.BotanicPledgeTab;
import yerova.botanicpledge.common.items.ItemInit;
import yerova.botanicpledge.setup.BotanicPledge;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BotanicPledge.MOD_ID);

    public static final RegistryObject<Block> MANA_YGGDRAL_BUFFER = registerBlockWithoutBlockItem("mana_yggdral_buffer",
            () -> new ManaYggdralBufferBlock(BlockBehaviour.Properties.copy(ModBlocks.runeAltar).noOcclusion()));

    //Flower
    public static final RegistryObject<Block> THUNDER_LILY  = registerBlock("thunder_lily", () ->
            new BlockSpecialFlower(MobEffects.ABSORPTION,10, BlockBehaviour.Properties.copy(Blocks.POPPY), BlockEntityInit.THUNDER_LILY_BLOCK_ENTITY::get), BotanicPledgeTab.BOTANIC_PLEDGE_TAB);


    //Ritual Blocks
    public static final RegistryObject<Block> RITUAL_CENTER = registerBlockWithoutBlockItem("ritual_center",
            () -> new RitualCenterBlock(BlockBehaviour.Properties.copy(ModBlocks.runeAltar).noOcclusion()));

    public static final RegistryObject<Block> RITUAL_PEDESTAL = registerBlockWithoutBlockItem("ritual_pedestal",
            () -> new RitualPedestalBlock(BlockBehaviour.Properties.copy(ModBlocks.runeAltar).noOcclusion()));

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
        return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)) {
            @Override
            public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                pTooltip.add(new TranslatableComponent(tooltipKey));
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
        return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }
}
