package yerova.botanicpledge.common.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import vazkii.botania.network.BotaniaPacket;
import yerova.botanicpledge.setup.BotanicPledge;

public class Networking {

    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(BotanicPledge.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SyncValues.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncValues::new)
                .encoder(SyncValues::encode)
                .consumerMainThread(SyncValues::handle)
                .add();

        net.messageBuilder(ItemButtonInteractionToServer.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ItemButtonInteractionToServer::new)
                .encoder(ItemButtonInteractionToServer::encode)
                .consumerMainThread(ItemButtonInteractionToServer::handle)
                .add();

        net.messageBuilder(SpawnYggdrasilGuardian.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpawnYggdrasilGuardian::decode)
                .encoder(SpawnYggdrasilGuardian::encode)
                .consumerMainThread(SpawnYggdrasilGuardian.Handler::handle)
                .add();
    }


    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }


    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    @SuppressWarnings("unchecked")
    public static Packet<ClientGamePacketListener> toVanillaClientboundPacket(BotaniaPacket packet) {
        return (Packet<ClientGamePacketListener>) Networking.INSTANCE.toVanillaPacket(packet, NetworkDirection.PLAY_TO_CLIENT);
    }


}
