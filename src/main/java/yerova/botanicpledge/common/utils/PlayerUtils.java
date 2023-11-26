package yerova.botanicpledge.common.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.xplat.XplatAbstractions;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.client.particle.custom.ManaSweepParticleData;
import yerova.botanicpledge.common.items.relic.YggdRamus;

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

    public static Player getStackOwner(@NotNull Level level, @NotNull ItemStack stack) {
        for (Player p : level.players()) {
            if (p.getInventory().contains(stack)) return p;
        }
        return level.getNearestPlayer(0, 0, 0, Double.MAX_VALUE, false);
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
    public static void sweepAttack(@NotNull Level level, @NotNull Player player, ItemStack stack,double knockbackStrength) {
        if (!level.isClientSide) {
            for (LivingEntity enemy : level.getEntitiesOfClass(LivingEntity.class, getSweepHitBox(player.getMainHandItem(), player))) {
                if (enemy != level.getPlayerByUUID(Objects.requireNonNull(Objects.requireNonNull(XplatAbstractions.INSTANCE.findRelic(player.getMainHandItem())).getSoulbindUUID())) && player.canAttack(enemy)) { // Original check was dist < 3, range is 3, so vanilla used padding=0

                    enemy.knockback(knockbackStrength, (double) Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(player.getYRot() * ((float) Math.PI / 180F))));
                    //((AsgardFractal)stack.getItem()).hurtEnemy(player.getMainHandItem(), enemy, player);

                    enemy.hurt(level.damageSources().playerAttack(player), 16);

                    YggdRamus.appendFireAspect(player, enemy);
                }
            }
            double d0 = (double) (-Mth.sin(player.getYRot() * ((float) Math.PI / 180F)));
            double d1 = (double) Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
            if (level.isClientSide) {
                level.addParticle(ManaSweepParticleData.createData(new ParticleColor(66, 214, 227)),
                        player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 1.0D, 1.0D, 1.0D);
            }
        }
    }
    @NotNull
    public static AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player) {
        return player.getBoundingBox().inflate(3.0D, 1D, 3.0D);
    }

}
