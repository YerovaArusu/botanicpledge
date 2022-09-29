package yerova.botanicpledge.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;
import vazkii.botania.common.handler.ModSounds;
import yerova.botanicpledge.common.entitites.projectiles.EntityCorruptMagicMissile;
import yerova.botanicpledge.common.entitites.projectiles.ManaSlashEntity;
import yerova.botanicpledge.common.items.YggdralScepter;

import java.util.Random;
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
