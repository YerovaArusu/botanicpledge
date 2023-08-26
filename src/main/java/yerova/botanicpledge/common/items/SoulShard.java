package yerova.botanicpledge.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import yerova.botanicpledge.common.utils.BPConstants;
import yerova.botanicpledge.common.utils.PlayerUtils;
import yerova.botanicpledge.setup.BPItems;

import java.util.List;
import java.util.UUID;

public class SoulShard extends Item {
    public SoulShard(Properties pProperties) {
        super(pProperties);
    }

    public static ItemStack createSoulShard(Player player){
        ItemStack stack = new ItemStack(BPItems.SOUL_SHARD.get());
        CompoundTag tag = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
        tag.putUUID(BPConstants.SOUL_SHARD_UUID_TAG_NAME, player.getUUID());
        tag.putString(BPConstants.SOUL_SHARD_NAME_TAG_NAME, player.getScoreboardName());

        return stack;
    }

    public static ItemStack createSoulShard(UUID playerUUID, String name){
        ItemStack stack = new ItemStack(BPItems.SOUL_SHARD.get());
        CompoundTag tag = stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME);
        tag.putUUID(BPConstants.SOUL_SHARD_UUID_TAG_NAME, playerUUID);
        tag.putString(BPConstants.SOUL_SHARD_NAME_TAG_NAME, name);

        return stack;
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(pStack.getTag() == null) return;

        String name = SoulShard.getSoulName(pStack);
        UUID uuid  = SoulShard.getSoulUUID(pStack);

        if (name != null && uuid != null) {
            TextComponent t = new TextComponent("Contains the soul of ");
            t.append(new TextComponent(name).withStyle(ChatFormatting.AQUA));

            pTooltipComponents.add(t);
        }


        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pPlayer.getMainHandItem().getItem() instanceof SoulShard) {
            for (SlotResult result: CuriosApi.getCuriosHelper().findCurios(pPlayer, "necklace").stream().toList()) {
                ItemStack stack = result.stack();

                UUID uuid = getSoulUUID(pPlayer.getMainHandItem());
                String name = getSoulName(pPlayer.getMainHandItem());

                if (stack.getItem() instanceof SoulAmulet && !SoulAmulet.amuletContainsSoul(stack, uuid)){
                    SoulAmulet.applySoul(stack,uuid, name);

                    PlayerUtils.removeItemFromInventory(pPlayer, pPlayer.getMainHandItem(), 1);
                }
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static UUID getSoulUUID(ItemStack stack) {
        return stack.getItem() instanceof SoulShard ? stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getUUID(BPConstants.SOUL_SHARD_UUID_TAG_NAME) : null;
    }

    public static String getSoulName(ItemStack stack) {
        return stack.getItem() instanceof SoulShard ? stack.getOrCreateTagElement(BPConstants.STATS_TAG_NAME).getString(BPConstants.SOUL_SHARD_NAME_TAG_NAME) : null;
    }


}
