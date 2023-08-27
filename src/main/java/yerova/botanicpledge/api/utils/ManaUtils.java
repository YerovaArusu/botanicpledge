package yerova.botanicpledge.api.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ManaUtils {
    public static BlockPos[] getRitualManaPoolsPos(BlockPos pos) {
        BlockPos[] pools = new BlockPos[4];
        pools[0] = pos.offset(1, 0, 1);
        pools[1] = pos.offset(-1, 0, -1);
        pools[2] = pos.offset(-1, 0, 1);
        pools[3] = pos.offset(1, 0, -1);

        return pools;
    }


    //TODO: Rework this
    @Nullable
    public static BlockPos takeManaNearby(BlockPos pos, Level world, int range, int mana) {
        Optional<BlockPos> loc = BlockPos.findClosestMatch(pos, range, range, (b) -> world.getBlockEntity(b) instanceof TilePool && ((TilePool) world.getBlockEntity(b)).getCurrentMana() >= mana);
        if (!loc.isPresent())
            return null;
        TilePool tile = (TilePool) world.getBlockEntity(loc.get());
        tile.receiveMana(-mana);
        return loc.get();
    }

    public static boolean hasEnoughManaInRitual(BlockPos pos, Level world, int mana) {

        AtomicInteger currentMana = new AtomicInteger();
        Arrays.stream(getRitualManaPoolsPos(pos)).forEach(pose -> {
            if (world.getBlockEntity(pose) instanceof TilePool tilePool) {
                currentMana.set(currentMana.get() + tilePool.getCurrentMana());
            }
        });

        return currentMana.get() >= mana;

    }

    public static boolean takeManaFromMultipleSources(BlockPos pos, Level world, int mana) {
        AtomicInteger rest = new AtomicInteger(mana);
        AtomicInteger combinedMax = new AtomicInteger();

        Arrays.stream(getRitualManaPoolsPos(pos)).forEach(pose -> {
            if (world.getBlockEntity(pose) instanceof TilePool tilePool) {
                combinedMax.set(combinedMax.get() + tilePool.getCurrentMana());
            }
        });


        if (combinedMax.get() >= rest.get()) {
            Arrays.stream(getRitualManaPoolsPos(pos)).forEach(pose -> {
                if (world.getBlockEntity(pose) instanceof TilePool tilePool) {
                    if (tilePool.getCurrentMana() < rest.get()) {
                        rest.set(rest.get() - tilePool.getCurrentMana());
                        tilePool.receiveMana(-tilePool.getCurrentMana());
                    } else {
                        tilePool.receiveMana(-rest.get());
                    }

                }
            });
            return true;
        }
        return false;
    }


    /**
     * Searches for nearby mana jars that have enough mana.
     * Returns the position where the mana was taken, or null if none were found.
     */
    public static boolean hasManaNearby(BlockPos pos, Level world, int range, int mana) {
        Optional<BlockPos> loc = BlockPos.findClosestMatch(pos, range, range, (b) -> world.getBlockEntity(b) instanceof TilePool jar && jar.getCurrentMana() >= mana);
        return loc.isPresent();
    }

    @Nullable
    public static BlockPos canGiveManaClosest(BlockPos pos, Level world, int range) {
        Optional<BlockPos> loc = BlockPos.findClosestMatch(pos, range, range, (b) -> world.getBlockEntity(b) instanceof TilePool jar && jar.getCurrentMana() < jar.manaCap);
        return loc.orElse(null);
    }

    public static List<BlockPos> canGiveSourceAny(BlockPos pos, Level world, int range) {
        List<BlockPos> posList = new ArrayList<>();
        BlockPos.withinManhattanStream(pos, range, range, range).forEach(b -> {
            if (world.getBlockEntity(b) instanceof TilePool jar && jar.getCurrentMana() < jar.manaCap)
                posList.add(b.immutable());
        });
        return posList;
    }

    public static List<BlockPos> canTakeManaAny(BlockPos pos, Level world, int range) {
        List<BlockPos> posList = new ArrayList<>();
        BlockPos.withinManhattanStream(pos, range, range, range).forEach(b -> {
            if (world.getBlockEntity(b) instanceof TilePool jar && jar.getCurrentMana() > 0) {
                posList.add(b.immutable());
            }
        });
        return posList;
    }
}
