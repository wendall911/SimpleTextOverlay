package simpletextoverlay.platform;

import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import simpletextoverlay.platform.services.IPlatform;

public class ForgePlatform implements IPlatform {

    static HashMap<String, String> srgMapping = new HashMap<>();

    static {
        srgMapping.put("save", "m_6765_");
    }

    @Override
    public boolean isModLoaded(String name) {
        return ModList.get().isLoaded(name);
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLLoader.getDist() == Dist.CLIENT;
    }

    @Override
    public @NotNull Field findField(@NotNull Class<?> clazz, @NotNull String name) throws NoSuchFieldException {
        try {
            final Field field = ObfuscationReflectionHelper.findField(clazz, srgMapping.get(name));
            field.setAccessible(true);
            return field;
        }
        catch (Exception e) {
            throw new NoSuchFieldException(e.toString());
        }
    }

    @Override
    public @NotNull Method findMethod(@NotNull Class<?> clazz, @NotNull String name, Class<?> @NotNull ... parameters) throws NoSuchMethodException {
        try {
            final Method method = ObfuscationReflectionHelper.findMethod(clazz, srgMapping.get(name));
            method.setAccessible(true);
            return method;
        }
        catch (Exception e) {
            throw new NoSuchMethodException(e.toString());
        }
    }

}
