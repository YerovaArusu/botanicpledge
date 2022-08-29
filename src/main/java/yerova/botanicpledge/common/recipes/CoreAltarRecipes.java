package yerova.botanicpledge.common.recipes;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import yerova.botanicpledge.common.blocks.block_entities.CoreAltarBlockEntity;
import yerova.botanicpledge.common.items.ItemInit;

import java.util.UUID;

public class CoreAltarRecipes {
    public static void recipeForMariasCore(CoreAltarBlockEntity entity) {

        if (
                entity.itemHandler.getStackInSlot(0).getItem().equals(ItemInit.YGGSRALIUM_SHARD.get()) &&
                        entity.itemHandler.getStackInSlot(1).getItem().equals(ItemInit.YGGSRALIUM_SHARD.get()) &&
                        entity.itemHandler.getStackInSlot(2).getItem().equals(ItemInit.YGGSRALIUM_SHARD.get()) &&

                        entity.itemHandler.getStackInSlot(3).getItem().equals(ItemInit.YGGSRALIUM_SHARD.get()) &&
                        entity.itemHandler.getStackInSlot(4).getItem().equals(ItemInit.MARIAS_CORE.get()) &&
                        entity.itemHandler.getStackInSlot(5).getItem().equals(ItemInit.YGGSRALIUM_SHARD.get()) &&

                        entity.itemHandler.getStackInSlot(6).getItem().equals(ItemInit.YGGSRALIUM_SHARD.get()) &&
                        entity.itemHandler.getStackInSlot(7).getItem().equals(ItemInit.YGGSRALIUM_SHARD.get()) &&
                        entity.itemHandler.getStackInSlot(8).getItem().equals(ItemInit.YGGSRALIUM_SHARD.get())) {

            ItemStack resultStack = entity.itemHandler.getStackInSlot(4);

            for (int i = 0; i <=9; i++){ entity.itemHandler.extractItem(i, 1,false); }

            entity.itemHandler.insertItem(9, resultStack, false);

        }
    }
}
