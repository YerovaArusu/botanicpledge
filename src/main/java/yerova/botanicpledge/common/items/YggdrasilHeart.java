package yerova.botanicpledge.common.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
import yerova.botanicpledge.common.blocks.block_entities.YggdrasilPylonBlockEntity;
import yerova.botanicpledge.setup.BPItems;

public class YggdrasilHeart extends Item {
    public YggdrasilHeart(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        var level = pContext.getLevel();
        var pos = pContext.getClickedPos();
        var itemStack = pContext.getItemInHand();

        if (level.getBlockEntity(pos) instanceof YggdrasilPylonBlockEntity pylon) {
            if (!pylon.canGenerateMana() && itemStack.is(BPItems.YGGDRASIL_HEART.get())) {
                pylon.setCanGenerateMana(true);
                itemStack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
