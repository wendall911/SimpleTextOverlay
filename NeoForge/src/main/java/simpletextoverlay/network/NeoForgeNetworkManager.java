package simpletextoverlay.network;

import java.util.Optional;

import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import simpletextoverlay.client.gui.SetDeathHistoryScreen;
import simpletextoverlay.platform.Services;

public class NeoForgeNetworkManager {

    private static final NeoForgeNetworkManager INSTANCE = new NeoForgeNetworkManager();

    public static NeoForgeNetworkManager getInstance() {
        return INSTANCE;
    }

    public void processSyncData(NeoForgeSyncData msgData, IPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> Services.CAPABILITY_PLATFORM.getDataManagerCapability(Minecraft.getInstance().player).ifPresent(data -> {
            data.read(Minecraft.getInstance().player, new FriendlyByteBuf(Unpooled.wrappedBuffer(msgData.getSyncData().bytes)));
        }));
    }

    public void processOpenHistory(OpenHistory msgData, IPayloadContext ctx) {
        openScreen(msgData, ctx);
    }

    public void openScreen(OpenHistory msgData, IPayloadContext ctx) {
        if (!msgData.getDeaths().isEmpty()) {
            ctx.workHandler().submitAsync(() -> Minecraft.getInstance().setScreen(new SetDeathHistoryScreen(msgData.getDeaths())));
        } else if (Minecraft.getInstance().player != null) {
            ctx.workHandler().submitAsync(() -> Minecraft.getInstance().player.displayClientMessage(Component.translatable("message.corpse.no_death_history"), true));
        }
    }

    public void processRequestDeathHistory(RequestDeathHistory msgData, IPayloadContext ctx) {
        Optional<Player> player = ctx.player();

        player.ifPresent(value -> ctx.workHandler().submitAsync(() -> RequestDeathHistory.sync((ServerPlayer) value)));
    }

    public void processSetDeathLocation(SetDeathLocation msgData, IPayloadContext ctx) {
        Optional<Player> player = ctx.player();

        player.ifPresent(value -> ctx.workHandler().submitAsync(() -> SetDeathLocation.sync((ServerPlayer) value, msgData.getDeath())));
    }

}
