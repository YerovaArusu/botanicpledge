package yerova.botanicpledge.common.blocks.block_entities.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.aura_node.AuraNodeType;
import yerova.botanicpledge.common.aura_node.IAuraNode;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.setup.BPBlockEntities;

import javax.annotation.Nullable;
import java.util.List;

public class AuraNodeBlockEntity extends BlockEntity implements IAuraNode {

    public AuraImplementation auraData = new AuraImplementation();
    public static final int RANGE = 8;

    public static final int CHAOS_INTERVAL = 10;



    public AuraNodeBlockEntity( BlockPos pos, BlockState state) {
        super(BPBlockEntities.AURA_NODE.get(), pos, state);
    }

    @Override
    public AuraImplementation getImplementation() {
        return auraData;
    }

    @Override
    public void setImplementation(AuraImplementation implementation) {
        auraData = implementation;
    }


    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("Aura")) {
            auraData.copyFrom(AuraImplementation.fromNBT(compound.getCompound("Aura")));
        }
    }



    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("Aura", auraData.toNBT());
        super.saveAdditional(tag);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AuraNodeBlockEntity entity) {
        if (level.isClientSide()) return; // nur Server
        if (entity.auraData == null) return;
        if (entity.auraData.getType() == null) return;

        if (!entity.auraData.getType().equals(AuraNodeType.CHAOTIC)) return;



        if (level.getGameTime() % CHAOS_INTERVAL == 0) {
            BlockPos randomPos = getRandomSurfaceBlock(level, blockPos, RANGE);
            if (randomPos.equals(blockPos)) return;
            if (level.getBlockEntity(randomPos) instanceof IAuraNode) return;

            BlockState suckedState = level.getBlockState(randomPos);
            if (!level.removeBlock(randomPos, false)) return;

            FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, randomPos, suckedState);

            Vec3 start = Vec3.atCenterOf(randomPos);
            Vec3 target = Vec3.atCenterOf(blockPos);
            Vec3 dir = target.subtract(start).normalize();

            float resistance = suckedState.getBlock().getExplosionResistance();
            double speed = 1.0 / (0.5 + resistance);

            fallingBlock.setDeltaMovement(dir.scale(speed));
            fallingBlock.time = 1;
            fallingBlock.dropItem = false;

            level.addFreshEntity(fallingBlock);
        }

        List<Entity> entities = level.getEntities(null, new AABB(blockPos).inflate(RANGE));
        Vec3 center = Vec3.atCenterOf(blockPos);

        for (Entity e : entities) {
            if (e instanceof Player player && (player.isCreative() || player.isSpectator())) continue;

            Vec3 dir = center.subtract(e.position());
            double dist = dir.length();


            Vec3 motion = dir.normalize().scale(0.3);

            e.setDeltaMovement(e.getDeltaMovement().add(motion));

            if (dist < 0.4) {

                e.hurt(level.damageSources().magic(), 1);


                if (e instanceof FallingBlockEntity fallingBlockEntity) {
                    fallingBlockEntity.kill();
                    entity.auraData.addEssence(Essence.getRandomEssence(),1);
                }
            }
            e.hurtMarked = true;
        }
    }


    public static BlockPos getRandomSurfaceBlock(Level level, BlockPos pos, int range) {
        int rX = level.random.nextInt(range * 2) - range;
        int rZ = level.random.nextInt(range * 2) - range;


        BlockPos tPos = pos.offset(rX, 0, rZ);

        for (int dy = 1; dy <= range; dy++) {
            BlockPos up = tPos.above(dy);
            BlockPos down = tPos.below(dy);

            if (!up.equals(pos) && isAirExposed(level, up)) {
                return up;
            }
            if (!down.equals(pos) && isAirExposed(level, down)) {
                return down;
            }
        }

        return tPos;
    }

    private static boolean isAirExposed(Level level, BlockPos pos) {
        if (level.getBlockState(pos).isAir()) return false;
        for (Direction dir : Direction.values()) {
            if (level.getBlockState(pos.relative(dir)).isAir()) {
                return true;
            }
        }
        return false;
    }


    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getTag() == null ? new CompoundTag() : pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }


}
