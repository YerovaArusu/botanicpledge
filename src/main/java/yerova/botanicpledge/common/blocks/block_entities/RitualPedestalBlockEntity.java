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

    public RitualPedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(BPBlockEntities.RITUAL_PEDESTAL.get(), pos, blockState);
    }

    @Override
    public void load(CompoundTag compound) {
        isActivated = compound.getBoolean("is_activated");
        animationTick = compound.getInt("animation_tick");
        prevAnimationTick = animationTick;
        ritualCenterPos = compound.contains("ritual_center_pos") ? BlockPos.of(compound.getLong("ritual_center_pos")) : BlockPos.ZERO;

        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putBoolean("is_activated", isActivated);
        tag.putInt("animation_tick", animationTick);
        tag.putLong("ritual_center_pos", ritualCenterPos.asLong());
        super.saveAdditional(tag);
    }


    public float getAnimationProgress(float partialTicks) {
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


        // prüfen, ob noch ein RitualCenter existiert
        boolean hadCenter = !pedestal.ritualCenterPos.equals(BlockPos.ZERO);

        if (hadCenter) {
            if (level.getBlockEntity(pedestal.ritualCenterPos) instanceof RitualCenterBlockEntity ritualCenter) {
                pedestal.isActivated = ritualCenter.isActivated;
            } else {
                pedestal.ritualCenterPos = BlockPos.ZERO;
                pedestal.isActivated = false;
            }
        } else {
            pedestal.isActivated = false;
        }


        // Animation hoch oder runter zählen
        pedestal.prevAnimationTick = pedestal.animationTick;

        if (pedestal.isActivated) {
            pedestal.animationTick = Math.min(pedestal.animationTick + 1, ANIMATION_TIME);
        } else {
            pedestal.animationTick = Math.max(pedestal.animationTick - 1, 0);
        }




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
}
