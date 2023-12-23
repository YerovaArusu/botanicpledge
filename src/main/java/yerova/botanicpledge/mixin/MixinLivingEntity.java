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
import yerova.botanicpledge.integration.curios.ItemHelper;


@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Inject(at = @At(value = "HEAD"), method = "getDamageAfterMagicAbsorb", cancellable = true)
    private void onGetDamageAfterMagicAbsorb(DamageSource pDamageSource, float pDamageAmount, CallbackInfoReturnable<Float> cir) {
        cir.cancel();

        LivingEntity l = (LivingEntity) ((Object) this);

        if (pDamageSource.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            cir.setReturnValue(pDamageAmount);
        } else {
            if (l.hasEffect(MobEffects.DAMAGE_RESISTANCE) && !pDamageSource.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                int i = (l.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = pDamageAmount * (float) j;
                float f1 = pDamageAmount;
                pDamageAmount = Math.max(f / 25.0F, 0.0F);
                float f2 = f1 - pDamageAmount;
                if (f2 > 0.0F && f2 < 3.4028235E37F) {
                    if (l instanceof ServerPlayer) {
                        ((ServerPlayer) l).awardStat(Stats.CUSTOM.get(Stats.DAMAGE_RESISTED), Math.round(f2 * 10.0F));
                    } else if (pDamageSource.getEntity() instanceof ServerPlayer) {
                        ((ServerPlayer) pDamageSource.getEntity()).awardStat(Stats.CUSTOM.get(Stats.DAMAGE_DEALT_RESISTED), Math.round(f2 * 10.0F));
                    }
                }
            }

            if (pDamageAmount <= 0.0F) {
                cir.setReturnValue(0.0F);
            } else {
                int k = EnchantmentHelper.getDamageProtection(l.getArmorSlots(), pDamageSource);
                if (l instanceof ServerPlayer player) {
                    k += EnchantmentHelper.getDamageProtection(
                            ItemHelper.getDivineCoreCurio(player).stream().map(SlotResult::stack).toList(),
                            pDamageSource);
                }
                if (k > 0) {
                    pDamageAmount = CombatRules.getDamageAfterMagicAbsorb(pDamageAmount, (float) k);
                }
                cir.setReturnValue(pDamageAmount);
            }
        }
    }


}
