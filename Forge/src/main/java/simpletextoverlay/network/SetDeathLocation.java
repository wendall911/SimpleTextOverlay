package simpletextoverlay.network;

import java.util.function.Supplier;

import de.maxhenkel.corpse.corelib.death.Death;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import simpletextoverlay.platform.Services;
import simpletextoverlay.util.PinHelper;

import static simpletextoverlay.events.SimpleTextOverlayEvents.LASTDEATH;

public class SetDeathLocation implements IData {

    private Death death;

    public SetDeathLocation(Death death) {
        this.death = death;
    }

    public SetDeathLocation(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();

        if (tag != null) {
            death = Death.fromNBT(tag);
        }
    }

    public static void init(int idx) {
        NetworkManager.registerMessage(idx, SetDeathLocation.class, SetDeathLocation::new);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(death.toNBT(false));
    }

    @Override
    public void process(Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer sp = ctx.get().getSender();

        if (sp != null) {
            ctx.get().enqueueWork(() -> setDeathLocation(sp, death));
        }
    }

    private void setDeathLocation(ServerPlayer sp, Death death) {
        Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
            ResourceKey<Level> worldKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(death.getDimension()));
            BlockPos deathPos = death.getBlockPos();
            PinHelper.PointPin lastDeath = PinHelper.getPointPin(sp, pinsData, worldKey, deathPos, LASTDEATH);

            PinHelper.setPointPin(sp, pinsData, lastDeath);

            NetworkManager.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> sp),
                new SyncData(sp)
            );
        });
    }

}
