package yerova.botanicpledge.common.aura_node.essence;

public interface IEssenceHolder<T extends EssenceCapacitorImplementation> {

    T getImplementation();
    void setImplementation(T impl);

}
