package yerova.botanicpledge.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.util.Arrays;
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

    public static boolean hasEnoughManaInRitual(BlockPos pos, Level world, int mana) {
        return getAvailableManaInRitual(pos,world) >= mana;
    }

    public static int getAvailableManaInRitual(BlockPos pos, Level world) {
        AtomicInteger currentMana = new AtomicInteger();
        Arrays.stream(getRitualManaPoolsPos(pos)).forEach(pose -> {
            if (world.getBlockEntity(pose) instanceof ManaPoolBlockEntity tilePool) {
                currentMana.set(currentMana.get() + tilePool.getCurrentMana());
            }
        });
        return currentMana.get();
    }

    public static boolean takeManaFromMultipleSources(BlockPos pos, Level world, int mana) {
        AtomicInteger rest = new AtomicInteger(mana);
        AtomicInteger combinedMax = new AtomicInteger();

        Arrays.stream(getRitualManaPoolsPos(pos)).forEach(pose -> {
            if (world.getBlockEntity(pose) instanceof ManaPoolBlockEntity tilePool) {
                combinedMax.set(combinedMax.get() + tilePool.getCurrentMana());
            }
        });


        if (combinedMax.get() >= rest.get()) {
            Arrays.stream(getRitualManaPoolsPos(pos)).forEach(pose -> {
                if (world.getBlockEntity(pose) instanceof ManaPoolBlockEntity tilePool) {
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


}
