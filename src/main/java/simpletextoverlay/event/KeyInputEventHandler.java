package simpletextoverlay.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.lwjgl.input.Keyboard;

import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.reference.Names;

public class KeyInputEventHandler {
    public static final KeyInputEventHandler INSTANCE = new KeyInputEventHandler();

    private static final KeyBinding KEY_BINDING_TOGGLE = new KeyBinding(Names.Keys.TOGGLE, Keyboard.KEY_NONE, Names.Keys.CATEGORY);

    public static final KeyBinding[] KEY_BINDINGS = new KeyBinding[] {
            KEY_BINDING_TOGGLE
    };

    private final Minecraft minecraft = Minecraft.getMinecraft();

    private KeyInputEventHandler() {}

    @SubscribeEvent
    public void onKeyInput(final InputEvent event) {
        if (this.minecraft.currentScreen == null) {
            if (KEY_BINDING_TOGGLE.isPressed()) {
                GameOverlayEventHandler.INSTANCE.enabled = !GameOverlayEventHandler.INSTANCE.enabled;
            }
        }
    }
}
