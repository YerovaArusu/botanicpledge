package yerova.botanicpledge.api.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManaUtils {
    @Nullable
    public static BlockPos takeManaNearby(BlockPos pos, Level world, int range, int mana) {
        Optional<BlockPos> loc = BlockPos.findClosestMatch(pos, range, range, (b) -> world.getBlockEntity(b) instanceof TilePool && ((TilePool) world.getBlockEntity(b)).getCurrentMana() >= mana);
        if (!loc.isPresent())
            return null;
        TilePool tile = (TilePool) world.getBlockEntity(loc.get());
        tile.receiveMana(-mana);
        return loc.get();
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
