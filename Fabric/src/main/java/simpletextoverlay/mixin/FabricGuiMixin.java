package simpletextoverlay.mixin;

import net.minecraft.client.gui.Gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import simpletextoverlay.events.GameOverlayEventHandler;

@Mixin(Gui.class)
public abstract class FabricGuiMixin {

    @Shadow
    private int tickCount;

    @Inject(method = "extractPlayerHealth", at = @At("HEAD"))
    private void sto$renderPlayerHealth(GuiGraphicsExtractor guiGraphics, CallbackInfo ci) {
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
