package yerova.botanicpledge.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yerova.botanicpledge.client.synched.ClientSyncedProtector;

import java.util.function.Supplier;

public class SyncProtector {

    private final int charge;
    private final int maxCharge;
    private final int defense;
    private final int maxDefense;

    public SyncProtector(int charge, int maxCharge, int defense, int maxDefense) {
        this.charge = charge;
        this.maxCharge = maxCharge;
        this.defense = defense;
        this.maxDefense = maxDefense;
    }

    public SyncProtector(FriendlyByteBuf buffer) {
        this.charge = buffer.readInt();
        this.maxCharge = buffer.readInt();
        this.defense = buffer.readInt();
        this.maxDefense = buffer.readInt();
    }


    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.charge);
        buffer.writeInt(this.maxCharge);
        buffer.writeInt(this.defense);
        buffer.writeInt(this.maxDefense);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();

        ctx.enqueueWork(() -> {
            ClientSyncedProtector.set(charge, maxCharge, defense, maxDefense);
        });
        return true;
    }

}
