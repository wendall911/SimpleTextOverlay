package simpletextoverlay.value.registry;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.value.Value;
import simpletextoverlay.value.ValueComplex;
import simpletextoverlay.value.ValueLogic;
import simpletextoverlay.value.ValueMath;
import simpletextoverlay.value.ValueSimple;

public class ValueRegistry {

    public static final ValueRegistry INSTANCE = new ValueRegistry();

    private Map<String, Value> stringValueMap = new HashMap<>();
    private Map<String, Class<? extends Value>> stringClassMap = new HashMap<>();
    private Map<Class<? extends Value>, String> classStringMap = new HashMap<>();

    private void register(final String name, final Value value, final boolean isAlias) {
        if (this.stringValueMap.containsKey(name)) {
            SimpleTextOverlay.logger.error("Duplicate value key '" + name + "'!");
            return;
        }

        if (name == null) {
            SimpleTextOverlay.logger.error("Value name cannot be null!");
            return;
        }

        final String nameLowerCase = name.toLowerCase(Locale.ENGLISH);
        this.stringValueMap.put(nameLowerCase, value);
        this.stringClassMap.put(nameLowerCase, value.getClass());
        if (!isAlias) {
            this.classStringMap.put(value.getClass(), nameLowerCase);
        }
    }

    public void register(final Value value) {
        register(value.getName(), value, false);

        for (final String name : value.getAliases()) {
            register(name, value, true);
        }
    }

    public Value forName(String name) {
        name = name.toLowerCase(Locale.ENGLISH);
        try {
            final Class<? extends Value> clazz = this.stringClassMap.get(name);
            if (clazz != null) {
                final Value value = clazz.newInstance();
                if (value != null) {
                    return value;
                }
            }
        } catch (final Exception e) {
            SimpleTextOverlay.logger.error(String.format("Failed to create an instance for %s!", name), e);
            return new ValueSimple.ValueInvalid();
        }

        SimpleTextOverlay.logger.error(String.format("Failed to create an instance for %s!", name));
        return new ValueSimple.ValueInvalid();
    }

    public String forClass(final Class<? extends Value> clazz) {
        final String str = this.classStringMap.get(clazz);
        return str != null ? str : "invalid";
    }

    public void init() {
        ValueComplex.register();
        ValueLogic.register();
        ValueMath.register();
        ValueSimple.register();
    }

}
