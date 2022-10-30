package yerova.botanicpledge.common.items.relic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.client.particle.custom.ManaSweepParticleData;
import yerova.botanicpledge.common.utils.LeftClickable;

public class YggdRoot extends ItemRelic implements LeftClickable {
    public YggdRoot(Properties props) {
        super(props);
    }


    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        this.sweepAttack(player.getLevel(), player, 0.4F);
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void LeftClick(Level level, Player player, ItemStack stack) {
        this.sweepAttack(level, player, 0.4F);
    }

    public static IRelic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @NotNull
    public AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player) {
        return player.getBoundingBox().inflate(3.0D, 1D, 3.0D);
    }

    public void sweepAttack(Level level, Player player, double knockbackStrength) {

        for (LivingEntity livingentity : level.getEntitiesOfClass(LivingEntity.class, this.getSweepHitBox(player.getMainHandItem(), player))) {
            if (livingentity != player && player.canHit(livingentity, 0)) { // Original check was dist < 3, range is 3, so vanilla used padding=0
                livingentity.knockback(knockbackStrength, (double) Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), (double) (-Mth.cos(player.getYRot() * ((float) Math.PI / 180F))));
                livingentity.hurt(DamageSource.playerAttack(player), 10);

            }
        }
        double d0 = (double) (-Mth.sin(player.getYRot() * ((float) Math.PI / 180F)));
        double d1 = (double) Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
        if (level instanceof ServerLevel) {
            ((ServerLevel) level).sendParticles(ManaSweepParticleData.createData(new ParticleColor(66, 214, 227)), player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);
        }

    }

}
