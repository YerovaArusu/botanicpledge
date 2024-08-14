package yerova.botanicpledge.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import vazkii.botania.common.handler.EquipmentHandler;
import yerova.botanicpledge.common.items.relic.FirstRelic;
import yerova.botanicpledge.common.items.relic.RingOfAesir;
import yerova.botanicpledge.common.items.relic.YggdRamus;
import yerova.botanicpledge.setup.BPItems;

import java.util.function.Supplier;

public class ItemButtonInteractionToServer {

    private boolean isCTRLDown;
    public ItemButtonInteractionToServer(boolean ctrlDown) {
        isCTRLDown = ctrlDown;
    }

    public ItemButtonInteractionToServer(FriendlyByteBuf buffer) {
        isCTRLDown = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(isCTRLDown);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ServerPlayer player = ctx.getSender();
        ctx.enqueueWork(() -> {


            if (player != null) {

                if (isCTRLDown) {
                    ItemStack stack = EquipmentHandler.findOrEmpty(BPItems.AESIR_RING.get(),player);
                    RingOfAesir.changeLokiState(player,stack);
                } else {
                    if (player.getMainHandItem().getItem() instanceof YggdRamus) {
                        YggdRamus.setRanged(player.getMainHandItem(), !YggdRamus.isRanged(player.getMainHandItem()));
                    }

                    if (player.isShiftKeyDown()) {
                        FirstRelic.switchRelic(player, player.level(), player.getMainHandItem());
                    }
                }
            }
        });
        return ctx.getPacketHandled();
    }
}
