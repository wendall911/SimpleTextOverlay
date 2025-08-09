package simpletextoverlay.mixin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import static simpletextoverlay.events.SimpleTextOverlayEvents.PINS_CACHE;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Inject(method = "setRespawnPosition", at = @At("HEAD"))
    private void sto$setRespawnPosition(ResourceKey<Level> worldKey, BlockPos respawnPos, float angle, boolean forced, boolean hasMessage, CallbackInfo ci) {
        if (hasMessage && respawnPos != null) {
            ServerPlayer sp = (ServerPlayer) (Object) this;

            Services.CAPABILITY_PLATFORM.getDataManagerCapability(sp).ifPresent((pinsData) -> {
                PinHelper.PointPin bedPin = PinHelper.getPointPin(sp, pinsData, worldKey, respawnPos, BEDSPAWN);
                UUID uuid = sp.getUUID();
                Map<ResourceKey<Level>, Map<String, PinHelper.PointPin>> playerCache = PINS_CACHE.computeIfAbsent(uuid, k -> new HashMap<>());

                playerCache.computeIfAbsent(worldKey, k -> new HashMap<>()).put(BEDSPAWN, bedPin);
                PinHelper.setPointPin(sp, pinsData, bedPin);

                Services.CAPABILITY_PLATFORM.syncData(sp);
            });
        }
    }

}
