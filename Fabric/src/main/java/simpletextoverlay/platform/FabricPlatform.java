package simpletextoverlay.platform;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import simpletextoverlay.platform.services.IPlatform;

public class FabricPlatform implements IPlatform {

    static HashMap<String, String> loomMapping = new HashMap<>();

    static {
        loomMapping.put("save", "???");
    }

    @Override
    public boolean isModLoaded(String name) {
        return FabricLoader.getInstance().isModLoaded(name);
    }

    @Override
    public boolean isPhysicalClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public Field findField(Class<?> clazz, String name) throws NoSuchFieldException {
        String mappedName = FabricLoader.getInstance().isDevelopmentEnvironment() ? name : loomMapping.get(name);
        final Field field = clazz.getDeclaredField(mappedName);

        field.setAccessible(true);
        return field;
    }

    @Override
    public Method findMethod(Class<?> clazz, String name, Class<?>[] parameters) throws NoSuchMethodException {
        String mappedName = FabricLoader.getInstance().isDevelopmentEnvironment() ? name : loomMapping.get(name);
        final Method method = clazz.getDeclaredMethod(mappedName, parameters);

        method.setAccessible(true);
        return method;
    }

}
