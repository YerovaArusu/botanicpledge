package yerova.botanicpledge.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import yerova.botanicpledge.common.utils.LeftClickable;

import java.util.function.Supplier;

public class LeftClick {

    private ItemStack leftClickedItem = ItemStack.EMPTY;

    public LeftClick(ItemStack item) {
        this.leftClickedItem = item;
    }

    public LeftClick(FriendlyByteBuf buffer) {
        this.leftClickedItem = buffer.readItem();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(this.leftClickedItem);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {

        NetworkEvent.Context ctx = contextSupplier.get();
        ServerPlayer player = ctx.getSender();
        ctx.enqueueWork(() -> {

            if (leftClickedItem.getItem() instanceof LeftClickable) {
                ((LeftClickable) leftClickedItem.getItem()).LeftClick(player.level, player, leftClickedItem);
            }
        });
        return ctx.getPacketHandled();
    }
}
