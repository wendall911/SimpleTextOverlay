package simpletextoverlay.platform.services;

import java.util.Optional;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import simpletextoverlay.overlay.compass.DataManager;

public interface ICapabilityPlatform {

    Optional<? extends DataManager> getDataManagerCapability(Player player);

    void syncData(ServerPlayer sp);

}
