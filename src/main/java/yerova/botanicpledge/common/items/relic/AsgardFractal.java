package yerova.botanicpledge.common.items.relic;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import yerova.botanicpledge.setup.TierInit;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.LeftClickable;
import yerova.botanicpledge.common.utils.ModNBTUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;


/*
    TODO: finish this Weapon.
    I have no clue how to finish this item, because i dont understand how Vectors work

    Inspiration and Idea:
    https://youtu.be/8qKTXf_-obk

 */
public class AsgardFractal extends SwordItem implements LeftClickable {
    private ArrayList<Entity> summonedBeings = new ArrayList<>();


    public AsgardFractal(Properties pProperties) {
        super(TierInit.YGGDRALIUM_TIER, 1, 0, pProperties);

    }



    @Override
    public void LeftClick(Level level, Player player, ItemStack stack) {
        getAttackableEnemiesAroundUser(player, level, 20).forEach(livingEntity -> {
            moveEntityAroundPlayer(level,player,livingEntity);
            level.addFreshEntity(new Arrow(level, livingEntity.getX(), livingEntity.getY() +2, livingEntity.getZ()));
        });
    }

    public void summonProjectiles() {

    }

    public void increaseProjectileNBT(ItemStack stack,int toIncreaseBy){
        if(ModNBTUtils.hasModTag(stack,BPConstants.PROJECTILE_COUNT_TRACKER_TAG_NAME)) {

        }
    }

    public void moveEntityAroundPlayer(Level level,Player player, Entity entity) {
        Vec3 v3 = player.position();

        //level.addParticle(data, (double)worldPosition.getX() + Math.random(), (double)worldPosition.getY() + 0.8, (double)worldPosition.getZ() + Math.random(), 0.0, 0.02500000037252903, 0.0);

        entity.setPos(player.getX() + (new Random().nextDouble(8)-4), player.getY() +1, player.getZ() + (new Random().nextDouble(8)-4));

    }


    public static List<LivingEntity> getAttackableEnemiesAroundUser(Player player, Level level, int radius) {
        final TargetingConditions alertableTargeting = TargetingConditions.forCombat().range(radius).ignoreLineOfSight().selector(new AttackableEntitiesSelector());
        return level.getNearbyEntities(LivingEntity.class, alertableTargeting,player,
                new AABB(player.getX()- radius, player.getY() -radius, player.getZ() - radius,
                        player.getX() + radius,player.getY() +radius, player.getZ() + radius));
    }

    public static class AttackableEntitiesSelector implements Predicate<LivingEntity> {
        public boolean test(LivingEntity pEntity) {
            if(pEntity instanceof Mob) {
                return true;
            } else return pEntity instanceof Player player && !player.isSpectator() && !player.isCreative();
        }
    }

}
