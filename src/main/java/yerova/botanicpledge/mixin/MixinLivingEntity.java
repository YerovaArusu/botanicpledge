package yerova.botanicpledge.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yerova.botanicpledge.common.utils.PlayerUtils;


@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Inject(at = @At(value = "RETURN"), method = "getDamageAfterMagicAbsorb", cancellable = true)
    private void onGetDamageAfterMagicAbsorb(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
        cir.cancel();
        LivingEntity l = (LivingEntity) ((Object) this);
        cir.setReturnValue(PlayerUtils.getDamageAfterMagicAbsorb(l, pDamageSource,pDamageAmount));
    }


}
