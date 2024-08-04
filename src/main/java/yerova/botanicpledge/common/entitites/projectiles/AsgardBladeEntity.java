package yerova.botanicpledge.common.entitites.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.item.equipment.bauble.ManaseerMonocleItem;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.xplat.BotaniaConfig;
import yerova.botanicpledge.common.entitites.yggdrasilguardian.YggdrasilGuardian;
import yerova.botanicpledge.setup.BPEntities;

import javax.annotation.Nullable;
import java.util.*;

public class AsgardBladeEntity extends EntityProjectileBase {

    private double damage = 1;
    private final Set<Integer> hurtEntities = new HashSet<>();
    @Nullable
    private UUID targetUUID;
    @Nullable
    private Entity target;

    public static final int LIVE_TICKS = 60;

    public AsgardBladeEntity(EntityType<? extends EntityProjectileBase> type, Level worldIn) {
        super(type, worldIn);
    }

    public AsgardBladeEntity(Level world, LivingEntity thrower, BlockPos targetPos, float damage) {
        super(BPEntities.ASGARD_BLADE.get(), world, thrower);
        setOwner(thrower);
        setTargetPos(targetPos);
        setDamage(damage);
        this.setDeltaMovement(calculateBladeVelocity(getXRot(), getYRot()));
    }

    public AsgardBladeEntity(Level world, LivingEntity thrower, LivingEntity target, float damage) {
        super(BPEntities.ASGARD_BLADE.get(), world, thrower);
        setOwner(thrower);
        setTargetPos(new BlockPos((int) target.getX(), (int) target.getY(), (int) target.getZ()));
        setDamage(damage);
        setTargetEntity(target);
        this.setDeltaMovement(calculateBladeVelocity(getXRot(), getYRot()));
    }

    public static @NotNull AsgardBladeEntity getRegistry(EntityType<AsgardBladeEntity> EntityType, Level level) {
        return new AsgardBladeEntity(EntityType, level);
    }


    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    public void setTargetEntity(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.targetUUID = pOwner.getUUID();
            this.target = pOwner;
        }
    }

    @Nullable
    public Entity getTargetEntity() {
        if (this.target != null && !this.target.isRemoved()) {
            return this.target;
        } else if (this.targetUUID != null && this.level() instanceof ServerLevel) {
            this.target = ((ServerLevel) this.level()).getEntity(this.targetUUID);
            return this.target;
        } else {
            return null;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            clientTick();
        } else {
            serverTick();
        }

    }


    private void clientTick() {

        if (shouldDiscard()) {
            remove(RemovalReason.DISCARDED);
            return;
        }
        particles();
    }

    private void serverTick() {
        if (getTargetEntity() != null) {
            setTargetPos(getTargetEntity().getOnPos().above(2).getCenter());
        } else {
            BlockPos targetPos = getTargetPos();
            if (hasReachedTarget(targetPos)) {
                remove(RemovalReason.DISCARDED);
            }
        }

        facePosition(getTargetPos());
        this.setDeltaMovement(calculateBladeVelocity(getXRot(), getYRot()));
        handleEntityCollisions();
    }

    private boolean shouldDiscard() {
        return getAliveTicks() >= (getOwner() instanceof YggdrasilGuardian ? LIVE_TICKS *4 : LIVE_TICKS)
                && (getOwner() == null || getOwner().isRemoved() || getTargetEntity() == null)
                || (getTargetEntity() != null && getTargetEntity().isRemoved());
    }


    @Override
    public boolean canChangeDimensions() {
        return false;
    }


    private boolean hasReachedTarget(BlockPos targetPos) {
        return ((int) position().x) == targetPos.getX()
                && ((int) position().y) == targetPos.getY()
                && ((int) position().z) == targetPos.getZ();
    }

    private void handleEntityCollisions() {
        AABB attackBox = this.getBoundingBox().inflate(2);
        for (Entity entity : level().getEntitiesOfClass(LivingEntity.class, attackBox)) {
            if (isValidTarget(entity)) {
                damageEntity(getOwner(), entity);
            }
        }

    }

    private boolean isValidTarget(Entity entity) {
        return entity != getOwner() && !(entity instanceof AsgardBladeEntity) && !hurtEntities.contains(entity.getId());
    }

    private void damageEntity(Entity attacker, Entity entity) {
        if (attacker instanceof Player player) {
            entity.hurt(level().damageSources().playerAttack(player), (float) getDamage());
        } else if(attacker instanceof LivingEntity living) {
            entity.hurt(level().damageSources().mobAttack(living), (float)getDamage());
        }

        hurtEntities.add(entity.getId());
        if (entity.equals(getTargetEntity())) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (isValidEntityHit(pResult)) {
            damageHitEntity(pResult);
        }
        if (isTargetEntityHit(pResult)) {
            this.remove(RemovalReason.DISCARDED);
        }
        super.onHitEntity(pResult);
    }

    private boolean isValidEntityHit(EntityHitResult pResult) {
        return getOwner() != null && pResult.getEntity() != getOwner() && !(pResult.getEntity() instanceof AsgardBladeEntity);
    }

    private void damageHitEntity(EntityHitResult pResult) {
        if (getOwner() instanceof Player player) {
            pResult.getEntity().hurt(level().damageSources().playerAttack(player), (float) getDamage());
        } else if (getOwner() instanceof LivingEntity entity) {
            pResult.getEntity().hurt(level().damageSources().mobAttack(entity), (float) getDamage());
        }
    }

    private boolean isTargetEntityHit(EntityHitResult pResult) {
        return getTargetEntity() != null && Objects.equals(getTargetEntity(), pResult.getEntity());
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        this.remove(RemovalReason.DISCARDED);
        super.onHitBlock(pResult);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag cmp) {
        super.addAdditionalSaveData(cmp);
        if (this.targetUUID != null) {
            cmp.putUUID("target", this.targetUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag cmp) {
        super.readAdditionalSaveData(cmp);
        if (cmp.hasUUID("target")) {
            this.targetUUID = cmp.getUUID("target");
            this.target = null;
        }
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
        if (!isAlive() || !level().isClientSide) {
            return;
        }
        int color = 0x08e8de;
        float r = (color >> 16 & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;
        float osize = 4;
        float size = osize;

        Player player = Proxy.INSTANCE.getClientPlayer();
        boolean depth = player == null || !ManaseerMonocleItem.hasMonocle(player);

        if (BotaniaConfig.client().subtlePowerSystem()) {
            createWispParticle(0.1F * size, r, g, b, depth);
        } else {
            createComplexParticles(r, g, b, osize, depth);
        }
    }

    private void createWispParticle(float size, float r, float g, float b, boolean depth) {
        WispParticleData data = WispParticleData.wisp(size, r, g, b, depth);
        Proxy.INSTANCE.addParticleForceNear(level(), data, getX(), getY(), getZ(), randomOffset(), randomOffset(), randomOffset());
    }

    private float randomOffset() {
        return (float) (Math.random() - 0.5F) * 0.02F;
    }

    private void createComplexParticles(float r, float g, float b, float osize, boolean depth) {
        float or = r;
        float og = g;
        float ob = b;

        double luminance = calculateLuminance(r, g, b);
        Vec3 iterPos = position();
        Vec3 oldPos = new Vec3(xo, yo, zo);
        Vec3 diffVec = oldPos.subtract(iterPos);
        Vec3 diffVecNorm = diffVec.normalize();

        double distance = 0.095;

        do {
            if (luminance < 0.1) {
                r = adjustColor(or);
                g = adjustColor(og);
                b = adjustColor(ob);
            }
            float size = osize + randomSizeOffset();
            WispParticleData data = WispParticleData.wisp(0.2F * size, r, g, b, depth);
            Proxy.INSTANCE.addParticleForceNear(level(), data, iterPos.x, iterPos.y, iterPos.z,
                    -getDeltaMovement().x() * 0.01F,
                    -getDeltaMovement().y() * 0.01F,
                    -getDeltaMovement().z() * 0.01F);

            iterPos = updatePosition(iterPos, diffVecNorm, distance);
            diffVec = oldPos.subtract(iterPos);

        } while (Math.abs(diffVec.length()) > distance);

        WispParticleData data = WispParticleData.wisp(0.1F * osize, or, og, ob, depth);
        level().addParticle(data, iterPos.x, iterPos.y, iterPos.z, randomOffset(), randomOffset(), randomOffset());
    }

    private double calculateLuminance(float r, float g, float b) {
        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }

    private float adjustColor(float color) {
        return color + (float) Math.random() * 0.125F;
    }

    private float randomSizeOffset() {
        return ((float) Math.random() - 0.5F) * 0.065F + (float) Math.sin(new Random(uuid.getMostSignificantBits()).nextInt(9001)) * 0.4F;
    }

    private Vec3 updatePosition(Vec3 iterPos, Vec3 diffVecNorm, double distance) {
        return new Vec3(iterPos.x + diffVecNorm.x * distance,
                iterPos.y + diffVecNorm.y * distance,
                iterPos.z + diffVecNorm.z * distance);
    }
}
