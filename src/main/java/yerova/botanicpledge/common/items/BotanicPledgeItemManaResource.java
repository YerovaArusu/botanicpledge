package yerova.botanicpledge.common.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ModItems;
import yerova.botanicpledge.common.entitites.marina_boss.MarinaEntity;

import javax.annotation.Nonnull;

public class BotanicPledgeItemManaResource extends Item {
    public BotanicPledgeItemManaResource(Properties p) {
        super(p);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        ItemStack stack = ctx.getItemInHand();

        if (stack.is(ItemInit.YGGSRALIUM_SHARD.get()) || stack.is(ItemInit.YGGDRALIUM_INGOT.get())) {
            return MarinaEntity.spawn(ctx.getPlayer(), stack, ctx.getLevel(), ctx.getClickedPos(), stack.is(ItemInit.YGGDRALIUM_INGOT.get()))
                    ? InteractionResult.SUCCESS
                    : InteractionResult.FAIL;
        } else if (stack.is(ModItems.livingroot)) {
            return Items.BONE_MEAL.useOn(ctx);
        }

        return super.useOn(ctx);
    }
}
