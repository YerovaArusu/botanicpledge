package yerova.botanicpledge.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BotanicPledge;

public class BPBlockStateProvider extends BlockStateProvider {

    public BPBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BotanicPledge.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Simple Block
        simpleBlock(BPBlocks.YGGDRALIUM_BLOCK.get());


        // Stairs
        stairsBlock(BPBlocks.YGGDRASIL_STAIRS.get(), modLoc("block/yggdrasil_planks"));

        // Slab
        slabBlock(BPBlocks.YGGDRASIL_SLAB.get(), modLoc("block/yggdrasil_planks"), modLoc("block/yggdrasil_planks"));

        // Fence
        fenceBlock(BPBlocks.YGGDRASIL_FENCE.get(), modLoc("block/yggdrasil_planks"));
        fenceGateBlock(BPBlocks.YGGDRASIL_FENCE_GATE.get(), modLoc("block/yggdrasil_planks"));

        // Wall
        wallBlock(BPBlocks.YGGDRASIL_WALL.get(), modLoc("block/yggdrasil_planks"));

        // Door
        doorBlockWithRenderType(BPBlocks.YGGDRASIL_DOOR.get(), modLoc("block/yggdrasil_door_bottom"), modLoc("block/yggdrasil_door_top"), "cutout");

        // Trapdoor
        trapdoorBlock(BPBlocks.YGGDRASIL_TRAPDOOR.get(), modLoc("block/yggdrasil_trapdoor"), true);

        // Sapling
        saplingBlock(BPBlocks.YGGDRASIL_SAPLING);
    }

    public ResourceLocation modLoc(String path) {
        return new ResourceLocation(BotanicPledge.MOD_ID, path);
    }

    private void saplingBlock(RegistryObject<? extends Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(), blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

}
