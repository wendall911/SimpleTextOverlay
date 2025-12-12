package simpletextoverlay.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class LocalPlayerHelper {

    public static Player getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

    public static boolean shouldLoad() {
        Player player = Minecraft.getInstance().player;

        return player != null;
    }

}
