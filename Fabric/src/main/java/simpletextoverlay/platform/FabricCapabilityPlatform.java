package simpletextoverlay.platform;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.platform.services.ICapabilityPlatform;

import java.util.Optional;

public class FabricCapabilityPlatform implements ICapabilityPlatform {

    @Override
    public Optional<? extends DataManager> getDataManagerCapability(Player player) {
        return Optional.empty();
    }

    @Override
    public void syncData(ServerPlayer sp) {

    }

}
