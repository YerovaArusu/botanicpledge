package yerova.botanicpledge.common.worldgen.tree.placer;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.blocks.block_entities.YggdrasilLogBlockEntity;
import yerova.botanicpledge.setup.BPTrunkPlacerTypes;

import java.util.List;
import java.util.function.BiConsumer;

public class YggdrasilTrunkPlacer extends TrunkPlacer {

    public static final Codec<YggdrasilTrunkPlacer> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.intRange(0, 32).fieldOf("base_height").forGetter(p -> p.baseHeight),
                    Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter(p -> p.heightRandA),
                    Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter(p -> p.heightRandB)
            ).apply(builder, YggdrasilTrunkPlacer::new)
    );

    public YggdrasilTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return BPTrunkPlacerTypes.YGGDRASIL_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader reader,
                                                            BiConsumer<BlockPos, net.minecraft.world.level.block.state.BlockState> setter,
                                                            RandomSource rand,
                                                            int freeTreeHeight,
                                                            BlockPos pos,
                                                            TreeConfiguration config) {
        List<FoliagePlacer.FoliageAttachment> attachments = Lists.newArrayList();
        // Grunddirt
        BlockPos below = pos.below();
        setDirtAt(reader, setter, rand, below, config);
        setDirtAt(reader, setter, rand, below.east(), config);
        setDirtAt(reader, setter, rand, below.south(), config);
        setDirtAt(reader, setter, rand, below.east().south(), config);

        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        int crownY = y + freeTreeHeight - 1;
        boolean auraPlaced = false;

        for (int i = 0; i < freeTreeHeight; i++) {
            int currentY = y + i;
            BlockPos layerOrigin = new BlockPos(x, currentY, z);
            BlockPos patternOrigin = layerOrigin;
            if (currentY == y || currentY == y + 1) {
                patternOrigin = layerOrigin.offset(-2, 0, -2);
            }

            List<BlockPos> positions = Lists.newArrayList();
            if (currentY == y) {
                // Bodenlayer mit Offset (" ## "/"####"/"####"/" ## ")
                positions.add(patternOrigin.offset(2, 0, 1));
                positions.add(patternOrigin.offset(3, 0, 1));
                for (int dz = 2; dz <= 3; dz++) {
                    positions.add(patternOrigin.offset(1, 0, dz));
                    positions.add(patternOrigin.offset(2, 0, dz));
                    positions.add(patternOrigin.offset(3, 0, dz));
                    positions.add(patternOrigin.offset(4, 0, dz));
                }
                positions.add(patternOrigin.offset(2, 0, 4));
                positions.add(patternOrigin.offset(3, 0, 4));
            } else if (currentY == y + 1) {
                // Ebene darüber mit Offset (" #  "/" ###"/"### "/"  # ")
                positions.add(patternOrigin.offset(2, 0, 1));
                positions.add(patternOrigin.offset(1, 0, 2));
                positions.add(patternOrigin.offset(2, 0, 2));
                positions.add(patternOrigin.offset(3, 0, 2));
                positions.add(patternOrigin.offset(1, 0, 3));
                positions.add(patternOrigin.offset(2, 0, 3));
                positions.add(patternOrigin.offset(3, 0, 3));
                positions.add(patternOrigin.offset(3, 0, 4));
            } else {
                // Regular 2x2
                positions.add(layerOrigin);
                positions.add(layerOrigin.east());
                positions.add(layerOrigin.south());
                positions.add(layerOrigin.east().south());
            }

            // Logs platzieren
            for (BlockPos logPos : positions) {
                if (TreeFeature.isAirOrLeaves(reader, logPos)) {
                    placeLog(reader, setter, rand, logPos, config);
                }
            }

            // Wurzeln nur auf Bodenlayer
            if (currentY == y) {
                addRoots(reader, rand, patternOrigin, setter, config, new Direction[]{Direction.NORTH, Direction.WEST});
                addRoots(reader, rand, patternOrigin.east(), setter, config, new Direction[]{Direction.NORTH, Direction.EAST});
                addRoots(reader, rand, patternOrigin.south(), setter, config, new Direction[]{Direction.SOUTH, Direction.WEST});
                addRoots(reader, rand, patternOrigin.east().south(), setter, config, new Direction[]{Direction.SOUTH, Direction.EAST});
            }

            // Aura-Node zentral in Krone
            if (!auraPlaced && currentY == crownY) {
                BlockPos auraPos = new BlockPos(x, crownY, z);
                if (reader instanceof Level level) {
                    BlockEntity be = level.getBlockEntity(auraPos);
                    if (be instanceof YggdrasilLogBlockEntity aura) {
                        AuraImplementation data = new AuraImplementation();
                        data.randomize(level);
                        aura.setImplementation(data);
                        aura.setChanged();
                    }
                }
                auraPlaced = true;
            }
        }

        attachments.add(new FoliagePlacer.FoliageAttachment(new BlockPos(x, crownY, z), 0, true));
        return attachments;
    }




    protected static void setDirtAt(LevelSimulatedReader reader,
                                    BiConsumer<BlockPos, net.minecraft.world.level.block.state.BlockState> setter,
                                    RandomSource rand, BlockPos pos, TreeConfiguration config, boolean origin) {
        if (origin || reader.isStateAtPosition(pos, state ->
                state.is(net.minecraft.tags.BlockTags.DIRT) || state.is(net.minecraft.world.level.block.Blocks.FARMLAND))) {
            TrunkPlacer.setDirtAt(reader, setter, rand, pos, config);
        }
    }

    protected boolean addRoots(LevelSimulatedReader reader, RandomSource rand, BlockPos pos,
                               BiConsumer<BlockPos, net.minecraft.world.level.block.state.BlockState> setter,
                               TreeConfiguration config, Direction[] dirs) {
        for (Direction d : dirs) {
            BlockPos rootPos = pos.below().relative(d);
            placeRotatedRoot(reader, rand, rootPos, setter, config, d);
        }
        return true;
    }

    protected boolean placeRotatedRoot(LevelSimulatedReader reader, RandomSource rand, BlockPos pos,
                                       BiConsumer<BlockPos, net.minecraft.world.level.block.state.BlockState> setter,
                                       TreeConfiguration config, Direction d) {
        if (!TreeFeature.validTreePos(reader, pos)) return false;
        var state = config.trunkProvider.getState(rand, pos);
        if (state.hasProperty(net.minecraft.world.level.block.RotatedPillarBlock.AXIS)) {
            state = state.setValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS, d.getAxis());
        }
        setter.accept(pos, state);
        return true;
    }

    public static BlockPos get2x2Origin(Level level, BlockPos triggeredPos, Block saplingBlock) {
        for (int dx = -1; dx <= 0; dx++) {
            for (int dz = -1; dz <= 0; dz++) {
                BlockPos check = triggeredPos.offset(dx, 0, dz);
                if (level.getBlockState(check).is(saplingBlock) &&
                        level.getBlockState(check.east()).is(saplingBlock) &&
                        level.getBlockState(check.south()).is(saplingBlock) &&
                        level.getBlockState(check.east().south()).is(saplingBlock)) {
                    return check;
                }
            }
        }
        return triggeredPos;
    }
}
