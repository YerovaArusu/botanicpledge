package yerova.botanicpledge.client.render.items;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import vazkii.botania.network.TriConsumer;
import yerova.botanicpledge.common.items.relic.YggdRamus;
import yerova.botanicpledge.setup.BPItems;
import yerova.botanicpledge.setup.BotanicPledge;


public class BotanicPledgeItemProperties {
    public static void init(TriConsumer<ItemLike, ResourceLocation, ClampedItemPropertyFunction> consumer) {

        consumer.accept(BPItems.YGGD_RAMUS.get(), new ResourceLocation(BotanicPledge.MOD_ID, "ranged"),
                (stack, world, entity, seed) -> YggdRamus.isRanged(stack) ? 1 : 0);

        consumer.accept(BPItems.TERRA_SHIELD.get(), new ResourceLocation("blocking"), (stack, world, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

    }

}
