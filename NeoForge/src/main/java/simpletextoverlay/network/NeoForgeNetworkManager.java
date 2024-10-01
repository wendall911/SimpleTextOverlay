package simpletextoverlay.network;

import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import simpletextoverlay.platform.Services;
import simpletextoverlay.util.LocalPlayerHelper;

public class NeoForgeNetworkManager {

    private static final NeoForgeNetworkManager INSTANCE = new NeoForgeNetworkManager();

    public static NeoForgeNetworkManager getInstance() {
        return INSTANCE;
    }

    public void processSyncData(SyncDataPacket msgData, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = LocalPlayerHelper.getLocalPlayer();

            Services.CAPABILITY_PLATFORM.getDataManagerCapability(player).ifPresent(data -> {
                data.readSyncData(msgData.data());
            });
        });
    }

}
