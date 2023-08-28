package simpletextoverlay.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

import net.minecraft.resources.ResourceLocation;

import simpletextoverlay.SimpleTextOverlay;

public class SimpleTextOverlayComponents implements EntityComponentInitializer {

    public static ResourceLocation STO_DATA = new ResourceLocation(SimpleTextOverlay.MODID, "sto_data");

    public static final ComponentKey<ComponentDataManager> DATA_MANAGER = ComponentRegistry.getOrCreate(
        new ResourceLocation(SimpleTextOverlay.MODID, "sto_provider"),
        ComponentDataManager.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(DATA_MANAGER, ComponentDataManager::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

}
