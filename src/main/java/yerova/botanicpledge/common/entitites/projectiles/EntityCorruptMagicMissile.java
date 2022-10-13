package yerova.botanicpledge.common.entitites.projectiles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.helper.VecHelper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

public class EntityCorruptMagicMissile extends ThrowableProjectile {
    public EntityCorruptMagicMissile(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    private static final String TAG_TIME = "time";
    private static final EntityDataAccessor<Boolean> EVIL = SynchedEntityData.defineId(EntityCorruptMagicMissile.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> TARGET = SynchedEntityData.defineId(EntityCorruptMagicMissile.class, EntityDataSerializers.INT);

    double lockX, lockY = Integer.MIN_VALUE, lockZ;
    int time = 0;

    public EntityCorruptMagicMissile(LivingEntity owner, boolean evil, int damage) {
        super(ModEntities.MAGIC_MISSILE, owner, owner.level);
        this.damage = damage;
        setEvil(evil);
    }

    public static @NotNull EntityCorruptMagicMissile getRegistry(EntityType<EntityCorruptMagicMissile> EntityType, Level level) {
        return new EntityCorruptMagicMissile(EntityType, level);
    }


    private int damage;


    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(EVIL, false);
        entityData.define(TARGET, 0);
    }

    public void setEvil(boolean evil) {
        entityData.set(EVIL, evil);
    }

    public boolean isEvil() {
        return entityData.get(EVIL);
    }

    public void setTarget(LivingEntity e) {
        entityData.set(TARGET, e == null ? -1 : e.getId());
    }

    public LivingEntity getTargetEntity() {
        int id = entityData.get(TARGET);
        Entity e = level.getEntity(id);
        if (e instanceof LivingEntity le) {
            return le;
        }

        return null;
    }

    @Override
    public void tick() {
        double lastTickPosX = this.xOld;
        double lastTickPosY = this.yOld;
        double lastTickPosZ = this.zOld;

        super.tick();

        if (!level.isClientSide && (!findTarget() || time > 40)) {
            discard();
            return;
        }

        boolean evil = isEvil();
        Vec3 thisVec = VecHelper.fromEntityCenter(this);
        Vec3 oldPos = new Vec3(lastTickPosX, lastTickPosY, lastTickPosZ);
        Vec3 diff = thisVec.subtract(oldPos);
        Vec3 step = diff.normalize().scale(0.05);
        int steps = (int) (diff.length() / step.length());
        Vec3 particlePos = oldPos;

        SparkleParticleData data = evil ? SparkleParticleData.corrupt(2F, 0F, 0.0F, 1F, 1)
                : SparkleParticleData.sparkle(2F, 0F, 0.0F, 1F, 1);
        for (int i = 0; i < steps; i++) {
            level.addParticle(data, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);

            if (level.random.nextInt(steps) <= 1) {
                level.addParticle(data, particlePos.x + (Math.random() - 0.5) * 0.4, particlePos.y + (Math.random() - 0.5) * 0.4, particlePos.z + (Math.random() - 0.5) * 0.4, 0, 0, 0);
            }

            particlePos = particlePos.add(step);
        }

        LivingEntity target = getTargetEntity();
        if (target != null) {
            if (lockY == Integer.MIN_VALUE) {
                lockX = target.getX();
                lockY = target.getY();
                lockZ = target.getZ();
            }

            Vec3 targetVec = evil ? new Vec3(lockX, lockY, lockZ) : VecHelper.fromEntityCenter(target);
            Vec3 diffVec = targetVec.subtract(thisVec);
            Vec3 motionVec = diffVec.normalize().scale(evil ? 0.5 : 0.6);
            setDeltaMovement(motionVec);
            if (time < 10) {
                setDeltaMovement(getDeltaMovement().x(), Math.abs(getDeltaMovement().y()), getDeltaMovement().z());
            }

            List<LivingEntity> targetList = level.getEntitiesOfClass(LivingEntity.class, new AABB(getX() - 0.5, getY() - 0.5, getZ() - 0.5, getX() + 0.5, getY() + 0.5, getZ() + 0.5));
            if (targetList.contains(target)) {
                Entity owner = getOwner();
                if (owner instanceof LivingEntity livingOwner) {
                    target.hurt(owner instanceof Player playerOwner ? DamageSource.playerAttack(playerOwner) : DamageSource.mobAttack(livingOwner), damage);
                } else {
                    target.hurt(DamageSource.GENERIC, damage);
                }

                discard();
            }

            if (evil && diffVec.length() < 1) {
                discard();
            }
        }

        time++;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag cmp) {
        super.addAdditionalSaveData(cmp);
        cmp.putInt(TAG_TIME, time);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag cmp) {
        super.readAdditionalSaveData(cmp);
        time = cmp.getInt(TAG_TIME);
    }

    public boolean findTarget() {
        LivingEntity target = getTargetEntity();
        if (target != null) {
            if (target.isAlive()) {
                return true;
            } else {
                target = null;
                setTarget(null);
            }
        }

        double range = 12;
        AABB bounds = new AABB(getX() - range, getY() - range, getZ() - range, getX() + range, getY() + range, getZ() + range);
        List<? extends LivingEntity> entities;
        if (isEvil()) {
            entities = level.getEntitiesOfClass(Player.class, bounds);
        } else {
            Entity owner = getOwner();
            Predicate<Entity> pred = EntitySelector.LIVING_ENTITY_STILL_ALIVE.and(targetPredicate(owner));
            entities = level.getEntitiesOfClass(LivingEntity.class, bounds, pred);
        }

        if (entities.size() > 0) {
            target = entities.get(level.random.nextInt(entities.size()));
            setTarget(target);
        }

        return target != null;
    }

    public static Predicate<Entity> targetPredicate(Entity owner) {
        return target -> target instanceof LivingEntity living && shouldTarget(owner, living);
    }

    public static boolean shouldTarget(Entity owner, LivingEntity e) {
        // always defend yourself
        if (e instanceof Mob mob && isHostile(owner, mob.getTarget())) {
            return true;
        }
        // don't target tamed creatures...
        if (e instanceof TamableAnimal animal && animal.isTame() || e instanceof AbstractHorse horse && horse.isTamed()) {
            return false;
        }

        // ...but other mobs die

        //TODO: Do stuff here
        return e instanceof Mob;
    }

    public static boolean isHostile(Entity owner, Entity attackTarget) {
        // if the owner can attack the target thru PvP...
        if (owner instanceof Player ownerPlayer && attackTarget instanceof Player targetedPlayer && ownerPlayer.canHarmPlayer(targetedPlayer)) {
            // ... then only defend self
            return owner == attackTarget;
        }
        // otherwise, kill any player-hostiles
        return attackTarget instanceof Player;
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult hit) {
        super.onHitBlock(hit);
        BlockState state = level.getBlockState(hit.getBlockPos());
        if (!level.isClientSide
                && !(state.getBlock() instanceof BushBlock)
                && !state.is(BlockTags.LEAVES)) {
            discard();
        }
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!level.isClientSide && hit.getEntity() == getTargetEntity()) {
            discard();
        }
    }


}
