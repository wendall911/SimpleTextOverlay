package simpletextoverlay.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayer.RespawnConfig;
import net.minecraft.world.level.storage.LevelData.RespawnData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import simpletextoverlay.overlay.compass.Pin.PinType;
import simpletextoverlay.platform.Services;
import simpletextoverlay.util.PinHelper;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "setRespawnPosition", at = @At("HEAD"))
    private void sto$setRespawnPosition(RespawnConfig respawnConfig, boolean displayInChat, CallbackInfo ci) {
        // TODO: Verify that I want to only set if it is displayed in chat
        // Also death note
        if (displayInChat) {
            ServerPlayer sp = (ServerPlayer) (Object) this;
            RespawnData respawnData = respawnConfig.respawnData();

            Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
                PinHelper.setPointPin(sp, pinsData, respawnData.dimension(), respawnData.pos(), PinType.BEDSPAWN);

                Services.CAPABILITY_PLATFORM.syncData(sp);
            });
        }
    }

}
