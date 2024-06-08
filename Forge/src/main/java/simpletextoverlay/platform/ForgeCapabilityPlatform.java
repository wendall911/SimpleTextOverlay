package simpletextoverlay.platform;

import java.util.Optional;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.network.PacketDistributor;

import simpletextoverlay.capability.CapabilityDataManager;
import simpletextoverlay.network.ForgeSyncData;
import simpletextoverlay.network.ForgeNetworkManager;
import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.platform.services.ICapabilityPlatform;

public class ForgeCapabilityPlatform implements ICapabilityPlatform {

    @Override
    public Optional<? extends DataManager> getDataManagerCapability(Player player) {
        return CapabilityDataManager.getCapability(player).resolve();
    }

    @Override
    public void syncData(ServerPlayer sp) {
        ForgeNetworkManager.INSTANCE.send(
            new ForgeSyncData(sp),
            PacketDistributor.PLAYER.with(sp)
        );
    }

}
