package yerova.botanicpledge.common.aura_node;

import net.minecraft.world.level.block.BeaconBeamBlock;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.aura_node.essence.EssenceList;

public interface IAuraNode {

    AuraImplementation getImplementation();

    void setImplementation(AuraImplementation implementation);
}
