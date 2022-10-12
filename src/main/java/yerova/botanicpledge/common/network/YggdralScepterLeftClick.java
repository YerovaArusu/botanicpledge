package yerova.botanicpledge.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yerova.botanicpledge.common.items.YggdralScepter;

import java.util.function.Supplier;

public class YggdralScepterLeftClick {


    public YggdralScepterLeftClick() {

    }

    public YggdralScepterLeftClick(FriendlyByteBuf buffer) {

    }

    public void encode(FriendlyByteBuf buffer) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {

        NetworkEvent.Context ctx =contextSupplier.get();
        ServerPlayer player = ctx.getSender();
        ctx.enqueueWork(() -> {
            YggdralScepter.summonCorruptMissile(player);
            YggdralScepter.summonCorruptMissile(player);
            YggdralScepter.summonCorruptMissile(player);


        });
        return ctx.getPacketHandled();
    }


}
