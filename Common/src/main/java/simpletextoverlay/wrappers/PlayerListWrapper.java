package simpletextoverlay.wrappers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.ReflectionUtil;

public class PlayerListWrapper extends Wrapper<PlayerList> {

    private static Method saveDataMethod;

    static {
        try {
            saveDataMethod = ReflectionUtil.findMethod(PlayerList.class, "save", ServerPlayer.class);
        } catch (NoSuchMethodException e) {
            SimpleTextOverlay.LOGGER.error("Error loading saveDataMethod {}", e);
        }
    }

    public PlayerListWrapper(PlayerList playerList) {
        super(playerList);
    }

    public void savePlayerData(ServerPlayer sp) {
        try {
            saveDataMethod.invoke(get(), sp);
        } catch (InvocationTargetException | IllegalAccessException ignore) {}
    }

}
