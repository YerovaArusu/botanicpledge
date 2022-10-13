package yerova.botanicpledge.common.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.handler.ModSounds;
import yerova.botanicpledge.client.render.items.YggdralScepterRenderer;
import yerova.botanicpledge.common.entitites.projectiles.EntityCorruptMagicMissile;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.YggdralScepterLeftClick;

import java.util.Random;
import java.util.function.Consumer;

public class YggdralScepter extends Item implements IAnimatable {

    public AnimationFactory factory = new AnimationFactory(this);

    public YggdralScepter(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.model.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            private final BlockEntityWithoutLevelRenderer renderer = new YggdralScepterRenderer();


            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    public static void summonCorruptMissile(ServerPlayer player) {
        Random random = new Random();

        EntityCorruptMagicMissile missile = new EntityCorruptMagicMissile(player, false, 40);
        missile.setPos(player.getX() + (random.nextDouble(0 + 4) - 2), player.getY() + (random.nextDouble(2 - 1) + 1), player.getZ() + (random.nextDouble(0 + 4) - 2));
        if (missile.findTarget()) {
            player.playSound(ModSounds.missile, 1F, 0.8F + (float) Math.random() * 0.2F);
            player.getLevel().addFreshEntity(missile);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (ManaItemHandler.instance().requestManaExact(stack, player, 10000, true)) {
            Networking.sendToServer(new YggdralScepterLeftClick());
        }


        return super.use(level, player, hand);
    }
}
