package yerova.botanicpledge.common.items.block_items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.common.item.CustomCreativeTabContents;
import yerova.botanicpledge.common.aura_node.AuraImplementation;
import yerova.botanicpledge.common.aura_node.AuraNodeType;
import yerova.botanicpledge.common.aura_node.essence.Essence;
import yerova.botanicpledge.common.blocks.block_entities.essence.AuraNodeBlockEntity;
import yerova.botanicpledge.setup.BPBlocks;
import yerova.botanicpledge.setup.BPEssences;

public class AuraNodeBlockItem extends BlockItem implements CustomCreativeTabContents {

    public AuraNodeBlockItem(Properties props) {
        super(BPBlocks.AURA_NODE.get(), props);
    }

    @Override
    public void addToCreativeTab(Item me, CreativeModeTab.Output output) {


        for (Essence rEssence : Essence.getRegisteredEssences()) {
            if (rEssence.equals(BPEssences.EMPTY_ESSENCE.get())) continue;
            ItemStack stack = new ItemStack(me);
            AuraImplementation auraData = new AuraImplementation();

            auraData.setBaseEssence(rEssence, 1);
            auraData.setEssenceAmount(rEssence, 10);
            auraData.setType(AuraNodeType.UNSTABLE);

            stack.addTagElement("Aura", auraData.toNBT());
            output.accept(stack);
        }
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack stack, BlockState state) {
        if (level.getBlockEntity(pos) instanceof AuraNodeBlockEntity entity) {
            if (stack.hasTag() && stack.getTag().contains("Aura")) {

                entity.auraData = AuraImplementation.fromNBT(entity.auraData,stack.getTag().getCompound("Aura"));
                entity.setChanged();
            }
        }
        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }


}
