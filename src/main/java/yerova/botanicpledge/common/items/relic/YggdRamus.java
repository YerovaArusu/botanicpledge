package yerova.botanicpledge.common.items.relic;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.client.particle.custom.ManaSweepParticleData;
import yerova.botanicpledge.common.entitites.projectiles.YggdFocus;
import yerova.botanicpledge.common.utils.LeftClickable;

import java.util.List;

public class YggdRamus extends ItemRelic implements LeftClickable {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public YggdRamus(Properties p) {
        super(p);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) -2.9F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (player.isShiftKeyDown()) {
            if (player.isOnGround()) {
                for (LivingEntity entity : YggdRamus.getEntitiesAround(player.getOnPos(), 6, level)) {
                    entity.setDeltaMovement(entity.getDeltaMovement().add(0, 1D, 0));
                }
                if (!level.isClientSide)
                    for (int i = 0; i < 360; i += 30) {
                        double r = 3D;
                        double x = player.getX() + r * Math.cos(Math.toRadians(i));
                        double y = player.getY() + 0.5D;
                        double z = player.getZ() + r * Math.sin(Math.toRadians(i));
                        for (int j = 0; j < 6; j++)
                            level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0.12F * j, 0);
                    }
            }
        } else {
/*            Vec3 targetVec = player.position().add(player.getLookAngle().scale(6));

            BlockHitResult result = new BlockHitResult(player.getLookAngle(), player.getDirection(), new BlockPos(targetVec), true);


            Sheep sheep = new Sheep(EntityType.SHEEP, level);
            sheep.setPos(Vec3.atCenterOf(result.getBlockPos().offset(0, 1, 0)));
            level.addFreshEntity(sheep);*/
        }


        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();

        Vec3 targetPos = player.position().add(player.getLookAngle().scale(5D));
        YggdFocus fvoid = new YggdFocus(pContext.getLevel(), player);
        fvoid.setPos(targetPos.x, targetPos.y+2, targetPos.z);
        if(!pContext.getLevel().isClientSide)
            pContext.getLevel().addFreshEntity(fvoid);
        player.getCooldowns().addCooldown(this, 40);
        return InteractionResult.SUCCESS;
    }

    public static IRelic makeRelic(ItemStack stack) {
        return new RelicImpl(stack, null);
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        this.sweepAttack(player.getLevel(), player, 0.4F);
        return super.onLeftClickEntity(stack, player, entity);
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


    @Override
    public void LeftClick(Level level, Player player, ItemStack stack) {
        this.sweepAttack(level, player, 0.4F);
    }

    public static List<LivingEntity> getEntitiesAround(BlockPos source, float range, Level world) {
        return world.getEntitiesOfClass(LivingEntity.class,
                new AABB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range,
                        source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
    }
}
