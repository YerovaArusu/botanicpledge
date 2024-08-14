package yerova.botanicpledge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.StoneOfTemperanceItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import vazkii.botania.common.item.relic.RingOfThorItem;
import yerova.botanicpledge.common.items.relic.RingOfAesir;

import java.util.function.Predicate;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mixin(TerraShattererItem.class)
public class MixinTerraShattererItem {
    @Inject(at = @At(value = "HEAD"), method = "breakOtherBlock", cancellable = true, remap = false)
    private void onBreakOtherBlock(Player player, ItemStack stack, BlockPos pos, BlockPos originPos, Direction side, CallbackInfo ci) {

        TerraShattererItem l = (TerraShattererItem) ((Object) this);

        if (!l.isEnabled(stack)) {
            return;
        }

        Level world = player.level();
        Predicate<BlockState> canMine = state -> {
            boolean rightToolForDrops = !state.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(state);
            boolean rightToolForSpeed = stack.getDestroySpeed(state) > 1
                    || state.is(BlockTags.MINEABLE_WITH_SHOVEL)
                    || state.is(BlockTags.MINEABLE_WITH_HOE);
            return rightToolForDrops && rightToolForSpeed;
        };

        BlockState targetState = world.getBlockState(pos);
        if (!canMine.test(targetState)) {
            return;
        }

        if (world.isEmptyBlock(pos)) {
            return;
        }

        boolean thor = !RingOfThorItem.getThorRing(player).isEmpty() || !RingOfAesir.getAesirRing(player).isEmpty();
        boolean doX = thor || side.getStepX() == 0;
        boolean doY = thor || side.getStepY() == 0;
        boolean doZ = thor || side.getStepZ() == 0;

        int origLevel = l.getLevel(stack);
        int level = origLevel + (thor ? 1 : 0);
        if (StoneOfTemperanceItem.hasTemperanceActive(player) && level > 2) {
            level = 2;
        }

        int range = level - 1;
        int rangeY = Math.max(1, range);

        if (range == 0 && level != 1) {
            return;
        }

        Vec3i beginDiff = new Vec3i(doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0);
        Vec3i endDiff = new Vec3i(doX ? range : 0, doY ? rangeY * 2 - 1 : 0, doZ ? range : 0);

        ToolCommons.removeBlocksInIteration(player, stack, world, pos, beginDiff, endDiff, canMine);

        if (origLevel == 5) {
            PlayerHelper.grantCriterion((ServerPlayer) player, prefix("challenge/rank_ss_pick"), "code_triggered");
        }
    }
}
