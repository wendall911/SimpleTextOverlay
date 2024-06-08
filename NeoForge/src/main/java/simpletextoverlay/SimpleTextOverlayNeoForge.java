package simpletextoverlay;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;

import simpletextoverlay.client.ModKeyBindings;
import simpletextoverlay.event.CapabilityEventHandler;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.event.KeyEventHandler;
import simpletextoverlay.event.PlayerEventHandler;
import simpletextoverlay.network.*;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlayNeoForge {

    private static boolean setupDone = false;

    public SimpleTextOverlayNeoForge(IEventBus eventBus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            eventBus.addListener(this::keyBindingSetup);
            eventBus.addListener(this::clientSetup);
            eventBus.addListener(GameOverlayEventHandler.INSTANCE::onRegisterOverlays);
        }
        eventBus.addListener(this::setup);
        eventBus.addListener(this::registerPayloadHandler);
        eventBus.register(CapabilityEventHandler.class);
        SimpleTextOverlay.init();
        SimpleTextOverlay.initConfig();
    }

    private void setup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    private void registerPayloadHandler(final RegisterPayloadHandlerEvent event) {
        event.registrar(SimpleTextOverlay.MODID).play(NeoForgeSyncData.ID, NeoForgeSyncData::new,
            handler -> handler.client(NeoForgeNetworkManager.getInstance()::processSyncData));
        event.registrar(SimpleTextOverlay.MODID).play(OpenHistory.ID, OpenHistory::new,
            handler -> handler.client(NeoForgeNetworkManager.getInstance()::processOpenHistory));
        event.registrar(SimpleTextOverlay.MODID).play(RequestDeathHistory.ID, RequestDeathHistory::new,
            handler -> handler.client(NeoForgeNetworkManager.getInstance()::processRequestDeathHistory));
        event.registrar(SimpleTextOverlay.MODID).play(SetDeathLocation.ID, SetDeathLocation::new,
            handler -> handler.client(NeoForgeNetworkManager.getInstance()::processSetDeathLocation));
    }

    public void clientSetup(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(new KeyEventHandler());
    }

    public void keyBindingSetup(RegisterKeyMappingsEvent event) {
        if (!setupDone && ModList.get().isLoaded("corpse")) {
            ModKeyBindings.init(event);
            setupDone = true;
        }
    }

}
