package yerova.botanicpledge.setup;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.block.BotaniaBlock;
import vazkii.botania.common.block.BotaniaBlockSetTypes;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.decor.stairs.BotaniaStairBlock;
import vazkii.botania.forge.block.ForgeSpecialFlowerBlock;
import yerova.botanicpledge.common.blocks.*;
import yerova.botanicpledge.common.items.BotanicPledgeTab;
import yerova.botanicpledge.common.worldgen.tree.YggdrasilTreeGrower;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

import static vazkii.botania.common.block.BotaniaBlocks.livingwood;

public class BPBlocks {
    private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> NO_SPAWN = (state, world, pos, et) -> false;


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BotanicPledge.MOD_ID);

    public static final RegistryObject<Block> YGGDRAL_SPREADER = registerBlock("yggdral_spreader",
            () -> new YggdralSpreader(YggdralSpreader.Variant.YGGDRAL, BlockBehaviour.Properties.copy(livingwood).isValidSpawn(NO_SPAWN)));

    public static final RegistryObject<Block> MANA_BUFFER = registerBlock("mana_buffer",
            () -> new ManaBufferBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> RITUAL_CENTER = registerBlock("ritual_center",
            () -> new RitualCenterBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> ORE_INFUSION = registerBlock("ore_infusion",
            () -> new OreInfusionBlock(BlockBehaviour.Properties.copy(livingwood).noOcclusion()));


    public static final RegistryObject<Block> YGGDRALIUM_BLOCK = registerBlock("yggdrasilsteel_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(9f).requiresCorrectToolForDrops()));


    public static final RegistryObject<Block> RITUAL_PEDESTAL = registerBlock("ritual_pedestal",
            () -> new RitualPedestalBlock(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> MODIFICATION_TABLE = registerBlock("modification_altar",
            () -> new ModificationAltarBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)));


    //Flower
    public static final RegistryObject<Block> THUNDER_LILY = registerBlock("thunder_lily", () ->
            new ForgeSpecialFlowerBlock(MobEffects.ABSORPTION, 10, BlockBehaviour.Properties.copy(Blocks.POPPY), BPBlockEntities.THUNDER_LILY_BLOCK_ENTITY::get));


    public static final RegistryObject<Block> YGGDRASIL_PYLON = registerBlockWithoutBlockItem("yggdrasil_pylon", () ->
            new YggdrasilPylon(BlockBehaviour.Properties.copy(BotaniaBlocks.gaiaPylon)));


    //World Ash
    public static final RegistryObject<Block> YGGDRASIL_SAPLING = registerBlock("yggdrasil_sapling", () ->
            new SaplingBlock(new YggdrasilTreeGrower(),BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));


    public static final RegistryObject<Block> YGGDRASIL_LOG = registerBlock("yggdrasil_log", () ->
            new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(3f)));

    public static final RegistryObject<Block> YGGDRASIL_WOOD = registerBlock("yggdrasil", () ->
            new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).strength(3f)));

    public static final RegistryObject<Block> STRIPPED_YGGDRASIL_LOG = registerBlock("stripped_yggdrasil_log", () ->
            new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).strength(3f)));

    public static final RegistryObject<Block> STRIPPED_YGGDRASIL_WOOD = registerBlock("stripped_yggdrasil", () ->
            new FlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD).strength(3f)));

    public static final RegistryObject<Block> YGGDRASIL_PLANKS = registerBlock("yggdrasil_planks", () ->
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2f)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });

    public static final RegistryObject<Block> YGGDRASIL_LEAVES = registerBlock("yggdrasil_leaves", () ->
            new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).noLootTable()) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });

    public static final RegistryObject<StairBlock> YGGDRASIL_STAIRS = registerBlock("yggdrasil_stairs",
            () -> new StairBlock(() -> BPBlocks.YGGDRASIL_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<SlabBlock> YGGDRASIL_SLAB = registerBlock("yggdrasil_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<FenceBlock> YGGDRASIL_FENCE = registerBlock("yggdrasil_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));
    public static final RegistryObject<FenceGateBlock> YGGDRASIL_FENCE_GATE = registerBlock("yggdrasil_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<WallBlock> YGGDRASIL_WALL = registerBlock("yggdrasil_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<DoorBlock> YGGDRASIL_DOOR = registerBlock("yggdrasil_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST).noOcclusion(), BotaniaBlockSetTypes.LIVINGWOOD.setType()));
    public static final RegistryObject<TrapDoorBlock> YGGDRASIL_TRAPDOOR = registerBlock("yggdrasil_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST).noOcclusion(), BotaniaBlockSetTypes.LIVINGWOOD.setType()));




    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, String tooltipKey) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tooltipKey);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, String tooltipKey) {
        return BPItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()) {
            @Override
            public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                pTooltip.add(Component.translatable(tooltipKey));
            }
        });
    }



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return BPItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }
}
