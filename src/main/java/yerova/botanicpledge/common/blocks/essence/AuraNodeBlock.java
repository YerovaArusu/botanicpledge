package yerova.botanicpledge.common.blocks.essence;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import yerova.botanicpledge.common.aura_node.AuraNodeType;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.blocks.block_entities.essence.AuraNodeBlockEntity;
import yerova.botanicpledge.setup.BPBlockEntities;
import yerova.botanicpledge.setup.BPEssences;

public class AuraNodeBlock extends BaseEntityBlock {



    public AuraNodeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {

        AuraNodeBlockEntity entity = new AuraNodeBlockEntity(pPos, pState);

        Essence rEssence = Essence.getRandomEssence(BPEssences.EMPTY_ESSENCE.get());
        entity.auraData.setBaseEssence(rEssence,1);
        entity.auraData.setEssenceAmount(rEssence,10);
        entity.auraData.setType(AuraNodeType.CHAOTIC);

        return entity;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(pHand) == ItemStack.EMPTY) return InteractionResult.PASS;
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

        ItemStack stack = pPlayer.getItemInHand(pHand);

        if (Essence.isEssence(stack) && blockEntity instanceof AuraNodeBlockEntity entity) {

            Essence essence = Essence.getEssence(stack);

            entity.auraData.setBaseEssence(essence,1);
            entity.auraData.setEssenceAmount(essence,10);
            entity.auraData.setType(AuraNodeType.CHAOTIC);

        }

        blockEntity.setChanged();

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Nullable
    @Override
    public <T extends
            BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BPBlockEntities.AURA_NODE.get(),
                AuraNodeBlockEntity::tick);
    }


}
