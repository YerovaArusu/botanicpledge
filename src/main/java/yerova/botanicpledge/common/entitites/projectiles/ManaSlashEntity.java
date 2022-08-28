package yerova.botanicpledge.common.entitites.projectiles;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import yerova.botanicpledge.common.entitites.EntityInit;

public class ManaSlashEntity extends ThrowableProjectile implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);




    public ManaSlashEntity(double x, double y, double z, Level level) {
        super(EntityInit.MANA_SLASH.get(), x, y, z, level);
    }



    public ManaSlashEntity(LivingEntity shooter, Level level) {
        super(EntityInit.MANA_SLASH.get(), shooter, level);
    }


    public ManaSlashEntity(EntityType<ManaSlashEntity> entityEntityType, Level level) {
        super(entityEntityType, level);

    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationEvent animationEvent) {
        animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", true));
        return PlayState.CONTINUE;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        hitResult.getEntity().hurt(new DamageSource(this.getOwner().getName().getString()), 20);

        super.onHitEntity(hitResult);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();

        if(this.tickCount >= 25)
            this.remove(RemovalReason.KILLED);
    }

    public static @NotNull ManaSlashEntity getRegistry(EntityType<ManaSlashEntity> EntityType, Level level) {
        return new ManaSlashEntity(EntityType, level);
    }

}


