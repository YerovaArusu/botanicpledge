package yerova.botanicpledge.common.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.item.BotaniaItems;
import yerova.botanicpledge.common.entitites.yggdrasilguardian.YggdrasilGuardian;
import yerova.botanicpledge.setup.BPItems;

public class Yggdrasilsteel extends Item {
    public Yggdrasilsteel(Properties pProperties) {
        super(pProperties);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        ItemStack stack = ctx.getItemInHand();

        if (stack.is(BPItems.YGGDRALIUM_INGOT.get())) {
            return YggdrasilGuardian.spawn(ctx.getPlayer(), stack, ctx.getLevel(), ctx.getClickedPos())
                    ? InteractionResult.sidedSuccess(ctx.getLevel().isClientSide())
                    : InteractionResult.FAIL;
        } else if (stack.is(BotaniaItems.livingroot)) {
            return Items.BONE_MEAL.useOn(ctx);
        }

        return super.useOn(ctx);
    }
}
