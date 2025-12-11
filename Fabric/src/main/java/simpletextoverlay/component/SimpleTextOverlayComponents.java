package simpletextoverlay.component;

import net.minecraft.resources.Identifier;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import simpletextoverlay.SimpleTextOverlay;

public class SimpleTextOverlayComponents implements EntityComponentInitializer {

    public static final ComponentKey<ComponentDataManager> DATA_MANAGER = ComponentRegistry.getOrCreate(
        Identifier.fromNamespaceAndPath(SimpleTextOverlay.MODID, "sto_provider"),
        ComponentDataManager.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(DATA_MANAGER, ComponentDataManager::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

}
