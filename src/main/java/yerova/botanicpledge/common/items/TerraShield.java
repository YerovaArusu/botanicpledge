package yerova.botanicpledge.common.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.LensEffectItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.entity.ManaBurstEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraBladeItem;
import yerova.botanicpledge.client.render.entities.ShieldBlockEntityWithoutLevelRenderer;
import yerova.botanicpledge.setup.BPItems;

import java.util.List;
import java.util.function.Consumer;

public class TerraShield extends ShieldItem implements LensEffectItem {

    public static final int MANA_PER_DAMAGE = 30;

    public TerraShield(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.is(BotaniaItems.terrasteel);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 7200;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player) {
            if (!world.isClientSide && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, MANA_PER_DAMAGE * 4, true)) {
                stack.setDamageValue(stack.getDamageValue() - 1);
            }
        }
    }

    public static void onShieldBlock(Level level, LivingEntity defender, LivingEntity attacker,ItemStack useItem) {
        if (level.isClientSide) return;


        if (defender instanceof Player player) {
            if (!player.isSpectator() && !player.getUseItem().isEmpty() && useItem.is(BPItems.TERRA_SHIELD.get())) {
                ManaBurstEntity burst = TerraBladeItem.getBurst(player, player.getMainHandItem());

                float rotX = (float) Math.atan(player.getX()+1/attacker.getX()+1);
                float rotY = (float) Math.atan((player.getY())/(attacker.getY()));

                burst.setYRot(-(rotX + 90F));
                burst.setXRot(rotY);


                burst.setColor(0x20FF20);
                burst.setMana(MANA_PER_DAMAGE);
                burst.setStartingMana(MANA_PER_DAMAGE);
                burst.setMinManaLoss(40);
                burst.setManaLossPerTick(4F);
                burst.setGravity(0F);
                burst.setDeltaMovement(burst.getDeltaMovement().scale(7f));

                burst.setSourceLens(useItem.copy());

                player.level().addFreshEntity(burst);

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.terraBlade, SoundSource.PLAYERS, 1F, 1F);
            }

        }


    }

    @SoftImplement("IForgeItem")
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @SoftImplement("IForgeItem")
    private boolean reequipAnimation(ItemStack before, ItemStack after) {
        return !after.is(this);
    }



    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ShieldBlockEntityWithoutLevelRenderer.instance;
            }
        });
    }


    @Override
    public void apply(ItemStack stack, BurstProperties props, Level level) {

    }

    @Override
    public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
        return shouldKill;
    }

    @Override
    public void updateBurst(ManaBurst burst, ItemStack stack) {
        ThrowableProjectile entity = burst.entity();
        AABB axis = new AABB(entity.getX(), entity.getY(), entity.getZ(), entity.xOld, entity.yOld, entity.zOld).inflate(1);
        List<LivingEntity> entities = entity.level().getEntitiesOfClass(LivingEntity.class, axis);
        Entity thrower = entity.getOwner();

        for (LivingEntity living : entities) {
            if (living == thrower || living instanceof Player livingPlayer && thrower instanceof Player throwingPlayer
                    && !throwingPlayer.canHarmPlayer(livingPlayer)) {
                continue;
            }

            if (living.hurtTime == 0) {
                int cost = MANA_PER_DAMAGE / 3;
                int mana = burst.getMana();
                if (mana >= cost) {
                    burst.setMana(mana - cost);
                    float damage = 4F + BotaniaAPI.instance().getTerrasteelItemTier().getAttackDamageBonus();
                    if (!burst.isFake() && !entity.level().isClientSide) {
                        DamageSource source = living.damageSources().magic();
                        if (thrower instanceof Player player) {
                            source = player.damageSources().playerAttack(player);
                        } else if (thrower instanceof LivingEntity livingEntity) {
                            source = livingEntity.damageSources().mobAttack(livingEntity);
                        }
                        living.hurt(source, damage);
                        entity.discard();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean doParticles(ManaBurst burst, ItemStack stack) {
        return true;
    }
}
