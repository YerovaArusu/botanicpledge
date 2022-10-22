package yerova.botanicpledge.common.items.relic;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.IRelic;
import vazkii.botania.common.item.relic.ItemRelic;
import vazkii.botania.common.item.relic.RelicImpl;
import yerova.botanicpledge.client.particle.ParticleColor;
import yerova.botanicpledge.client.particle.custom.ManaSweepParticleData;

public class YggdRamus extends ItemRelic {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public YggdRamus(Properties p) {
        super(p);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }



    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
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


        for(LivingEntity livingentity : player.level.getEntitiesOfClass(LivingEntity.class, this.getSweepHitBox(stack, player,entity ))) {
            if (livingentity != player && player.canHit(livingentity, 0)) { // Original check was dist < 3, range is 3, so vanilla used padding=0
                livingentity.knockback((double)0.4F, (double)Mth.sin(player.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(player.getYRot() * ((float)Math.PI / 180F))));
                livingentity.hurt(DamageSource.playerAttack(player), 10);
            }
        }
        this.sweepAttack(player.getLevel(), player);


        return super.onLeftClickEntity(stack, player, entity);
    }

    @NotNull
    @Override
    public AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        return player.getBoundingBox().inflate(3.0D, 1D, 3.0D);
    }

    public void sweepAttack(Level level, Player player) {
        double d0 = (double)(-Mth.sin(player.getYRot() * ((float)Math.PI / 180F)));
        double d1 = (double)Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
        if (level instanceof ServerLevel) {
            ((ServerLevel)level).sendParticles(ManaSweepParticleData.createData(new ParticleColor(66, 214, 227)), player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);
        }

    }

}
