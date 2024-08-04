package yerova.botanicpledge.mixin_api;


import net.minecraft.world.entity.LivingEntity;

public interface IMixinPixieEntity {
    void botanicpledge$setSummoner(LivingEntity summoner);
    LivingEntity botanicpledge$getSummoner();
}
