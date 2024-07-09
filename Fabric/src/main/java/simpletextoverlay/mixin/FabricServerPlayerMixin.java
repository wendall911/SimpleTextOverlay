package simpletextoverlay.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import simpletextoverlay.events.SimpleTextOverlayEvents;

@Mixin(ServerPlayer.class)
public class FabricServerPlayerMixin {

    @Inject(method = "triggerDimensionChangeTriggers", at = @At("HEAD"))
    private void sto$triggerDimensionChangeTriggers(ServerLevel level, CallbackInfo ci) {
        ServerPlayer sp = (ServerPlayer) (Object) this;

        SimpleTextOverlayEvents.onPlayerChangeDimension(sp, sp.level().dimension());
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void sto$die(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayer sp = (ServerPlayer) (Object) this;

        SimpleTextOverlayEvents.onPlayerDeath(sp);
    }

    // For refmap ... need to figure out how to make the mapping file added
    @Inject(method = "setRespawnPosition", at = @At("HEAD"))
    private void stofabric$setRespawnPosition(ResourceKey<Level> worldKey, BlockPos respawnPos, float angle, boolean forced, boolean hasMessage, CallbackInfo ci) {}

}
