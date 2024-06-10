package simpletextoverlay.platform;

import java.util.Optional;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import net.neoforged.neoforge.network.PacketDistributor;

import simpletextoverlay.attachments.AttachmentDataManager;
import simpletextoverlay.network.NeoForgeSyncData;
import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.platform.services.ICapabilityPlatform;

public class NeoForgeCapabilityPlatform implements ICapabilityPlatform {

    @Override
    public Optional<? extends DataManager> getDataManagerCapability(Player player) {
        return AttachmentDataManager.getData(player);
    }

    @Override
    public void syncData(ServerPlayer sp) {
        PacketDistributor.PLAYER.with(sp).send(new NeoForgeSyncData(sp));
    }

}
