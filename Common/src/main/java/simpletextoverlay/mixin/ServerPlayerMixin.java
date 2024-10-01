package simpletextoverlay.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import simpletextoverlay.platform.Services;
import simpletextoverlay.util.PinHelper;

import static simpletextoverlay.events.SimpleTextOverlayEvents.BEDSPAWN;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "setRespawnPosition", at = @At("HEAD"))
    private void sto$setRespawnPosition(ResourceKey<Level> worldKey, BlockPos respawnPos, float angle, boolean forced, boolean hasMessage, CallbackInfo ci) {
        if (hasMessage && respawnPos != null) {
            ServerPlayer sp = (ServerPlayer) (Object) this;

            Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
                PinHelper.setPointPin(sp, pinsData, worldKey, respawnPos, BEDSPAWN);

                Services.CAPABILITY_PLATFORM.syncData(sp);
            });
        }
    }

}
