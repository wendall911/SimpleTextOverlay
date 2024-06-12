package simpletextoverlay.network;

import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import simpletextoverlay.platform.Services;

public class NeoForgeNetworkManager {

    private static final NeoForgeNetworkManager INSTANCE = new NeoForgeNetworkManager();

    public static NeoForgeNetworkManager getInstance() {
        return INSTANCE;
    }

    public static void processSyncData(SyncDataPacket msgData, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();

            Services.CAPABILITY_PLATFORM.getDataManagerCapability(player).ifPresent(data -> {
                data.readSyncData(player, msgData.data());
            });
        });
    }

}
