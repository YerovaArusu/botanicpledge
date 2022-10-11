package yerova.botanicpledge.common.blocks.block_entities;

import com.google.common.base.Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.IManaSpark;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.common.block.tile.mana.IThrottledPacket;
import vazkii.botania.common.block.tile.mana.TilePool;

import java.util.List;

public class ManaYggdralBufferBlockEntity extends BlockEntity implements IAnimatable, IManaReceiver, ISparkAttachable, IThrottledPacket {
    public static final int MAX_MANA = 264000000;
    public static final int TRANSFER_SPEED = 16000;
    private static final BlockPos[] POOL_LOCATIONS = {new BlockPos(1, 0, 0), new BlockPos(0, 0, 1),
            new BlockPos(-1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, -1, 0)};
    private static final String TAG_MANA = "mana";
    private static boolean sendPacket = false;
    private final AnimationFactory factory = new AnimationFactory(this);
    private int mana;

    public ManaYggdralBufferBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.MANA_YGGDRAL_BUFFER_BLOCK_ENTITY.get(), blockPos, blockState);

    }

    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, E e) {
        if (level.getBlockEntity(blockPos) instanceof ManaYggdralBufferBlockEntity) {
            for (BlockPos poolPos : POOL_LOCATIONS) {
                if (level.getBlockEntity(blockPos.offset(poolPos)) instanceof TilePool) {
                    TilePool tilePool = (TilePool) level.getBlockEntity(blockPos.offset(poolPos));
                    int manaToGet = Math.min(TRANSFER_SPEED, tilePool.getCurrentMana());
                    int spaceLeft = Math.max(0, MAX_MANA - tilePool.getCurrentMana());
                    int current = Math.min(spaceLeft, manaToGet);
                    tilePool.receiveMana(-current);
                    ((ManaYggdralBufferBlockEntity) level.getBlockEntity(blockPos)).receiveMana(current);
                } else if (level.getBlockEntity(blockPos.offset(poolPos)) instanceof ManaYggdralBufferBlockEntity) {
                    ManaYggdralBufferBlockEntity buffer = (ManaYggdralBufferBlockEntity) level.getBlockEntity(blockPos.offset(poolPos));
                    int manaToGet = Math.min(TRANSFER_SPEED, buffer.getCurrentMana());
                    int spaceLeft = Math.max(0, MAX_MANA - buffer.getCurrentMana());
                    int current = Math.min(spaceLeft, manaToGet);
                    buffer.receiveMana(-current);
                    ((ManaYggdralBufferBlockEntity) level.getBlockEntity(blockPos)).receiveMana(current);
                }
            }

            if (level.getBlockEntity(blockPos.offset(0, 1, 0)) instanceof TilePool) {
                ManaYggdralBufferBlockEntity sender = ((ManaYggdralBufferBlockEntity) level.getBlockEntity(blockPos));
                TilePool receiver = (TilePool) level.getBlockEntity(blockPos.offset(0, 1, 0));
                int manaToGet = Math.min(TRANSFER_SPEED, sender.getCurrentMana());
                int space = Math.max(0, receiver.manaCap - receiver.getCurrentMana());
                int current = Math.min(space, manaToGet);

                sender.receiveMana(-current);
                receiver.receiveMana(current);
            } else if (level.getBlockEntity(blockPos.offset(0, 1, 0)) instanceof ManaYggdralBufferBlockEntity) {
                ManaYggdralBufferBlockEntity sender = ((ManaYggdralBufferBlockEntity) level.getBlockEntity(blockPos));
                ManaYggdralBufferBlockEntity receiver = (ManaYggdralBufferBlockEntity) level.getBlockEntity(blockPos.offset(0, 1, 0));
                int manaToGet = Math.min(TRANSFER_SPEED, sender.getCurrentMana());
                int space = Math.max(0, MAX_MANA - receiver.getCurrentMana());
                int current = Math.min(space, manaToGet);

                sender.receiveMana(-current);
                receiver.receiveMana(current);
            }

        }


    }

    //Animations with GeckoLib
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
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mana_yggdral_buffer.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public Level getManaReceiverLevel() {
        return level;
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return worldPosition;
    }

    @Override
    public void load(CompoundTag tag) {
        mana = tag.getInt(TAG_MANA);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt(TAG_MANA, mana);
    }


    @Override
    public boolean canAttachSpark(ItemStack stack) {
        return true;
    }

    @Override
    public void attachSpark(IManaSpark entity) {
        ISparkAttachable.super.attachSpark(entity);
    }

    @Override
    public int getAvailableSpaceForMana() {
        int space = Math.max(0, MAX_MANA - getCurrentMana());
        if (space > 0) {
            return space;
        } else {
            return 0;
        }
    }

    @Override
    public IManaSpark getAttachedSpark() {

        List<Entity> sparks = level.getEntitiesOfClass(Entity.class, new AABB(worldPosition.above(), worldPosition.above().offset(1, 1, 1)), Predicates.instanceOf(IManaSpark.class));
        if (sparks.size() == 1) {
            Entity e = sparks.get(0);
            return (IManaSpark) e;
        }

        return null;
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return false;
    }

    @Override
    public boolean isFull() {
        return getCurrentMana() >= MAX_MANA;
    }

    @Override
    public void receiveMana(int mana) {
        int old = this.mana;
        this.mana = Math.max(0, Math.min(getCurrentMana() + mana, MAX_MANA));
        if (old != this.mana) {
            markDispatchable();
        }
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    @Override
    public int getCurrentMana() {
        return mana;
    }

    @Override
    public void markDispatchable() {
        sendPacket = true;
    }
}
