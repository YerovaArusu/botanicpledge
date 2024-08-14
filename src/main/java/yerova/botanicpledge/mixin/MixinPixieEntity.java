package yerova.botanicpledge.mixin;


import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.entity.PixieEntity;
import yerova.botanicpledge.common.items.armor.YggdrasilsteelHelmet;
import yerova.botanicpledge.mixin_api.IMixinPixieEntity;
import yerova.botanicpledge.setup.BPItems;

import javax.annotation.Nullable;

@Mixin(PixieEntity.class)
public abstract class MixinPixieEntity extends FlyingMob implements IMixinPixieEntity {

    @Shadow
    @Nullable
    private LivingEntity summoner;

    @Shadow
    private float damage;

    @Shadow
    @Nullable
    private MobEffectInstance effect;


    @Shadow public abstract void setPixieType(int type);

    protected MixinPixieEntity(EntityType<? extends FlyingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void botanicpledge$setSummoner(LivingEntity summoner) {
        this.summoner = summoner;
    }

    @Override
    public LivingEntity botanicpledge$getSummoner() {
        return summoner;
    }

    @Inject(method = "customServerAiStep", at = @At("HEAD"), cancellable = true)
    public void onTick(CallbackInfo ci) {
        PixieEntity self = (PixieEntity) (Object) this;

        LivingEntity target = self.getTarget();
        if (target != null) {
            double d0 = target.getX() + target.getBbWidth() / 2 - getX();
            double d1 = target.getY() + target.getBbHeight() / 2 - getY();
            double d2 = target.getZ() + target.getBbWidth() / 2 - getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            float mod = 0.45F;
            if (self.getPixieType() == 1) {
                mod = 0.1F;
            }

            setDeltaMovement(d0 / d3 * mod, d1 / d3 * mod, d2 / d3 * mod);

            if (Math.sqrt(d3) < 1F) {
                if (summoner != null && summoner != target) {
                    if (summoner instanceof Player player) {
                        target.hurt(damageSources().playerAttack(player), damage);
                    } else {
                        target.hurt(damageSources().mobAttack(summoner), damage);
                    }
                } else {
                    target.hurt(damageSources().mobAttack(this), damage);
                }
                if (effect != null && !(target instanceof Player)) {
                    target.addEffect(effect);
                }

                for (ItemStack stack : summoner.getArmorSlots()) {
                    if (stack.is(BPItems.YGGDRASIL_HELMET.get())) {
                        YggdrasilsteelHelmet.decrementPixieCount(stack);
                        break;
                    }
                }

                discard();
            }
        }

        yBodyRot = -((float) Math.atan2(getDeltaMovement().x(), getDeltaMovement().z()))
                * 180.0F / (float) Math.PI;
        setYRot(yBodyRot);
    }

}
