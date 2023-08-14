package yerova.botanicpledge.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class EntityUtils {

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

}
