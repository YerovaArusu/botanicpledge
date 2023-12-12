package yerova.botanicpledge.common.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.common.handler.EquipmentHandler;
import vazkii.botania.common.item.equipment.bauble.BaubleItem;
import vazkii.botania.common.item.equipment.bauble.CirrusAmuletItem;
import vazkii.botania.common.item.equipment.bauble.SojournersSashItem;
import vazkii.botania.common.proxy.Proxy;
import vazkii.botania.xplat.XplatAbstractions;


import java.util.UUID;

public class ConqueringSashItem extends BaubleItem {

    private static final UUID STEP_BOOST_UUID = UUID.fromString("fbdc4ac7-57c8-425f-ba2d-f8c487f535c1");
    private static final AttributeModifier STEP_BOOST = new AttributeModifier(
            STEP_BOOST_UUID,
            "botanicpledge:conquering_belt",
            2.6, AttributeModifier.Operation.ADDITION);

    private static final int COST = 16;
    private static final int COST_INTERVAL = 10;

    public final float speed;
    public final float jump;
    public final float fallBuffer;
    public ConqueringSashItem(Properties props, float speed, float jump, float fallBuffer) {
        super(props);
        this.speed = speed;
        this.jump = jump;
        this.fallBuffer = fallBuffer;
    }

    public ConqueringSashItem(Properties props) {
        this(props, 1.2F, 0.8F, 8F);
    }

    public static float onPlayerFall(Player entity, float dist) {
        boolean pendantJump = CirrusAmuletItem.popJumping(entity);
        ItemStack stack = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ConqueringSashItem, entity);

        if (!stack.isEmpty()) {
            float fallBuffer = ((ConqueringSashItem) stack.getItem()).fallBuffer;

            if (pendantJump) {
                ItemStack amulet = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof CirrusAmuletItem, entity);
                if (!amulet.isEmpty()) {
                    fallBuffer *= ((CirrusAmuletItem) amulet.getItem()).getMaxAllowedJumps();
                }
            }

            return Math.max(0, dist - fallBuffer);
        }
        return dist;
    }

    @Override
    public void onWornTick(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player) {
            ItemStack belt = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ConqueringSashItem, player);

            var stepHeight = XplatAbstractions.INSTANCE.getStepHeightAttribute();
            AttributeInstance attrib = player.getAttribute(stepHeight);
            boolean hasBoost = attrib.hasModifier(STEP_BOOST);

            if (tryConsumeMana(player)) {
                if (player.level().isClientSide) {
                    ConqueringSashItem beltItem = (ConqueringSashItem) belt.getItem();
                    if ((player.onGround() || player.getAbilities().flying) && player.zza > 0F && !player.isInWaterOrBubble()) {
                        float speed = beltItem.getSpeed(belt);
                        player.moveRelative(player.getAbilities().flying ? speed : speed, new Vec3(0, 0, 1));
                        beltItem.onMovedTick(belt, player);

                        if (player.tickCount % COST_INTERVAL == 0) {
                            ManaItemHandler.instance().requestManaExact(belt, player, COST, true);
                        }
                    } else {
                        beltItem.onNotMovingTick(belt, player);
                    }
                } else {
                    if (player.isShiftKeyDown()) {
                        if (hasBoost) {
                            attrib.removeModifier(STEP_BOOST);
                        }
                    } else {
                        if (!hasBoost) {
                            attrib.addTransientModifier(STEP_BOOST);
                        }
                    }
                }
            } else if (!player.level().isClientSide && hasBoost) {
                attrib.removeModifier(STEP_BOOST);
            }
        }
    }


    public float getSpeed(ItemStack stack) {
        return speed;
    }

    public void onMovedTick(ItemStack stack, Player player) {}

    public void onNotMovingTick(ItemStack stack, Player player) {}


    public static void onPlayerJump(LivingEntity living) {
        if (living instanceof Player player) {
            ItemStack belt = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ConqueringSashItem, player);

            if (!belt.isEmpty() && ManaItemHandler.instance().requestManaExact(belt, player, COST, false)) {
                player.setDeltaMovement(player.getDeltaMovement().add(0, ((ConqueringSashItem) belt.getItem()).jump, 0));
            }
        }
    }

    private static boolean tryConsumeMana(Player player) {
        ItemStack result = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof ConqueringSashItem, player);
        return !result.isEmpty() && ManaItemHandler.instance().requestManaExact(result, player, COST, false);
    }

}
