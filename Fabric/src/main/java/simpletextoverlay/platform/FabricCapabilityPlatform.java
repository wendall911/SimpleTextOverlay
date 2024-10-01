package simpletextoverlay.platform;

import java.util.Optional;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import simpletextoverlay.component.SimpleTextOverlayComponents;
import simpletextoverlay.network.SyncDataPacket;
import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.platform.services.ICapabilityPlatform;

public class FabricCapabilityPlatform implements ICapabilityPlatform {

    @Override
    public Optional<? extends DataManager> getDataManagerCapability(Player player) {
        return SimpleTextOverlayComponents.DATA_MANAGER.maybeGet(player);
    }

    @Override
    public void syncData(ServerPlayer sp) {
        Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent(data -> {
            ServerPlayNetworking.send(sp, new SyncDataPacket(data.getSyncData()));
        });
    }

}
