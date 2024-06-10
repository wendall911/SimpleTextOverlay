package simpletextoverlay.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.Services;

public class RequestSyncData implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(SimpleTextOverlay.MODID, "request_sync_data");

    public RequestSyncData() {}

    public RequestSyncData(FriendlyByteBuf buf) {}

    @Override
    public void write(FriendlyByteBuf pBuffer) {}

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static void sync(Player sp) {
        Services.CAPABILITY_PLATFORM.syncData((ServerPlayer) sp);
    }

}
