package yerova.botanicpledge.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yerova.botanicpledge.client.synched.ClientSyncedProtector;

import java.util.function.Supplier;

public class SyncProtector {


    private final int defense;
    private final int maxDefense;

    public SyncProtector(int defense, int maxDefense) {
        this.defense = defense;
        this.maxDefense = maxDefense;
    }

    public SyncProtector(FriendlyByteBuf buffer) {
        this.defense = buffer.readInt();
        this.maxDefense = buffer.readInt();
    }


    public void encode(FriendlyByteBuf buffer) {

        buffer.writeInt(this.defense);
        buffer.writeInt(this.maxDefense);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();

        ctx.enqueueWork(() -> {
            ClientSyncedProtector.set(defense, maxDefense);
        });
        return true;
    }

}
