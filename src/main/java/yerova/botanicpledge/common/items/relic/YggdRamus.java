package yerova.botanicpledge.common.items.relic;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.common.entitites.projectiles.YggdFocus;
import yerova.botanicpledge.common.entitites.projectiles.YggdrafoliumEntity;
import yerova.botanicpledge.common.utils.LeftClickable;

import java.util.List;

import static vazkii.botania.common.item.equipment.tool.ToolCommons.raytraceFromEntity;

public class YggdRamus extends ItemRelic implements LeftClickable {

    public final int MANA_COST_PER_SHOT = 4000;
    public final int SUMMON_AMOUNT_PER_CLICK = 4;


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
        } else attackEntity(player, null);
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();

        Vec3 targetPos = player.position().add(player.getLookAngle().scale(5D));
        YggdFocus focus = new YggdFocus(pContext.getLevel(), player);
        focus.setPos(targetPos.x, targetPos.y + 2, targetPos.z);
        if (!pContext.getLevel().isClientSide)
            pContext.getLevel().addFreshEntity(focus);
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

        return super.onLeftClickEntity(stack, player, entity);
    }


    @Override
    public void LeftClick(Level level, Player player, ItemStack stack) {

    }

    public static List<LivingEntity> getEntitiesAround(BlockPos source, float range, Level world) {
        return world.getEntitiesOfClass(LivingEntity.class,
                new AABB(source.getX() + 0.5 - range, source.getY() + 0.5 - range, source.getZ() + 0.5 - range,
                        source.getX() + 0.5 + range, source.getY() + 0.5 + range, source.getZ() + 0.5 + range));
    }

    public void attackEntity(LivingEntity player, Entity target) {
        BlockPos targetpos = target == null ? raytraceFromEntity(player, 80F, true).getBlockPos().offset(0, 1, 0) : new BlockPos(target.getX(), target.getY(), target.getZ()).offset(0, 1, 0);

        double range = 4D;
        double j = -Math.PI + 2 * Math.PI * Math.random();
        double k;
        double x, y, z;
        for (int i = 0; i < this.SUMMON_AMOUNT_PER_CLICK - 1; i++) {
            if (ManaItemHandler.instance().requestManaExact(player.getMainHandItem(), ((Player) player), MANA_COST_PER_SHOT, true)) {
                YggdrafoliumEntity sword = new YggdrafoliumEntity(player.level, player, targetpos);
                k = 0.12F * Math.PI * Math.random() + 0.28F * Math.PI;
                x = player.getX() + range * Math.sin(k) * Math.cos(j);
                y = player.getY() + range * Math.cos(k);
                z = player.getZ() + range * Math.sin(k) * Math.sin(j);
                j += 2 * Math.PI * Math.random() * 0.08F + 2 * Math.PI * 0.17F;
                sword.setPos(x, y, z);
                sword.faceTarget(1.0F);

                sword.setColor(0x08e8de);
                sword.setStartingMana(MANA_COST_PER_SHOT);
                sword.setMinManaLoss(1);
                sword.setManaLossPerTick(1F);
                sword.setMana(MANA_COST_PER_SHOT);
                sword.setGravity(0F);
                sword.setDeltaMovement(sword.getDeltaMovement().scale(0.8));

                sword.setSourceLens(player.getItemInHand(player.getUsedItemHand()).copy());

                player.level.addFreshEntity(sword);
            }
        }
    }

}
