package yerova.botanicpledge.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yerova.botanicpledge.client.synched.ClientSyncedValues;
import yerova.botanicpledge.common.capabilities.YggdrasilAura;

import java.util.function.Supplier;

public class SyncValues {


    private final int defense;
    private final int maxDefense;
    private final int yggdrasilPower;

    public SyncValues(int defense, int maxDefense, int yggdrasilPower) {
        this.defense = defense;
        this.maxDefense = maxDefense;
        this.yggdrasilPower = yggdrasilPower;
    }

    public SyncValues(FriendlyByteBuf buffer) {
        this.defense = buffer.readInt();
        this.maxDefense = buffer.readInt();
        this.yggdrasilPower = buffer.readInt();
    }


    public void encode(FriendlyByteBuf buffer) {

        buffer.writeInt(this.defense);
        buffer.writeInt(this.maxDefense);
        buffer.writeInt(this.yggdrasilPower);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();

        ctx.enqueueWork(() -> {
            ClientSyncedValues.set(defense, maxDefense,yggdrasilPower);
        });
        return true;
    }

}
