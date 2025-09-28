package yerova.botanicpledge.common.aura_node.essence;

import net.minecraft.core.Direction;

public interface IEssenceTransportable {


    boolean canReceiveEssenceFromSide(Direction from);

    boolean canExtractEssenceFromSide(Direction to);

    int receiveEssence(Direction from, Essence essence, EssenceCapacitorImplementation impl, int amount, boolean simulate);

    int extractEssence(Direction to, Essence essence, EssenceCapacitorImplementation impl, int amount, boolean simulate);
}
