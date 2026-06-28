package simpletextoverlay.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import simpletextoverlay.events.GameOverlayEventHandler;

@Mixin(Hud.class)
public abstract class FabricHudMixin {

    @Shadow
    private int tickCount;

    @Inject(method = "extractPlayerHealth", at = @At("HEAD"))
    private void sto$extractPlayerHealth(GuiGraphicsExtractor guiGraphics, CallbackInfo ci) {
        Player player = this.getCameraPlayer();

        if (player != null) {
            GameOverlayEventHandler.onHudRender(guiGraphics, this.tickCount);
        }

    }

    @Shadow
    private Player getCameraPlayer() {
        throw new IllegalStateException();
    }

}
