package yerova.botanicpledge.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
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

        net.messageBuilder(YggdralScepterLeftClick.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(YggdralScepterLeftClick::new)
                .encoder(YggdralScepterLeftClick::encode)
                .consumer(YggdralScepterLeftClick::handle)
                .add();


        net.messageBuilder(SyncProtector.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SyncProtector::new)
                .encoder(SyncProtector::encode)
                .consumer(SyncProtector::handle)
                .add();

        net.messageBuilder(YggdralScepterSwitchSkills.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(YggdralScepterSwitchSkills::new)
                .encoder(YggdralScepterSwitchSkills::encode)
                .consumer(YggdralScepterSwitchSkills::handle)
                .add();

        net.messageBuilder(LeftClick.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(LeftClick::new)
                .encoder(LeftClick::encode)
                .consumer(LeftClick::handle)
                .add();

    }


    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }


    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
