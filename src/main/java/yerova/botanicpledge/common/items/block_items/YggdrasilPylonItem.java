package yerova.botanicpledge.common.items.block_items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import yerova.botanicpledge.client.render.blocks.YggdrasilPylonRenderer;
import yerova.botanicpledge.setup.BPBlocks;

import java.util.function.Consumer;

public class YggdrasilPylonItem extends BlockItem {


    public YggdrasilPylonItem(Properties pProperties) {
        super(BPBlocks.YGGDRASIL_PYLON.get(), pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new YggdrasilPylonRenderer.ItemRenderer();
            }
        });
    }
}
