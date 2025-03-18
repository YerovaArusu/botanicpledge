package yerova.botanicpledge.common.aura_node;

import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.aura_node.essence.EssenceList;

import java.util.LinkedHashMap;

public interface IAuraNode {
    String id();

    EssenceList getAuraNodeEssences();

    void setAuraNodeType(AuraNodeType type);
    void setAuraNodeModifier(AuraNodeModifier type);
    AuraNodeModifier getNodeModifier();


    int getAuraNodeEssenceBase(Essence paramAspect);
    void setAuraNodeEssenceBase(Essence paramAspect, int amount);

    int getAuraNodeEssence(Essence paramAspect);
    void setAuraNodeEssence(Essence paramAspect, int amount);
}
