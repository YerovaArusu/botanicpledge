package yerova.botanicpledge.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import vazkii.botania.network.BotaniaPacket;
import yerova.botanicpledge.client.synched.ClientSyncedProtector;
import yerova.botanicpledge.common.entitites.yggdrasilguardian.YggdrasilGuardian;
import yerova.botanicpledge.setup.BotanicPledge;

import java.util.UUID;
import java.util.function.Supplier;

public record SpawnYggdrasilGuardian (ClientboundAddEntityPacket inner, int playerCount, BlockPos source, UUID bossInfoId) implements BotaniaPacket {
    public static final ResourceLocation ID = new ResourceLocation(BotanicPledge.MOD_ID,"spg");

    @Override
    public void encode(FriendlyByteBuf buf) {
        inner().write(buf);
        buf.writeVarInt(playerCount());
        buf.writeBlockPos(source());
        buf.writeUUID(bossInfoId());
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }

    public static SpawnYggdrasilGuardian decode(FriendlyByteBuf buf) {
        return new SpawnYggdrasilGuardian(
                new ClientboundAddEntityPacket(buf),
                buf.readVarInt(),
                buf.readBlockPos(),
                buf.readUUID()
        );
    }

    public static class Handler {
        public static void handle(SpawnYggdrasilGuardian packet, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context ctx = contextSupplier.get();

            ctx.enqueueWork(() -> {
                var inner = packet.inner();
                int playerCount = packet.playerCount();
                BlockPos source = packet.source();
                UUID bossInfoUuid = packet.bossInfoId();

                Minecraft.getInstance().execute(() -> {
                    var player = Minecraft.getInstance().player;
                    if (player != null) {
                        player.connection.handleAddEntity(inner);
                        Entity e = player.level().getEntity(inner.getId());
                        if (e instanceof YggdrasilGuardian dopple) {
                            dopple.readSpawnData(playerCount, source, bossInfoUuid);
                        }
                    }
                });
            });



        }
    }
}
