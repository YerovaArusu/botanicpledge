package yerova.botanicpledge.datagen;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import yerova.botanicpledge.setup.BPBlocks;

import java.util.Set;

public class BlockLootTables extends BlockLootSubProvider {
    public BlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {

        this.createOakLeavesDrops(BPBlocks.YGGDRASIL_LEAVES.get(), BPBlocks.YGGDRASIL_SAPLING.get());

        BPBlocks.BLOCKS.getEntries().stream().forEach(block -> {
            this.dropSelf(block.get());
        });


    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return BPBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }


}
