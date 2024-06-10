package simpletextoverlay.network;

import de.maxhenkel.corpse.corelib.death.Death;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.network.PacketDistributor;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.platform.Services;
import simpletextoverlay.util.PinHelper;

import static simpletextoverlay.events.SimpleTextOverlayEvents.LASTDEATH;

public class SetDeathLocation implements CustomPacketPayload {

    private Death death;

    public static final ResourceLocation ID = new ResourceLocation(SimpleTextOverlay.MODID, "set_death_location");

    public SetDeathLocation(Death death) {
        this.death = death;
    }

    public SetDeathLocation(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();

        if (tag != null) {
            death = Death.fromNBT(tag);
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(death.toNBT(false));
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public Death getDeath() {
        return death;
    }

    public static void sync(Player player, Death death) {
        ServerPlayer sp = (ServerPlayer) player;

        Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
            ResourceKey<Level> worldKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(death.getDimension()));
            BlockPos deathPos = death.getBlockPos();
            PinHelper.PointPin lastDeath = PinHelper.getPointPin(sp, pinsData, worldKey, deathPos, LASTDEATH);

            PinHelper.setPointPin(sp, pinsData, lastDeath);

            PacketDistributor.PLAYER.with(sp).send(new NeoForgeSyncData(sp));
        });
    }

}
