package yerova.botanicpledge.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends BlockTagsProvider {
    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BotanicPledge.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        for (RegistryObject<Block> b : BPBlocks.BLOCKS.getEntries()) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(b.get());
            this.tag(BlockTags.NEEDS_IRON_TOOL).add(b.get());
        }

        this.tag(BlockTags.LOGS_THAT_BURN).add(BPBlocks.STRIPPED_YGGDRASIL_LOG.get(), BPBlocks.STRIPPED_YGGDRASIL_WOOD.get(), BPBlocks.YGGDRASIL_WOOD.get(), BPBlocks.YGGDRASIL_LOG.get());
        this.tag(BlockTags.LOGS).add(BPBlocks.STRIPPED_YGGDRASIL_LOG.get(),BPBlocks.YGGDRASIL_LOG.get(),BPBlocks.YGGDRASIL_WOOD.get(),BPBlocks.STRIPPED_YGGDRASIL_WOOD.get());

        this.tag(BlockTags.PLANKS).add(BPBlocks.YGGDRASIL_PLANKS.get());
        this.tag(BlockTags.LEAVES).add(BPBlocks.YGGDRASIL_LEAVES.get());
        this.tag(BlockTags.FENCES).add(BPBlocks.YGGDRASIL_FENCE.get());
        this.tag(BlockTags.FENCE_GATES).add(BPBlocks.YGGDRASIL_FENCE_GATE.get());
        this.tag(BlockTags.WALLS).add(BPBlocks.YGGDRASIL_WALL.get());

    }
}
