package simpletextoverlay.platform;

import java.util.Optional;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.network.PacketDistributor;

import simpletextoverlay.attachments.CompassDataProvider;
import simpletextoverlay.network.SyncDataPacket;
import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.platform.services.ICapabilityPlatform;

public class NeoForgeCapabilityPlatform implements ICapabilityPlatform {

    @Override
    public Optional<? extends DataManager> getDataManagerCapability(Player player) {
        return CompassDataProvider.getData(player);
    }

    @Override
    public void syncData(ServerPlayer sp) {
        Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent(data -> {
            PacketDistributor.sendToPlayer(sp, new SyncDataPacket(data.getSyncData()));
        });
    }

}
