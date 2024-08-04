package yerova.botanicpledge.mixin;


import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.entity.MagicLandmineEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import yerova.botanicpledge.common.entitites.yggdrasilguardian.YggdrasilGuardian;
import yerova.botanicpledge.mixin_api.IMagicLandmineEntity;

import java.util.List;

@Mixin(MagicLandmineEntity.class)
public abstract class MixinMagicLandMineEntity extends Entity implements IMagicLandmineEntity {

    @Unique
    public YggdrasilGuardian botanicpledge$guardian;

    public MixinMagicLandMineEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Unique
    public YggdrasilGuardian botanicpledge$getGuardian() {
        return botanicpledge$guardian;
    }

    @Unique
    public void botanicpledge$setGuardian(YggdrasilGuardian customSummoner) {
        this.botanicpledge$guardian = customSummoner;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void onTick(CallbackInfo ci) {
        MagicLandmineEntity self = (MagicLandmineEntity) (Object) this;
        self.setDeltaMovement(Vec3.ZERO);
        super.tick();

        float range = self.getBbWidth() / 2;
        float r = 0.2F;
        float g = 0F;
        float b = 0.2F;

        for (int i = 0; i < 6; i++) {
            WispParticleData data = WispParticleData.wisp(0.4F, r, g, b, 1.0F);
            level().addParticle(data, getX() - range + Math.random() * range * 2, getY(), getZ() - range + Math.random() * range * 2, 0, -0.015F, 0);
        }

        if (tickCount >= 55) {
            level().playSound(null, getX(), getY(), getZ(), BotaniaSounds.gaiaTrap, SoundSource.NEUTRAL, 1F, 1F);

            float m = 0.35F;
            g = 0.4F;
            for (int i = 0; i < 25; i++) {
                WispParticleData data = WispParticleData.wisp(0.5F, r, g, b);
                level().addParticle(data, getX(), getY() + 1, getZ(), (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m, (float) (Math.random() - 0.5F) * m);
            }

            if (!level().isClientSide) {
                List<Player> players = level().getEntitiesOfClass(Player.class, getBoundingBox());
                for (Player player : players) {
                    player.hurt(this.damageSources().indirectMagic(this, botanicpledge$guardian), 10);
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 25, 0));
                    MobEffectInstance wither = new MobEffectInstance(MobEffects.WITHER, 120, 2);
                    player.addEffect(wither);
                }
            }

            discard();
        }
        ci.cancel();
    }
}
