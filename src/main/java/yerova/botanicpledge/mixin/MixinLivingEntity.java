package yerova.botanicpledge.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotResult;
import yerova.botanicpledge.common.utils.PlayerUtils;
import yerova.botanicpledge.integration.curios.ItemHelper;


@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Inject(at = @At(value = "RETURN"), method = "getDamageAfterMagicAbsorb", cancellable = true)
    private void onGetDamageAfterMagicAbsorb(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
        cir.cancel();
        LivingEntity l = (LivingEntity) ((Object) this);
        cir.setReturnValue(PlayerUtils.getDamageAfterMagicAbsorb(l, pDamageSource,pDamageAmount));
    }


}
