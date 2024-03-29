package yerova.botanicpledge.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import vazkii.botania.common.item.BotaniaItems;
import yerova.botanicpledge.common.items.relic.AsgardFractal;
import yerova.botanicpledge.common.items.relic.FirstRelic;
import yerova.botanicpledge.common.items.relic.YggdRamus;
import yerova.botanicpledge.setup.BotanicPledge;

import java.awt.event.KeyEvent;
import java.util.function.Supplier;

public class ItemButtonInteractionToServer {


    public ItemButtonInteractionToServer() {
    }

    public ItemButtonInteractionToServer(FriendlyByteBuf buffer) {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        ServerPlayer player = ctx.getSender();
        ctx.enqueueWork(() -> {


            if (player != null) {

                if (player.getMainHandItem().getItem() instanceof YggdRamus) {
                    YggdRamus.setRanged(player.getMainHandItem(), !YggdRamus.isRanged(player.getMainHandItem()));
                }

                if (player.getMainHandItem().getItem() instanceof AsgardFractal) {
                    AsgardFractal.switchSkill(player, player.getMainHandItem());
                }
                if (player.isShiftKeyDown()) {
                    FirstRelic.switchRelic(player, player.level(), player.getMainHandItem());
                }

            }
        });
        return ctx.getPacketHandled();
    }
}
