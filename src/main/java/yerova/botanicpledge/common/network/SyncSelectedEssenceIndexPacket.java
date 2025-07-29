package yerova.botanicpledge.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import yerova.botanicpledge.common.items.relic.NineRealmGlove;

import java.util.function.Supplier;

public class SyncSelectedEssenceIndexPacket {
    private final int index;

    public SyncSelectedEssenceIndexPacket(int index) {
        this.index = index;
    }

    public SyncSelectedEssenceIndexPacket(FriendlyByteBuf buf) {
        this.index = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(index);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack stack = player.getMainHandItem();
                if (!(stack.getItem() instanceof NineRealmGlove)) {
                    stack = player.getOffhandItem();
                }

                if (stack.getItem() instanceof NineRealmGlove) {
                    NineRealmGlove.setSelectedIndex(stack, index);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
