package yerova.botanicpledge.common.blocks.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import yerova.botanicpledge.setup.BlockEntityInit;


public class RitualPedestalBlockEntity extends RitualBaseBlockEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    public RitualPedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityInit.RITUAL_PEDESTAL_BLOCK_ENTITY.get(), pos, blockState);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ritual_pedestal.idle", true));
        return PlayState.CONTINUE;
    }

}
