package yerova.botanicpledge.common.entitites.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
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
import yerova.botanicpledge.setup.BPEntities;

import java.util.List;

public class YggdFocusEntity extends Entity {

    public static @NotNull YggdFocusEntity getRegistry(EntityType<YggdFocusEntity> EntityType, Level level) {
        return new YggdFocusEntity(EntityType, level);
    }

    private Player owner;
    private float damage = 1.5F;

    public YggdFocusEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }


    public YggdFocusEntity(Level worldIn, Player owner) {
        super(BPEntities.YGGD_FOCUS.get(), worldIn);
        this.owner = owner;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            level().addParticle(YggdralParticleData.createData(new ParticleColor(12, 70, 204)),
                    this.getX() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                    this.getY() + 1.5 + ParticleUtils.inRange(-0.2, 0.2),
                    this.getZ() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                    0, 0, 0);

            level().addParticle(YggdralParticleData.createData(new ParticleColor(12, 70, 204)),
                    this.getX() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                    this.getY() + 1.5 + ParticleUtils.inRange(-0.2, 0.2),
                    this.getZ() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                    0, 0, 0);

            level().addParticle(YggdralParticleData.createData(new ParticleColor(12, 70, 204)),
                    this.getX() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                    this.getY() + 1.5 + ParticleUtils.inRange(-0.2, 0.2),
                    this.getZ() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                    0, 0, 0);

            level().addParticle(YggdralParticleData.createData(new ParticleColor(12, 70, 204)),
                    this.getX() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                    this.getY() + 1.5 + ParticleUtils.inRange(-0.2, 0.2),
                    this.getZ() + 0.5 + ParticleUtils.inRange(-0.2, 0.2),
                    0, 0, 0);
        } else {
            dmg();
        }

    }


    public void dmg() {
        for (LivingEntity entity : getEntitiesAround()) {
            if (owner != null) {
                if (entity instanceof Mob) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 2, 2, false, false));
                }
                if (entity == owner)
                    continue;
                Vec3 vec = this.position().subtract(entity.position());
                entity.setDeltaMovement(vec.normalize().scale(1.5D));

                if (this.tickCount % 20 == 0) {
                    entity.hurt(level().damageSources().playerAttack(owner), 4);
                }
            }
        }


        if (this.tickCount >= 80)
            remove(RemovalReason.KILLED);
    }


    public List<LivingEntity> getEntitiesAround() {
        BlockPos source = new BlockPos((int)getX(),(int) getY(),(int) getZ());
        float range = 6F;
        return level().getEntitiesOfClass(LivingEntity.class,
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
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
