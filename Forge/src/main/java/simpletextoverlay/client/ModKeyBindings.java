package simpletextoverlay.client;

import net.minecraft.client.KeyMapping;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {

    public static KeyMapping KEY_DEATH_HISTORY;

    public static void init(RegisterKeyMappingsEvent event) {
        KEY_DEATH_HISTORY = new KeyMapping("key.simpletextoverlay.death_history", GLFW.GLFW_KEY_U, "key.categories.misc");
        event.register(KEY_DEATH_HISTORY);
    }

}