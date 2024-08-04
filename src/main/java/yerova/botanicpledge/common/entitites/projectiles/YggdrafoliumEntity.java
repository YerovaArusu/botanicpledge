package yerova.botanicpledge.common.entitites.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.item.equipment.bauble.ManaseerMonocleItem;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.xplat.BotaniaConfig;
import yerova.botanicpledge.common.items.relic.YggdRamus;
import yerova.botanicpledge.setup.BPEntities;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class YggdrafoliumEntity extends EntityProjectileBase {

    private static final String TAG_TICKS_EXISTED = "ticksExisted";
    private static final String TAG_COLOR = "color";
    private static final String TAG_GRAVITY = "gravity";
    private static final String TAG_HAS_SHOOTER = "hasShooter";
    private static final String TAG_SHOOTER = "shooterUUID";

    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(YggdrafoliumEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> GRAVITY = SynchedEntityData.defineId(YggdrafoliumEntity.class, EntityDataSerializers.FLOAT);
    private UUID shooterIdentity = null;
    private int _ticksExisted = 0;

    private float damage;
    private boolean shallExplode = true;

    public YggdrafoliumEntity(EntityType<YggdrafoliumEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    public YggdrafoliumEntity(Level world, LivingEntity thrower, BlockPos targetpos, float damage) {
        super(BPEntities.YGGDRAFOLIUM.get(), world, thrower);
        setTargetPos(targetpos);
        this.damage = damage;
        this.setOwner(thrower);
    }


    public static @NotNull YggdrafoliumEntity getRegistry(EntityType<YggdrafoliumEntity> EntityType, Level level) {
        return new YggdrafoliumEntity(EntityType, level);
    }


    public YggdrafoliumEntity(Level level, BlockPos pos, float rotX, float rotY, boolean fake) {
        this(BPEntities.YGGDRAFOLIUM.get(), level);
        moveTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0);
        /* NB: this looks backwards but it's right. spreaders take rotX/rotY to respectively mean
         * "rotation *parallel* to the X and Y axes", while vanilla's methods take XRot/YRot
         * to respectively mean "rotation *around* the X and Y axes".
         * TODO consider renaming our versions to match vanilla
         */
        setYRot(-(rotX + 90F));
        setXRot(rotY);
        setDeltaMovement(calculateBurstVelocity(getXRot(), getYRot()));
    }

    public YggdrafoliumEntity(Player player) {
        super(BPEntities.YGGDRAFOLIUM.get(), player.level(), player);

        setRot(player.getYRot() + 180, -player.getXRot());
        setDeltaMovement(calculateBurstVelocity(getXRot(), getYRot()));
    }


    @Override
    protected void defineSynchedData() {
        entityData.define(COLOR, 0);
        entityData.define(GRAVITY, 0F);

        super.defineSynchedData();
    }

    public static Vec3 calculateBurstVelocity(float xRot, float yRot) {
        float f = 0.4F;
        double mx = Mth.sin(yRot / 180.0F * (float) Math.PI) * Mth.cos(xRot / 180.0F * (float) Math.PI) * f / 2D;
        double mz = -(Mth.cos(yRot / 180.0F * (float) Math.PI) * Mth.cos(xRot / 180.0F * (float) Math.PI) * f) / 2D;
        double my = Mth.sin(xRot / 180.0F * (float) Math.PI) * f / 2D;
        return new Vec3(mx, my, mz);
    }

    @Override
    public void tick() {
        setTicksExisted(getTicksExisted() + 1);

        if (this.getTicksExisted() >= 160) discard();

        particles();
        super.tick();

        if (!level().isClientSide) {
            AABB axis = new AABB(position().x - 2F, position().y - 2F, position().z - 2F, position().x + 2F,
                    position().y + 2F, position().z + 2F);

            List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, axis);
            for (LivingEntity living : entities) {
                if (living.equals(getOwner())) continue;
                living.hurt(living.damageSources().generic(), damage);
            }
        }


    }

    @Override
    public boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> fluid, double mag) {
        return false;
    }

    @Override
    public boolean isInLava() {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {


        tag.putInt(TAG_TICKS_EXISTED, getTicksExisted());
        tag.putInt(TAG_COLOR, getColor());
        tag.putFloat(TAG_GRAVITY, getGravity());


        UUID identity = getShooterUUID();
        boolean hasShooter = identity != null;
        tag.putBoolean(TAG_HAS_SHOOTER, hasShooter);
        if (hasShooter) {
            tag.putUUID(TAG_SHOOTER, identity);
        }

        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag cmp) {

        setTicksExisted(cmp.getInt(TAG_TICKS_EXISTED));
        setColor(cmp.getInt(TAG_COLOR));
        setGravity(cmp.getFloat(TAG_GRAVITY));


        // Reread Motion because Entity.load clamps it to +/-10
        ListTag motion = cmp.getList("Motion", Tag.TAG_DOUBLE);
        setDeltaMovement(motion.getDouble(0), motion.getDouble(1), motion.getDouble(2));

        boolean hasShooter = cmp.getBoolean(TAG_HAS_SHOOTER);
        if (hasShooter) {
            UUID serializedUuid = cmp.getUUID(TAG_SHOOTER);
            UUID identity = getShooterUUID();
            if (!serializedUuid.equals(identity)) {
                setShooterUUID(serializedUuid);
            }
        }

        super.readAdditionalSaveData(cmp);
    }

    public void particles() {
        if (!isAlive() || !level().isClientSide) {
            return;
        }


        int color = getColor();
        float r = (color >> 16 & 0xFF) / 255F;
        float g = (color >> 8 & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;
        float osize = 4;//getParticleSize();
        float size = osize;


        Player player = Proxy.INSTANCE.getClientPlayer();
        boolean depth = player == null || !ManaseerMonocleItem.hasMonocle(player);

        if (BotaniaConfig.client().subtlePowerSystem()) {
            WispParticleData data = WispParticleData.wisp(0.1F * size, r, g, b, depth);
            Proxy.INSTANCE.addParticleForceNear(level(), data, getX(), getY(), getZ(), (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.02F, (float) (Math.random() - 0.5F) * 0.01F);
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
                Proxy.INSTANCE.addParticleForceNear(level(), data, iterX, iterY, iterZ,
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
            level().addParticle(data, iterX, iterY, iterZ, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F, (float) (Math.random() - 0.5F) * 0.06F);
        }

    }


    @Override
    protected void onHitBlock(@Nonnull BlockHitResult hit) {
        onHitCommon(hit);

    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult hit) {

        if (hit.getEntity() == getOwner()) {
            return;
        } else {
            hit.getEntity().hurt(level().damageSources().playerAttack((Player) getOwner()), damage);
            if (getOwner() instanceof Player player && player.getMainHandItem().getItem() instanceof YggdRamus) {
                YggdRamus.appendFireAspect(player, hit.getEntity());
            }
        }

        onHitCommon(hit);
    }

    private void onHitCommon(HitResult hit) {

        if (isAlive()) {
            if (level().isClientSide) {
                int color = getColor();
                float r = (color >> 16 & 0xFF) / 255F;
                float g = (color >> 8 & 0xFF) / 255F;
                float b = (color & 0xFF) / 255F;

                if (!BotaniaConfig.client().subtlePowerSystem()) {
                    for (int i = 0; i < 4; i++) {
                        WispParticleData data = WispParticleData.wisp(0.15F, r, g, b);
                        level().addParticle(data, getX(), getY(), getZ(), (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F, (float) (Math.random() - 0.5F) * 0.04F);
                    }
                }
                SparkleParticleData data = SparkleParticleData.sparkle((float) 4, r, g, b, 2);
                level().addParticle(data, getX(), getY(), getZ(), 0, 0, 0);
            }

            explodeAndDie();
        }
    }

    @Override
    public float getGravity() {
        return entityData.get(GRAVITY);
    }


    public void setGravity(float gravity) {
        entityData.set(GRAVITY, gravity);
    }

    public int getColor() {
        return entityData.get(COLOR);
    }

    public void setColor(int color) {
        entityData.set(COLOR, color);
    }

    public void setTicksExisted(int ticks) {
        _ticksExisted = ticks;
    }

    public int getTicksExisted() {
        return _ticksExisted;
    }


    public void setShooterUUID(UUID uuid) {
        shooterIdentity = uuid;
    }


    public UUID getShooterUUID() {
        return shooterIdentity;
    }

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }


    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    private void explodeAndDie() {
        if (!level().isClientSide && isExplosive()) {
            level().explode(this, getX(), getY(), getZ(), 2F, Level.ExplosionInteraction.NONE);
            discard();
        }
    }

    public void setExplosive(boolean explosive) {
        this.shallExplode = explosive;
    }

    public boolean isExplosive() {
        return this.shallExplode;
    }
}
