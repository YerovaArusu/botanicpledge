package yerova.botanicpledge.common.blocks.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.aura_node.essence.EssenceTransportableImplementation;
import yerova.botanicpledge.common.blocks.block_entities.ModificationAltarBlockEntity;
import yerova.botanicpledge.common.blocks.block_entities.essence.EssenceTransporterBlockEntity;
import yerova.botanicpledge.common.utils.ShapeRotations;
import yerova.botanicpledge.setup.BPBlockEntities;

import java.util.HashMap;

import static yerova.botanicpledge.common.blocks.block_entities.essence.EssenceTransporterBlockEntity.mapRelativeToWorld;

public class EssenceTransporter extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty EXTRACT = BooleanProperty.create("extract");

    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 3, 12);

    public EssenceTransporter(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(EXTRACT, false));
    }

    @Nullable
    @Override
    public <T extends
            BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BPBlockEntities.ESSENCE_TRANSPORTER.get(),
                EssenceTransporterBlockEntity::tick);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {

        if (pLevel.getBlockEntity(pPos) instanceof EssenceTransporterBlockEntity entity) {

            EssenceTransportableImplementation transportable =  entity.getTransportable();

            HashMap<Direction, EssenceTransportableImplementation.TransferType> map = new HashMap<>();

            for (Direction direction :transportable.getTransferTypeMap().keySet()) {
                Direction relative = mapRelativeToWorld(entity.getFacing().getOpposite(),direction);
                map.put(relative, transportable.getTransferTypeMap().get(direction));
            }

            transportable.setTransferTypeMap(map);


        }



        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new EssenceTransporterBlockEntity(pPos, pState);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return ShapeRotations.rotateFromUp(SHAPE,state.getValue(FACING));
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(EXTRACT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(EXTRACT, false);
    }


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
