package yerova.botanicpledge.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import yerova.botanicpledge.common.network.Networking;
import yerova.botanicpledge.common.network.PacketSyncDCPLayerStatsToClient;

import javax.annotation.Nonnull;

public class DivineCorePlayerStatsManager extends SavedData {

    private int counter = 0;

    public DivineCorePlayerStatsManager() {
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        return compoundTag;
    }

    public DivineCorePlayerStatsManager(CompoundTag tag) {

        //TODO: DO THAT FUCKING STUFF

    }

    @Nonnull
    public static DivineCorePlayerStatsManager get(Level level) {
        if (level.isClientSide) {
            throw new RuntimeException("Don't access this client-side!");
        }
        DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
        return storage.computeIfAbsent(DivineCorePlayerStatsManager::new, DivineCorePlayerStatsManager::new, "dc_player_stats_manager");
    }

    public void tick(Level level) {
        counter--;
        if (counter <= 0) {
            counter = 10;
            // Synchronize the mana to the players in this world
            // todo expansion: keep the previous data that was sent to the player and only send if changed
            level.players().forEach(player -> {
                if (player instanceof ServerPlayer serverPlayer) {
                    int maxShield = serverPlayer.getCapability(DivineCorePlayerStatsProvider.DC_PLAYER_STATS).map(DivineCorePlayerStats::getMaxShield).orElse(0);
                    int shield = serverPlayer.getCapability(DivineCorePlayerStatsProvider.DC_PLAYER_STATS).map(DivineCorePlayerStats::getShield).orElse(0);
                    int maxCharge = serverPlayer.getCapability(DivineCorePlayerStatsProvider.DC_PLAYER_STATS).map(DivineCorePlayerStats::getMaxCharge).orElse(0);
                    int charge = serverPlayer.getCapability(DivineCorePlayerStatsProvider.DC_PLAYER_STATS).map(DivineCorePlayerStats::getCharge).orElse(0);

                    double armorToughness = serverPlayer.getCapability(DivineCorePlayerStatsProvider.DC_PLAYER_STATS).map(DivineCorePlayerStats::getArmorToughness).orElse(0.0D);
                    int armor = serverPlayer.getCapability(DivineCorePlayerStatsProvider.DC_PLAYER_STATS).map(DivineCorePlayerStats::getArmor).orElse(0);
                    double maxHealth = serverPlayer.getCapability(DivineCorePlayerStatsProvider.DC_PLAYER_STATS).map(DivineCorePlayerStats::getMaxHealth).orElse(0.0D);
                    double knockbackResistance = serverPlayer.getCapability(DivineCorePlayerStatsProvider.DC_PLAYER_STATS).map(DivineCorePlayerStats::getKnockbackResistance).orElse(0.0D);
                    double attackDamage = serverPlayer.getCapability(DivineCorePlayerStatsProvider.DC_PLAYER_STATS).map(DivineCorePlayerStats::getAttackDamage).orElse(0.0D);

                    Networking.sendToPlayer(new PacketSyncDCPLayerStatsToClient(maxShield, shield, maxCharge, charge,
                            armorToughness, armor, maxHealth, knockbackResistance, attackDamage), serverPlayer);
                }
            });

            // todo expansion: here it would be possible to slowly regenerate mana in chunks
        }
    }
}
