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
public abstract class NeoForgeGuiMixin {

    @Shadow
    private int tickCount;

    /*
     * The NeoForge layering system makes absolutely zero sense given other mods can cancel events for rendering.
     * I suppose I could just render a layer above everything, but then we are just doing what this mixin does with
     * less headache and more compat with future versions? At minimum, it needs more documentation and explanation on
     * exactly what they were trying to achieve by adding the ability for other mods to disable UI elements from
     * this mod. Makes no sense. This ensures they can't bust our mod with canceling events.
     */
    @Inject(method = "extractAirLevel", at = @At(value = "HEAD"))
    private void sto$extractAirLevel(GuiGraphicsExtractor guiGraphics, CallbackInfo ci) {
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
