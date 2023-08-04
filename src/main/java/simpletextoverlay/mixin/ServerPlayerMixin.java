package simpletextoverlay.mixin;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import simpletextoverlay.overlay.compass.DataManager;
import simpletextoverlay.util.PinHelper;

import static simpletextoverlay.event.PlayerEventHandler.BEDSPAWN;
import static simpletextoverlay.event.PlayerEventHandler.PINS_CACHE;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "setRespawnPosition", at = @At("HEAD"))
    private void sto$setRespawnPosition(ResourceKey<Level> worldKey, BlockPos respawnPos, float angle, boolean forced, boolean hasMessage, CallbackInfo ci) {
        if (hasMessage && respawnPos != null) {
            ServerPlayer player = (ServerPlayer) (Object) this;

            player.getCapability(DataManager.INSTANCE).ifPresent((pinsData) -> {
                PinHelper.PointPin bedPin = PinHelper.getPointPin(pinsData, worldKey, respawnPos, BEDSPAWN);
                Map<String, PinHelper.PointPin> cachedPins = PINS_CACHE.get(worldKey);

                cachedPins.put(BEDSPAWN, bedPin);
                PinHelper.setPointPin(pinsData, bedPin);
            });
        }
    }

}
