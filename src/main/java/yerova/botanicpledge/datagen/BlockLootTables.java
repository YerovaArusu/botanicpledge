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
        this.dropSelf(BPBlocks.YGGDRAL_SPREADER.get());
        this.dropSelf(BPBlocks.MANA_BUFFER.get());
        this.dropSelf(BPBlocks.RITUAL_CENTER.get());
        this.dropSelf(BPBlocks.ORE_INFUSION.get());
        this.dropSelf(BPBlocks.YGGDRALIUM_BLOCK.get());
        this.dropSelf(BPBlocks.RITUAL_PEDESTAL.get());
        this.dropSelf(BPBlocks.MODIFICATION_TABLE.get());
        this.dropSelf(BPBlocks.THUNDER_LILY.get());
        this.dropSelf(BPBlocks.YGGDRASIL_PYLON.get());
        this.dropSelf(BPBlocks.YGGDRASIL_SAPLING.get());
        this.dropSelf(BPBlocks.YGGDRASIL_LOG.get());
        this.dropSelf(BPBlocks.YGGDRASIL_WOOD.get());
        this.dropSelf(BPBlocks.STRIPPED_YGGDRASIL_LOG.get());
        this.dropSelf(BPBlocks.STRIPPED_YGGDRASIL_WOOD.get());
        this.dropSelf(BPBlocks.YGGDRASIL_PLANKS.get());
        this.dropSelf(BPBlocks.YGGDRASIL_STAIRS.get());
        this.dropSelf(BPBlocks.YGGDRASIL_SLAB.get());
        this.dropSelf(BPBlocks.YGGDRASIL_FENCE.get());
        this.dropSelf(BPBlocks.YGGDRASIL_FENCE_GATE.get());
        this.dropSelf(BPBlocks.YGGDRASIL_WALL.get());
        this.dropSelf(BPBlocks.YGGDRASIL_TRAPDOOR.get());
        this.dropSelf(BPBlocks.AURA_NODE.get());
        this.add(BPBlocks.YGGDRASIL_DOOR.get(),block -> createDoorTable(block));

        this.dropSelf(BPBlocks.ESSENCE_JAR.get());
        this.dropSelf(BPBlocks.ESSENCE_CONDENSER.get());
        this.dropSelf(BPBlocks.ESSENCE_TRANSPORTER.get());
        this.dropSelf(BPBlocks.ESSENCE_EXTRACTOR.get());


        this.createLeavesDrops(BPBlocks.YGGDRASIL_LEAVES.get(), BPBlocks.YGGDRASIL_SAPLING.get(), 0.05f);
    }


    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BPBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }


}
