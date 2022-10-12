package yerova.botanicpledge.common.items.block_items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import yerova.botanicpledge.client.render.items.ritual_pedestal_block_item.RitualPedestalBlockItemRenderer;

import java.util.function.Consumer;

public class RitualPedestalBlockItem extends BlockItem implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);
    public RitualPedestalBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new RitualPedestalBlockItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

}
