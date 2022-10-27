package yerova.botanicpledge.common.entitites.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.client.particle.ParticleUtils;
import yerova.botanicpledge.client.particle.custom.YggdralParticleData;
import yerova.botanicpledge.common.entitites.EntityInit;

import java.util.List;

public class YggdFocus extends Entity {

    public static @NotNull YggdFocus getRegistry(EntityType<YggdFocus> EntityType, Level level) {
        return new YggdFocus(EntityType, level);
    }

    private Player owner;
    private float damage = 1.5F;

    public YggdFocus(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }


    public YggdFocus(Level worldIn, Player owner) {
        super(EntityInit.YGGD_FOCUS.get(), worldIn);
        this.owner = owner;
    }

    @Override
    public void tick() {
        super.tick();

        level.addParticle(YggdralParticleData.createData(new ParticleColor(12, 70, 204)),
                this.getX() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                this.getY() + 1.5 + ParticleUtils.inRange(-0.2, 0.2),
                this.getZ() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                0, 0, 0);

        level.addParticle(YggdralParticleData.createData(new ParticleColor(12, 70, 204)),
                this.getX() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                this.getY() + 1.5 + ParticleUtils.inRange(-0.2, 0.2),
                this.getZ() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                0, 0, 0);

        level.addParticle(YggdralParticleData.createData(new ParticleColor(12, 70, 204)),
                this.getX() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                this.getY() + 1.5 + ParticleUtils.inRange(-0.2, 0.2),
                this.getZ() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                0, 0, 0);

        level.addParticle(YggdralParticleData.createData(new ParticleColor(12, 70, 204)),
                this.getX() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                this.getY() + 1.5 + ParticleUtils.inRange(-0.2, 0.2),
                this.getZ() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                0, 0, 0);


        dmg();
    }


    public void dmg() {
        for (LivingEntity entity : getEntitiesAround()) {
            if (owner != null) {
                if (entity instanceof Mob) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 3, false, false));
                }
                if (entity == owner)
                    continue;
                Vec3 vec = this.position().subtract(entity.position());
                entity.setDeltaMovement(vec.normalize().scale(1.5D));
            }
        }

        if (this.tickCount % 15 == 0) {
            //damageAllAround(damage);
        }

        if (this.tickCount >= 40)
            remove(RemovalReason.KILLED);
    }


    public List<LivingEntity> getEntitiesAround() {
        BlockPos source = new BlockPos(getX(), getY(), getZ());
        float range = 6F;
        return level.getEntitiesOfClass(LivingEntity.class,
                new AABB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range,
                        source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
    }

    @Override
    protected void defineSynchedData() {

    }


    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
