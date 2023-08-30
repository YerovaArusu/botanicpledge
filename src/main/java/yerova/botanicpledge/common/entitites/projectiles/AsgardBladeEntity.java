package yerova.botanicpledge.common.entitites.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;
import vazkii.botania.common.proxy.IProxy;
import vazkii.botania.xplat.BotaniaConfig;
import yerova.botanicpledge.setup.BPEntities;

import java.util.Random;

public class AsgardBladeEntity extends EntityProjectileBase {

    private double damage = 1;

    public AsgardBladeEntity(EntityType<? extends EntityProjectileBase> type, Level worldIn) {
        super(type, worldIn);
    }

    public AsgardBladeEntity(Level world, LivingEntity thrower, LivingEntity target) {
        super(BPEntities.ASGARD_BLADE.get(), world, thrower);
        setThrower(thrower);
        setTargetPos(new BlockPos(target.getX(), target.getY(), target.getZ()));

        setTarget_id(target.getId());
        setVariety((int) (10 * Math.random()));
        setThrower(thrower);
        setDamage(10);
        this.setDeltaMovement(calculateBladeVelocity(getXRot(), getYRot()));

    }

    public AsgardBladeEntity(Level world, LivingEntity thrower, LivingEntity target, float damage) {
        super(BPEntities.ASGARD_BLADE.get(), world, thrower);
        setThrower(thrower);
        setTargetPos(new BlockPos(target.getX(), target.getY(), target.getZ()));
        setDamage(damage);
        setTarget_id(target.getId());
        setVariety((int) (10 * Math.random()));
        setThrower(thrower);
        this.setDeltaMovement(calculateBladeVelocity(getXRot(), getYRot()));

    }


    public static @NotNull AsgardBladeEntity getRegistry(EntityType<AsgardBladeEntity> EntityType, Level level) {
        return new AsgardBladeEntity(EntityType, level);
    }


    public static final int LIVE_TICKS = 60;

    private static final String TAG_VARIETY = "variety";
    private static final String TAG_DELAY = "delay";
    private static final String TAG_FAKE = "fake";
    private static final String TAG_TARGET_ENTITY_ID = "target_entity";
    private static final String TAG_ORIGIN_STACK = "target_entity";


    private static final EntityDataAccessor<Integer> VARIETY =
            SynchedEntityData.defineId(AsgardBladeEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DELAY =
            SynchedEntityData.defineId(AsgardBladeEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TARGET_ENTITY_ID =
            SynchedEntityData.defineId(AsgardBladeEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> FAKE =
            SynchedEntityData.defineId(AsgardBladeEntity.class, EntityDataSerializers.BOOLEAN);



    @Override
    protected void defineSynchedData() {
        entityData.define(VARIETY, 0);
        entityData.define(DELAY, 0);
        entityData.define(TARGET_ENTITY_ID, 0);
        entityData.define(FAKE, false);

        super.defineSynchedData();
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public void tick() {

        particles();

        if (getDelay() > 0) {
            setDelay(getDelay() - 1);
            return;
        }

        if (getTarget_id() != 0) {

            Entity target = level.getEntity(getTarget_id());
            if (target != null) {
                setTargetPos(target.getOnPos().above().above());
                faceEntity(target.getOnPos().above().above());
                this.setDeltaMovement(calculateBladeVelocity(getXRot(), getYRot()));
            } else {
                remove(RemovalReason.DISCARDED);
            }
        }

        if (getThrower() != null && getThrower() instanceof Player player) {
            AABB attackBox = this.getBoundingBox().inflate(2);
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, attackBox)) {

                entity.hurt(DamageSource.playerAttack(player), (float) getDamage());

                if (entity.getId() == this.getTarget_id()) {
                    this.remove(RemovalReason.DISCARDED);
                    break;
                }

            }
        }


        if (getFake()) {
            this.setDeltaMovement(0D, 0D, 0D);
            return;
        }

        if (getFake() || level.isClientSide && this.tickCount >= LIVE_TICKS && (
                getThrower() == null || getThrower().isRemoved()
                        || level.getEntity(getTarget_id()) == null) || (level.getEntity(getTarget_id()) != null && level.getEntity(getTarget_id()).isRemoved())) {
            remove(RemovalReason.DISCARDED);
            return;
        }


        super.tick();
    }


    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (getThrower() != null) {
            pResult.getEntity().hurt(DamageSource.playerAttack((Player) getThrower()), (float) getDamage());
        }
        if (level.getEntity(getTarget_id()).equals(pResult)) this.remove(RemovalReason.DISCARDED);
        super.onHitEntity(pResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        this.remove(RemovalReason.DISCARDED);
        super.onHitBlock(pResult);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag cmp) {
        super.addAdditionalSaveData(cmp);

        cmp.putInt(TAG_VARIETY, getVariety());
        cmp.putInt(TAG_DELAY, getDelay());
        cmp.putInt(TAG_TARGET_ENTITY_ID, getTarget_id());
        cmp.putBoolean(TAG_FAKE, getFake());

    }


    @Override
    public void readAdditionalSaveData(CompoundTag cmp) {
        super.readAdditionalSaveData(cmp);

        setVariety(cmp.getInt(TAG_VARIETY));
        setDelay(cmp.getInt(TAG_DELAY));
        setTarget_id(cmp.getInt(TAG_TARGET_ENTITY_ID));
        setFake(cmp.getBoolean(TAG_FAKE));


    }




    private void setTarget_id(int anInt) {
        entityData.set(TARGET_ENTITY_ID, anInt);
    }

    public int getTarget_id() {
        return entityData.get(TARGET_ENTITY_ID);
    }


    public int getVariety() {
        return entityData.get(VARIETY);
    }

    public void setVariety(int var) {
        entityData.set(VARIETY, var);
    }

    public int getDelay() {
        return entityData.get(DELAY);
    }

    public void setDelay(int var) {
        entityData.set(DELAY, var);
    }

    public boolean getFake() {
        return entityData.get(FAKE);
    }

    public void setFake(boolean rot) {
        entityData.set(FAKE, rot);
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDamage() {
        return this.damage;
    }

    public static Vec3 calculateBladeVelocity(float xRot, float yRot) {
        float f = 1F;
        double mx = Mth.sin(yRot / 180.0F * (float) Math.PI) * Mth.cos(xRot / 180.0F * (float) Math.PI) * f / 2D;
        double mz = -(Mth.cos(yRot / 180.0F * (float) Math.PI) * Mth.cos(xRot / 180.0F * (float) Math.PI) * f) / 2D;
        double my = Mth.sin(xRot / 180.0F * (float) Math.PI) * f / 2D;
        return new Vec3(-mx, -my, -mz);
    }

    public void particles() {
        if (!isAlive() || !level.isClientSide) {
            return;
        }

        int color = 0x08e8de;
        float r = (color >> 16 & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;
        float osize = 4;//getParticleSize();
        float size = osize;


        Player player = IProxy.INSTANCE.getClientPlayer();
        boolean depth = player == null || !ItemMonocle.hasMonocle(player);

        if (BotaniaConfig.client().subtlePowerSystem()) {
            WispParticleData data = WispParticleData.wisp(0.1F * size, r, g, b, depth);
            IProxy.INSTANCE.addParticleForceNear(level, data, getX(), getY(), getZ(), (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.01F);
        } else {
            float or = r;
            float og = g;
            float ob = b;

            double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b; // Standard relative luminance calculation

            double iterX = getX();
            double iterY = getY();
            double iterZ = getZ();

            Vec3 currentPos = position();
            Vec3 oldPos = new Vec3(xo, yo, zo);
            Vec3 diffVec = oldPos.subtract(currentPos);
            Vec3 diffVecNorm = diffVec.normalize();

            double distance = 0.095;

            do {
                if (luminance < 0.1) {
                    r = or + (float) Math.random() * 0.125F;
                    g = og + (float) Math.random() * 0.125F;
                    b = ob + (float) Math.random() * 0.125F;
                }
                size = osize + ((float) Math.random() - 0.5F) * 0.065F + (float) Math.sin(new Random(uuid.getMostSignificantBits()).nextInt(9001)) * 0.4F;
                WispParticleData data = WispParticleData.wisp(0.2F * size, r, g, b, depth);
                IProxy.INSTANCE.addParticleForceNear(level, data, iterX, iterY, iterZ,
                        (float) -getDeltaMovement().x() * 0.01F,
                        (float) -getDeltaMovement().y() * 0.01F,
                        (float) -getDeltaMovement().z() * 0.01F);

                iterX += diffVecNorm.x * distance;
                iterY += diffVecNorm.y * distance;
                iterZ += diffVecNorm.z * distance;

                currentPos = new Vec3(iterX, iterY, iterZ);
                diffVec = oldPos.subtract(currentPos);

            } while (Math.abs(diffVec.length()) > distance);

            WispParticleData data = WispParticleData.wisp(0.1F * size, or, og, ob, depth);
            level.addParticle(data, iterX, iterY, iterZ, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F);

        }
    }


    @NotNull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
