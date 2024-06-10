package simpletextoverlay.platform.services;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface IPlatform {

    boolean isModLoaded(String name);

    boolean isPhysicalClient();

    Field findField(Class<?> clazz, String name) throws NoSuchFieldException;

    Method findMethod(Class<?> clazz, String name, Class<?>[] parameters) throws NoSuchMethodException;

}
