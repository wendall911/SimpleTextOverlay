package simpletextoverlay.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import simpletextoverlay.SimpleTextOverlay;

public class FabricSyncData implements CustomPacketPayload {

    private SyncData syncData;
    public static final ResourceLocation ID = new ResourceLocation(SimpleTextOverlay.MODID, "sto_data");
    public static final Type<FabricSyncData> TYPE = new Type<>(ID);

    public FabricSyncData(FriendlyByteBuf buffer) {
        syncData = new SyncData(buffer.readByteArray());
    }

    public FabricSyncData(ServerPlayer player) {
        syncData = new SyncData(player);
    }

    public SyncData getSyncData() {
        return syncData;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }

}
