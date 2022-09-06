package yerova.botanicpledge.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yerova.botanicpledge.client.synched.ClientSyncedDivineCorePlayerStats;

import java.util.function.Supplier;

public class PacketSyncDCPLayerStatsToClient {
    private final int maxShield, shield, maxCharge, charge, armor;
    private final double armorToughness, maxHealth, knockbackResistance, attackDamage;

    public PacketSyncDCPLayerStatsToClient(int maxShield, int shield, int maxCharge, int charge, double armorToughness, int armor, double maxHealth, double knockbackResistance, double attackDamage) {
        this.maxShield = maxShield;
        this.shield = shield;
        this.maxCharge = maxCharge;
        this.charge = charge;

        this.armorToughness = armorToughness;
        this.armor = armor;
        this.maxHealth = maxHealth;
        this.knockbackResistance = knockbackResistance;
        this.attackDamage = attackDamage;
    }

    public PacketSyncDCPLayerStatsToClient(FriendlyByteBuf buffer) {
        this.maxShield = buffer.readInt();
        this.shield = buffer.readInt();
        this.maxCharge = buffer.readInt();
        this.charge = buffer.readInt();

        this.armorToughness = buffer.readDouble();
        this.armor = buffer.readInt();
        this.maxHealth = buffer.readDouble();
        this.knockbackResistance = buffer.readDouble();
        this.attackDamage = buffer.readDouble();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(maxShield);
        buffer.writeInt(shield);
        buffer.writeInt(maxCharge);
        buffer.writeInt(charge);

        buffer.writeDouble(armorToughness);
        buffer.writeInt(armor);
        buffer.writeDouble(maxHealth);
        buffer.writeDouble(knockbackResistance);
        buffer.writeDouble(attackDamage);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();

        ctx.enqueueWork(()-> {
            ClientSyncedDivineCorePlayerStats.set(maxShield, shield, maxCharge, charge, armorToughness, armor, maxHealth, knockbackResistance, attackDamage);
        });
        return true;
    }
}
