package simpletextoverlay.platform;

import java.util.Optional;

import io.netty.buffer.Unpooled;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.component.SimpleTextOverlayComponents;
import simpletextoverlay.network.SyncData;
import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.platform.services.ICapabilityPlatform;

public class FabricCapabilityPlatform implements ICapabilityPlatform {

    @Override
    public Optional<? extends DataManager> getDataManagerCapability(Player player) {
        return SimpleTextOverlayComponents.DATA_MANAGER.maybeGet(player);
    }

    @Override
    public void syncData(ServerPlayer sp) {
        SyncData syncData = new SyncData(sp);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(syncData.bytes));

        ServerPlayNetworking.send(sp, new ResourceLocation(SimpleTextOverlay.MODID, DataManager.STO_DATA), buf);
    }

}
