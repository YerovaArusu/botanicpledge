package yerova.botanicpledge.common.items.relic;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
import org.jetbrains.annotations.NotNull;
import yerova.botanicpledge.common.entitites.projectiles.AsgardBladeEntity;
import yerova.botanicpledge.common.entitites.projectiles.YggdrafoliumEntity;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.EntityUtils;
import yerova.botanicpledge.common.utils.LeftClickable;
import yerova.botanicpledge.common.utils.ModNBTUtils;
import yerova.botanicpledge.setup.TierInit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class AsgardFractal extends SwordItem implements LeftClickable {


    public AsgardFractal(Properties pProperties) {
        super(TierInit.YGGDRALIUM_TIER, 1, 0, pProperties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {


        List<LivingEntity> list = EntityUtils.getAttackableEnemiesAround(player, level, 20, new EntityUtils.AttackableEntitiesSelector());
        int amount = 10;
        if(!list.isEmpty()) {
            for (int i = 0; i <= amount-1; i++) {

                if (list.size() >= amount) {
                    summonProjectile(level, player, list.get(i));
                } else if (i<list.size()){
                    summonProjectile(level,player, list.get(i));
                } else if (i%(list.size()) > 0){
                    summonProjectile(level,player, list.get(i%(list.size())));
                } else {
                    summonProjectile(level,player, list.get(0));
                }
            }
        }


        return super.use(level, player, pUsedHand);
    }

    public void summonProjectile(Level level,Player player, LivingEntity target){
        double range = 4D;
        double j = -Math.PI + 2 * Math.PI * Math.random();
        double k;
        double x, y, z;

        k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
        x = player.getX() + range * Math.sin(k) * Math.cos(j);
        y = player.getY() + range * Math.cos(k);
        z = player.getZ() + range * Math.sin(k) * Math.sin(j);
        j += 2 * Math.PI * Math.random() * 0.08F + 2 * Math.PI * 0.17F;


        AsgardBladeEntity blade = new AsgardBladeEntity(level, player, target);

        blade.setPos(x, y, z);
        blade.setVariety(1);
        blade.faceTarget(1);
        blade.setNoGravity(true);
        level.addFreshEntity(blade);
    }


    @Override
    public void LeftClick(Level level, Player player, ItemStack stack) {

    }




}
