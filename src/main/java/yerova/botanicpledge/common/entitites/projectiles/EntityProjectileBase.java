package yerova.botanicpledge.common.entitites.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityProjectileBase extends ThrowableProjectile {

    private static final String TAG_ROTATION = "rotation";
    private static final String TAG_PITCH = "pitch";
    private static final String TAG_TARGETPOS = "targetpos";
    private static final String TAG_TARGETPOSX = "targetposx";
    private static final String TAG_TARGETPOSY = "targetposy";
    private static final String TAG_TARGETPOSZ = "targetposz";

    private static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(EntityProjectileBase.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(EntityProjectileBase.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<BlockPos> TARGET_POS = SynchedEntityData.defineId(EntityProjectileBase.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Float> TARGET_POS_X = SynchedEntityData.defineId(EntityProjectileBase.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_POS_Y = SynchedEntityData.defineId(EntityProjectileBase.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> TARGET_POS_Z = SynchedEntityData.defineId(EntityProjectileBase.class, EntityDataSerializers.FLOAT);

    private LivingEntity thrower;

    public EntityProjectileBase(EntityType<? extends ThrowableProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityProjectileBase(EntityType<? extends ThrowableProjectile> type, Level worldIn, LivingEntity thrower) {
        super(type, worldIn);
        this.thrower = thrower;
        this.setNoGravity(true);
    }

    @Nullable
    public LivingEntity getThrower() {
        return this.thrower;
    }

    public void setThrower(LivingEntity entity) {
        this.thrower = entity;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(ROTATION, 0F);
        entityData.define(PITCH, 0F);
        entityData.define(TARGET_POS, BlockPos.ZERO);
        entityData.define(TARGET_POS_X, 0F);
        entityData.define(TARGET_POS_Y, 0F);
        entityData.define(TARGET_POS_Z, 0F);
    }

    public void faceTargetAccurately(float modifier) {
        this.faceEntity(this.getTargetPosX(), this.getTargetPosY(), this.getTargetPosZ());
        Vec3 vec = new Vec3(getTargetPosX() - getX(), getTargetPosY() - getY(), getTargetPosZ() - getZ())
                .normalize();
        this.setDeltaMovement(vec.x * modifier, vec.y * modifier, vec.z * modifier);
    }

    public void faceTarget(float modifier) {
        this.faceEntity(this.getTargetPos());
        Vec3 vec = new Vec3(this.getTargetPos().getX() - this.getX(), this.getTargetPos().getY() - this.getY(), this.getTargetPos().getZ() - this.getZ())
                .normalize();
        this.setDeltaMovement(vec.x * modifier, vec.y * modifier, vec.z * modifier);
    }

    public void setTargetPos(Vec3 vec) {
        this.setTargetPosX((float) vec.x);
        this.setTargetPosY((float) vec.y);
        this.setTargetPosZ((float) vec.z);
        this.setTargetPos(new BlockPos(vec));
    }

    public void faceEntity(float vx, float vy, float vz) {
        double dX = vx - this.getX();
        double dZ = vz - this.getZ();
        double dY = vy - this.getY();

        double d3 = Math.sqrt(dX * dX + dZ * dZ);
        float f = (float) (Math.atan2(dZ, dX) * (180D / Math.PI)) - 90.0F;
        float f1 = (float) (-(Math.atan2(dY, d3) * (180D / Math.PI)));

        //YRot = Rotation Yaw
        this.setYRot(this.updateRotation(this.getYRot(), f, 360F));
        this.setRotation(Mth.wrapDegrees(-this.getYRot() + 180));


        //XRot = Rotation Pitch
        this.setXRot(this.updateRotation(this.getXRot(), f1, 360));
        this.setPitch(-this.getXRot());

    }

    public void faceEntity(BlockPos target) {
        double d0 = target.getX() - this.getX();
        double d2 = target.getZ() - this.getZ();
        double d1 = target.getY() - this.getY();

        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float f = (float) (Math.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
        float f1 = (float) (-(Math.atan2(d1, d3) * (180D / Math.PI)));

        //YRot = Rotation Yaw
        this.setYRot(this.updateRotation(this.getYRot(), f, 360F));
        this.setRotation(Mth.wrapDegrees(-this.getYRot() + 180));

        //XRot = Rotation Pitch
        this.setXRot(this.updateRotation(this.getXRot(), f1, 360));
        this.setPitch(-this.getXRot());

    }

    private float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = Mth.wrapDegrees(targetAngle - angle);

        if (f > maxIncrease) {
            f = maxIncrease;
        }

        if (f < -maxIncrease) {
            f = -maxIncrease;
        }

        return angle + f;
    }


    @Override
    public void addAdditionalSaveData(CompoundTag cmp) {

        cmp.putFloat(TAG_ROTATION, this.getRotation());
        cmp.putFloat(TAG_PITCH, this.getPitch());
        cmp.putLong(TAG_TARGETPOS, this.getTargetPos().asLong());
        cmp.putFloat(TAG_TARGETPOSX, this.getTargetPosX());
        cmp.putFloat(TAG_TARGETPOSY, this.getTargetPosY());
        cmp.putFloat(TAG_TARGETPOSZ, this.getTargetPosZ());


        super.addAdditionalSaveData(cmp);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag cmp) {

        setRotation(cmp.getFloat(TAG_ROTATION));
        setPitch(cmp.getFloat(TAG_PITCH));
        setTargetPos(BlockPos.of(cmp.getLong(TAG_TARGETPOS)));
        setTargetPosX(cmp.getFloat(TAG_TARGETPOSX));
        setTargetPosY(cmp.getFloat(TAG_TARGETPOSY));
        setTargetPosZ(cmp.getFloat(TAG_TARGETPOSZ));

        super.readAdditionalSaveData(cmp);
    }


    public float getRotation() {
        return entityData.get(ROTATION);
    }

    public void setRotation(float rot) {
        entityData.set(ROTATION, rot);
    }

    public float getPitch() {
        return entityData.get(PITCH);
    }

    public void setPitch(float rot) {
        entityData.set(PITCH, rot);
    }

    public BlockPos getTargetPos() {
        return entityData.get(TARGET_POS);
    }

    public void setTargetPos(BlockPos pos) {
        entityData.set(TARGET_POS, pos);
    }

    public float getTargetPosX() {
        return entityData.get(TARGET_POS_X);
    }

    public void setTargetPosX(float f) {
        entityData.set(TARGET_POS_X, f);
    }

    public float getTargetPosY() {
        return entityData.get(TARGET_POS_Y);
    }

    public void setTargetPosY(float f) {
        entityData.set(TARGET_POS_Y, f);
    }

    public float getTargetPosZ() {
        return entityData.get(TARGET_POS_Z);
    }

    public void setTargetPosZ(float f) {
        entityData.set(TARGET_POS_Z, f);
    }

    @NotNull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


}
