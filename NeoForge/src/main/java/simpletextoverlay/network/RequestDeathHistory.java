package simpletextoverlay.network;

import java.util.List;

import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.corelib.death.DeathManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import simpletextoverlay.SimpleTextOverlay;

public class RequestDeathHistory implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation(SimpleTextOverlay.MODID, "request_death_history");

    public RequestDeathHistory() {}

    public RequestDeathHistory(FriendlyByteBuf buf) {}

    @Override
    public void write(FriendlyByteBuf buffer) {}

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static void sync(Player sp) {
        List<Death> deaths = DeathManager.getDeaths((ServerLevel) sp.level(), sp.getUUID());

        if (deaths != null) {
            PacketDistributor.PLAYER.with((ServerPlayer) sp).send(new OpenHistory(deaths));
        }
    }

}
