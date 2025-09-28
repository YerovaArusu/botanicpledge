package yerova.botanicpledge.common.blocks.block_entities.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.item.WandOfTheForestItem;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.aura_node.essence.EssenceCapacitorImplementation;
import yerova.botanicpledge.common.aura_node.essence.EssenceTransportableImplementation;
import yerova.botanicpledge.common.blocks.essence.EssenceTransporter;
import yerova.botanicpledge.setup.BPBlockEntities;
import yerova.botanicpledge.setup.BPEssences;

import javax.annotation.Nullable;

public class EssenceTransporterBlockEntity extends EssenceCapableBlockEntityBase implements Wandable, WandBindable {

    boolean isExtractMode = false;
    BlockPos boundBlockPos = null;

    boolean willTransport = false;
    public int burstColor = 0xFFFFFF;
    public int burstColorTime = 0;

    public static final int FIXED_TRANSFER_RATE = 6;

    public EssenceTransporterBlockEntity(BlockPos pos, BlockState state) {
        super(BPBlockEntities.ESSENCE_TRANSPORTER.get(), pos, state);

        transportable.getTransferTypeMap().clear();

        capacitor.setMaxCapacity(10);

        if (this.isExtractMode) {
            transportable.addIO(Direction.DOWN, EssenceTransportableImplementation.TransferType.IN);
        } else {
            transportable.addIO(Direction.DOWN, EssenceTransportableImplementation.TransferType.OUT);
        }
    }


    @Override
    public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {

        if (WandOfTheForestItem.getBindMode(stack)) {
            //TODO: Add binding logic
        } else {
            if (player.isShiftKeyDown()) {
                this.isExtractMode = !this.isExtractMode;
                transportable.getTransferTypeMap().clear();
                if (this.isExtractMode) {
                    transportable.addIO(Direction.DOWN, EssenceTransportableImplementation.TransferType.IN);
                } else {
                    transportable.addIO(Direction.DOWN, EssenceTransportableImplementation.TransferType.IN);
                }
            }
        }

        return true;
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, EssenceTransporterBlockEntity entity) {

        if (level.isClientSide()) {
            renderStaticNode(entity);
            if (entity.willTransport) renderTransferParticles(entity);
        } else {
            blockState = blockState.setValue(EssenceTransporter.EXTRACT, entity.isExtractMode);
            level.setBlock(pos, blockState, 3);
            entity.updateBlock();
        }

        entity.willTransport = false;
        entity.essenceTick(level, pos, blockState, entity);
        if(entity.boundBlockPos == null) return;
        if(entity.boundBlockPos.equals(entity.getBlockPos())) return;
        if(!(level.getBlockEntity(entity.boundBlockPos) instanceof EssenceTransporterBlockEntity otherBe)) return;
        if(!(entity.isExtractMode && !otherBe.isExtractMode)) return;

        EssenceCapacitorImplementation thisImp = entity.getImplementation();
        EssenceCapacitorImplementation otherImp = otherBe.getImplementation();

        if (thisImp == null || otherImp == null) return;

        Essence transferEssence = thisImp.getFirstEssence();
        if (transferEssence == null || transferEssence.equals(BPEssences.EMPTY_ESSENCE.get())) return;
        if (!otherImp.getEssenceList().containsEssence(transferEssence) && otherImp.getEssenceList().getEssenceTypes().size() >= otherImp.getMaxEssenceAmount()) return;

        int actualTransferRate = Math.min(Math.min(thisImp.getEssenceAmount(transferEssence), FIXED_TRANSFER_RATE), otherImp.getMaxCapacity() - otherImp.getEssenceCount());

        /*
        if (entity.burstColor != 0xFFFFFF && entity.burstColor == transferEssence.getColor()) {
            entity.burstColorTime = Math.min(entity.burstColorTime+1, MAX_COLOR_DURATION);
            if (entity.burstColorTime == MAX_COLOR_DURATION) {
                entity.burstColorTime = 0;
                entity.burstColor = 0xFFFFFF;
            }
        }else if (entity.burstColor != transferEssence.getColor()) {
            entity.burstColorTime = 0;
            entity.burstColor = transferEssence.getColor();
        }

        if (otherBe.burstColor != 0xFFFFFF && otherBe.burstColor == transferEssence.getColor()) {
            otherBe.burstColorTime = Math.min(otherBe.burstColorTime+1, MAX_COLOR_DURATION);
            if (otherBe.burstColorTime == MAX_COLOR_DURATION) {
                otherBe.burstColorTime = 0;
                otherBe.burstColor = 0xFFFFFF;
            }
        } else if (otherBe.burstColor != transferEssence.getColor()) {
            otherBe.burstColorTime = 0;
            otherBe.burstColor = transferEssence.getColor();

         */

        entity.burstColor = transferEssence.getColor();
        otherBe.burstColor = transferEssence.getColor();

        if (actualTransferRate <= 0) {
            entity.willTransport = false;
            return;
        } else entity.willTransport = true;


        if (level.getGameTime() % 20 == 0) {
            thisImp.removeEssence(transferEssence,actualTransferRate);
            otherImp.addEssence(transferEssence, actualTransferRate);
        }

    }


    public boolean updateBlock() {
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
            setChanged();
            return true;
        }
        return false;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        isExtractMode = compound.getBoolean("extract_state");
        burstColor = compound.getInt("burst_color");
        willTransport = compound.getBoolean("will_transport");
        burstColorTime = compound.getInt("burst_color_time");


        if (compound.contains("bound_block_pos")) {
            boundBlockPos = BlockPos.of(compound.getLong("bound_block_pos"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putBoolean("extract_state", isExtractMode);
        tag.putInt("burst_color", burstColor);
        tag.putBoolean("will_transport", willTransport);
        tag.putInt("burst_color_time", burstColorTime);

        if (boundBlockPos != null) {
            tag.putLong("bound_block_pos", boundBlockPos.asLong());
        }
    }

    @Override
    public boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side) {
        return true;
    }

    @Override
    public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
        if (level == null) return false;

        if (level.getBlockEntity(pos) instanceof EssenceTransporterBlockEntity be) {
            boundBlockPos = pos;

            if (be.boundBlockPos != this.getBlockPos()) {
                be.boundBlockPos = this.getBlockPos();
            }

            return true;
        }

        return false;
    }

    public boolean isExtractMode() {
        return isExtractMode;
    }


    @Override
    public @org.jetbrains.annotations.Nullable BlockPos getBinding() {
        return boundBlockPos;
    }

    public Direction getFacing() {
        return getBlockState().getValue(EssenceTransporter.FACING);
    }

    public static Direction mapRelativeToWorld(Direction facing, Direction relative) {
        /*
        // Wenn "relative" UP oder DOWN ist → bleibt gleich (Rotation betrifft nur Horizontal-Ebene)
        if (relative == Direction.UP || relative == Direction.DOWN) {
            return relative;
        }
         */

        return Direction.from3DDataValue(
                (relative.get3DDataValue() + facing.get3DDataValue()) % 4
        );
    }

    public BlockPos getBoundBlockPos() {
        return boundBlockPos;
    }

    public static void renderTransferParticles(EssenceTransporterBlockEntity entity) {
        if (entity.getBoundBlockPos() == null) return;
        if (!entity.isExtractMode()) return;
        if (entity.getLevel() == null) return;
        if (!entity.getLevel().isClientSide()) return;


        Level world = entity.getLevel();

        Vec3 start = Vec3.atCenterOf(entity.getBlockPos());
        Vec3 end   = Vec3.atCenterOf(entity.getBoundBlockPos());

        Vec3 diff = end.subtract(start);
        double length = diff.length();
        if (length < 0.01) return;

        Vec3 dir = diff.normalize();


        int color = entity.burstColor;


        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        int count = (int) (length * 4);
        for (int i = 0; i < count; i++) {
            double t = (i + world.random.nextDouble()) / count;
            Vec3 pos = start.add(dir.scale(length * t));

            double wave = Math.sin(t * Math.PI * 6 + world.getGameTime() * 0.3) * 0.15;
            Vec3 side = new Vec3(dir.z, 0, -dir.x).normalize().scale(wave);
            pos = pos.add(side);

            if (world.random.nextFloat() < 0.3) {
                double rc = 0.1;
                pos = pos.add(
                        (world.random.nextDouble() - 0.5) * rc,
                        (world.random.nextDouble() - 0.5) * rc,
                        (world.random.nextDouble() - 0.5) * rc
                );
            }

            Vec3 motion = end.subtract(pos).scale(0.05 + 0.03 * t);

            float progress = (float)t;
            float r2 = r * (1.0f - 0.3f * progress);
            float g2 = g * (1.0f - 0.3f * progress);
            float b2 = b * (1.0f - 0.3f * progress);

            float pulse = (float)(0.9 + 0.2 * Math.sin(world.getGameTime() * 0.2 + t * 12));
            float size = 0.08F + 0.12F * pulse;

            WispParticleData data = WispParticleData.wisp(size, r2, g2, b2).withNoClip(true);
            world.addAlwaysVisibleParticle(data, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
        }
    }

    public static void renderStaticNode(EssenceTransporterBlockEntity entity) {

        if (entity.getLevel() == null) return;
        if (!entity.getLevel().isClientSide()) return;

        int color = entity.burstColor;


        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        double x = entity.worldPosition.getX();
        double y = entity.worldPosition.getY();
        double z = entity.worldPosition.getZ();

        switch (entity.getFacing()) {
            case UP -> {
                y += 0.6; x+= 0.5; z+= 0.5;
            }
            case DOWN -> {
                y += 0.4; x+= 0.5; z+= 0.5;
            }
            case NORTH -> {
                y+= 0.5; x+= 0.5; z+= 0.4;
            }
            case SOUTH -> {
                y+= 0.5; x+= 0.5; z+= 0.6;
            }
            case WEST -> {
                y+= 0.5; x+= 0.4; z+= 0.5;
            }
            case EAST -> {
                y+= 0.5; x+= 0.6; z+= 0.5;
            }
        }

        float size = 0.3f;
        if(r == 1 && g == 1 && b == 1) size = 0.1f;

        WispParticleData data = WispParticleData.wisp(size, r, g, b).withNoClip(true);
        entity.getLevel().addAlwaysVisibleParticle(data, x, y, z, 0,0,0);
    }
}