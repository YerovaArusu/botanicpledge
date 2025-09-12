package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.client.fx.WispParticleData;
import yerova.botanicpledge.common.blocks.RitualCenterBlock;
import yerova.botanicpledge.setup.BPBlockEntities;

public class RitualPedestalBlockEntity extends RitualBaseBlockEntity {

    public boolean isActivated = false;
    public BlockPos ritualCenterPos = BlockPos.ZERO;
    public static final int ANIMATION_TIME = 40;

    public int animationTick = 0;
    private int prevAnimationTick = 0;

    // Rotation (für smooth rotation)
    public float rotationYaw = 0f;
    public float prevRotationYaw = 0f;
    private float targetRotationYaw = 0f;

    public RitualPedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(BPBlockEntities.RITUAL_PEDESTAL.get(), pos, blockState);
    }

    @Override
    public void load(CompoundTag compound) {
        isActivated = compound.getBoolean("is_activated");
        animationTick = compound.getInt("animation_tick");
        prevAnimationTick = animationTick;
        ritualCenterPos = compound.contains("ritual_center_pos") ? BlockPos.of(compound.getLong("ritual_center_pos")) : BlockPos.ZERO;

        rotationYaw = compound.contains("rotation_yaw") ? compound.getFloat("rotation_yaw") : 0f;
        prevRotationYaw = rotationYaw;
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putBoolean("is_activated", isActivated);
        tag.putInt("animation_tick", animationTick);
        tag.putLong("ritual_center_pos", ritualCenterPos.asLong());
        tag.putFloat("rotation_yaw", rotationYaw);
        super.saveAdditional(tag);
    }


    public float getAnimationProgress(float partialTicks) {
        // falls die Animation vollständig ist, gib exakte Endwerte zurück (kein partialTicks)
        if (animationTick == 0 && prevAnimationTick == 0) return 0f;
        if (animationTick == ANIMATION_TIME && prevAnimationTick == ANIMATION_TIME) return 1f;

        float interp = prevAnimationTick + (animationTick - prevAnimationTick) * partialTicks;
        float progress = interp / (float) ANIMATION_TIME;
        // clamp extra-sicher
        if (progress < 0f) return 0f;
        if (progress > 1f) return 1f;
        return progress;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, RitualPedestalBlockEntity pedestal) {

        // prevRotation für Interpolation vorm Update setzen
        pedestal.prevRotationYaw = pedestal.rotationYaw;

        // prüfen, ob noch ein RitualCenter existiert
        boolean hadCenter = !pedestal.ritualCenterPos.equals(BlockPos.ZERO);
        boolean centerActive = false;
        if (hadCenter) {
            if (level.getBlockEntity(pedestal.ritualCenterPos) instanceof RitualCenterBlockEntity ritualCenter) {
                pedestal.isActivated = ritualCenter.isActivated;
                centerActive = ritualCenter.isCrafting; // ACHTUNG: Annahme, dass RitualCenterBlockEntity.isCrafting() existiert
            } else {
                pedestal.ritualCenterPos = BlockPos.ZERO;
                pedestal.isActivated = false;
            }
        } else {
            pedestal.isActivated = false;
        }


        // Animation hoch oder runter zählen
        if (pedestal.isActivated) {
            pedestal.animationTick = Math.min(pedestal.animationTick + 1, ANIMATION_TIME);
        } else {
            pedestal.animationTick = Math.max(pedestal.animationTick - 1, 0);
        }


        // --- Rotation-Zielberechnung ---
        // Nur Pedestal dreht sich; nur wenn center vorhanden und center.isCrafting()==true
        if (hadCenter && centerActive) {
            // Zielwinkel zur Mitte berechnen (Mitte = ritualCenterPos)
            double dx = pedestal.ritualCenterPos.getX() - pedestal.getBlockPos().getX();
            double dz = pedestal.ritualCenterPos.getZ() - pedestal.getBlockPos().getZ();
            // atan2(dz, dx) -> Richtung, -90 um Modellorientierung zu korrigieren
            float desired = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90f;
            pedestal.targetRotationYaw = desired;
        } else {
            // wenn kein crafting, wir können optional langsam zurückdrehen (z.B. zu 0)
            pedestal.targetRotationYaw = pedestal.targetRotationYaw; // oder 0f, wenn du Reset willst
        }

        // Sanfte Annäherung an targetRotationYaw (wrap korrekt behandeln)
        float diff = wrapDegrees(pedestal.targetRotationYaw - pedestal.rotationYaw);
        float step = diff * 0.2f; // 0.2 = Geschwindigkeit; anpassbar (kleiner = langsamer)
        pedestal.rotationYaw = pedestal.rotationYaw + step;


        if (level.isClientSide) {
            return;
        }

        if (pedestal.isActivated && pedestal.animationTick > 0 && level instanceof ServerLevel serverLevel) {
            float progress = pedestal.animationTick / (float) ANIMATION_TIME;

            // Hex: 0x08e8de
            float r = 8 / 255f;
            float g = 232 / 255f;
            float b = 222 / 255f;
            
            int particleCount = (int) (1 + progress *2);

            double cx = blockPos.getX() + 0.5;
            double cy = blockPos.getY() + 0.6875; // Height: 11/16
            double cz = blockPos.getZ() + 0.5;

            for (int i = 0; i < particleCount; i++) {
                double dx = (Math.random() - 0.01) * 0.01; // kleine Streuung
                double dy = (Math.random() - 0.01) * 0.01;
                double dz = (Math.random() - 0.01) * 0.01;

                WispParticleData data = WispParticleData.wisp(
                        0.05F + progress * 0.2F, // Größe nimmt mit progress zu
                        r, g, b,
                        true
                );

                serverLevel.sendParticles(
                        data,
                        cx, cy, cz,
                        1, // immer 1 pro Loop
                        dx, dy, dz,
                        0.01
                );
            }
        }
    }

    public float getInterpolatedYaw(float partialTicks) {
        float diff = wrapDegrees(rotationYaw - prevRotationYaw);
        return prevRotationYaw + diff * partialTicks;
    }

    private static float wrapDegrees(float angle) {
        // normalisiere in (-180, 180]
        angle %= 360f;
        if (angle >= 180f) angle -= 360f;
        if (angle < -180f) angle += 360f;
        return angle;
    }

}
