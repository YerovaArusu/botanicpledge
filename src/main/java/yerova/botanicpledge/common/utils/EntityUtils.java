package yerova.botanicpledge.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class EntityUtils {


    public static HitResult raytrace(Entity e, double distance, boolean fluids) {
        return e.pick(distance, 1, fluids);
    }

    public static List<LivingEntity> getAttackableEnemiesAround(@NotNull LivingEntity entity, Level level, int radius, Predicate<LivingEntity> entitiesSelector) {
        final TargetingConditions alertableTargeting = TargetingConditions.forCombat().range(radius).ignoreLineOfSight().selector(entitiesSelector);
        return level.getNearbyEntities(LivingEntity.class, alertableTargeting, entity,
                new AABB(entity.getX() - radius, entity.getY() - radius, entity.getZ() - radius,
                        entity.getX() + radius, entity.getY() + radius, entity.getZ() + radius));
    }

    public static List<LivingEntity> getAttackableEnemiesAround(@NotNull LivingEntity entity, Level level, BlockPos pos, int radius, Predicate<LivingEntity> entitiesSelector) {
        final TargetingConditions alertableTargeting = TargetingConditions.forCombat().range(radius).ignoreLineOfSight().selector(entitiesSelector);
        return level.getNearbyEntities(LivingEntity.class, alertableTargeting, entity,
                new AABB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                        pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius));
    }

    public static class AttackableEntitiesSelector implements Predicate<LivingEntity> {
        public boolean test(LivingEntity pEntity) {
            if (pEntity instanceof Mob) {
                return true;
            } else return pEntity instanceof Player player && !player.isSpectator() && !player.isCreative();
        }
    }

    public static Entity getPlayerPOVHitResult(Level level, Player player, int range) {
        if (level.isClientSide) return player;
        Entity entity = null;
        float playerRotX = player.getXRot();
        float playerRotY = player.getYRot();
        Vec3 startPos = player.getEyePosition();
        float f2 = Mth.cos(-playerRotY * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-playerRotY * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-playerRotX * ((float) Math.PI / 180F));
        float additionY = Mth.sin(-playerRotX * ((float) Math.PI / 180F));
        float additionX = f3 * f4;
        float additionZ = f2 * f4;
        double d0 = range;
        Vec3 endVec = startPos.add((double) additionX * d0, (double) additionY * d0, (double) additionZ * d0);
        AABB startEndBox = new AABB(startPos, endVec);

        for (Entity entity1 : level.getEntities(player, startEndBox, (val) -> true)) {
            AABB aabb = entity1.getBoundingBox().inflate(range);
            Optional<Vec3> optional = aabb.clip(startPos, endVec);
            if (aabb.contains(startPos)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    startPos = optional.orElse(startPos);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = startPos.distanceToSqr(vec31);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == player.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            startPos = vec31;
                        }
                    } else {
                        entity = entity1;
                        startPos = vec31;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

    public static boolean hasIdMatch(Set<LivingEntity> entities, Entity entity) {
        boolean toReturn = false;

        for (LivingEntity e : entities) {
            if (e.getId() == entity.getId()) {
                toReturn = true;
                break;
            }
        }
        return toReturn;
    }
}
