package yerova.botanicpledge.common.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotResult;
import vazkii.botania.xplat.XplatAbstractions;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.client.particle.custom.ManaSweepParticleData;
import yerova.botanicpledge.common.items.relic.YggdRamus;
import yerova.botanicpledge.integration.curios.ItemHelper;

import java.util.Objects;

public class PlayerUtils {


    public static boolean removeItemFromInventory(@NotNull Player player, @NotNull ItemStack stack, int amount) {
        if (!player.getInventory().contains(stack)) return false;
        if (stack.getCount() < amount) return false;

        if (stack.getCount() == amount) {
            player.getInventory().removeItem(stack);
            return true;
        }

        if (stack.getCount() > amount) {
            for (ItemStack s : player.getInventory().items) {
                if (s.equals(stack, true)) {
                    s.setCount(s.getCount() - amount);
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean checkForArmorFromMod(@NotNull Player player, @NotNull String modIdToCheckFor) {
        boolean toReturn = false;
        for (ItemStack stack : player.getArmorSlots()) {
            if (stack != ItemStack.EMPTY && stack != null && ForgeHooks.getDefaultCreatorModId(stack).equals(modIdToCheckFor)) {
                toReturn = true;
                break;
            }
        }
        return toReturn;
    }

    public static void sweepAttack(@NotNull Level level, @NotNull Player player, ItemStack stack, double knockbackStrength) {
        if (!level.isClientSide) {

            for (LivingEntity enemy : level.getEntitiesOfClass(LivingEntity.class, getSweepHitBox(player.getMainHandItem(), player))) {
                if (enemy != level.getPlayerByUUID(Objects.requireNonNull(Objects.requireNonNull(XplatAbstractions.INSTANCE.findRelic(player.getMainHandItem())).getSoulbindUUID())) && player.canAttack(enemy)) { // Original check was dist < 3, range is 3, so vanilla used padding=0

                    enemy.knockback(knockbackStrength, (double) Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(player.getYRot() * ((float) Math.PI / 180F))));
                    enemy.hurt(level.damageSources().playerAttack(player), 16);

                    YggdRamus.appendFireAspect(player, enemy);
                }
            }
        } else {
            double d0 = (double) (-Mth.sin(player.getYRot() * ((float) Math.PI / 180F)));
            double d1 = (double) Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
            level.addParticle(ManaSweepParticleData.createData(new ParticleColor(66, 214, 227)),
                    player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 1.0D, 1.0D, 1.0D);
        }
    }

    @NotNull
    public static AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player) {
        return player.getBoundingBox().inflate(5.0D, 2D, 5.0D);
    }


    public static float getDamageAfterMagicAbsorb(LivingEntity entity,DamageSource pDamageSource, float pDamageAmount) {


        if (pDamageSource.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            return pDamageAmount;
        } else {
            if (entity.hasEffect(MobEffects.DAMAGE_RESISTANCE) && !pDamageSource.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
                int i = (entity.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = pDamageAmount * (float) j;
                float f1 = pDamageAmount;
                pDamageAmount = Math.max(f / 25.0F, 0.0F);
                float f2 = f1 - pDamageAmount;
                if (f2 > 0.0F && f2 < 3.4028235E37F) {
                    if (entity instanceof ServerPlayer) {
                        ((ServerPlayer) entity).awardStat(Stats.CUSTOM.get(Stats.DAMAGE_RESISTED), Math.round(f2 * 10.0F));
                    } else if (pDamageSource.getEntity() instanceof ServerPlayer) {
                        ((ServerPlayer) pDamageSource.getEntity()).awardStat(Stats.CUSTOM.get(Stats.DAMAGE_DEALT_RESISTED), Math.round(f2 * 10.0F));
                    }
                }
            }

            if (pDamageAmount <= 0.0F) {
                return 0F;
            } else {
                int k = EnchantmentHelper.getDamageProtection(entity.getArmorSlots(), pDamageSource);
                if (entity instanceof ServerPlayer player) {
                    k += EnchantmentHelper.getDamageProtection(
                            ItemHelper.getDivineCoreCurio(player).stream().map(SlotResult::stack).toList(),
                            pDamageSource);
                }
                if (k > 0) {
                    pDamageAmount = CombatRules.getDamageAfterMagicAbsorb(pDamageAmount, (float) k);
                }
                return pDamageAmount;
            }
        }
    }

}
