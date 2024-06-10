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

import simpletextoverlay.attachments.AttachmentDataManager;
import simpletextoverlay.client.ModKeyBindings;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.event.KeyEventHandler;
import simpletextoverlay.event.LocalPlayerEventHandler;
import simpletextoverlay.event.PlayerEventHandler;
import simpletextoverlay.network.NeoForgeNetworkManager;
import simpletextoverlay.network.NeoForgeSyncData;
import simpletextoverlay.network.RequestSyncData;
import simpletextoverlay.util.CorpseHelper;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlayNeoForge {

    private static boolean setupDone = false;

    public SimpleTextOverlayNeoForge(IEventBus eventBus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            eventBus.addListener(this::keyBindingSetup);
            eventBus.addListener(this::clientSetup);
            eventBus.addListener(GameOverlayEventHandler.INSTANCE::onRegisterOverlays);
        }
        AttachmentDataManager.init(eventBus);
        eventBus.addListener(this::setup);
        eventBus.addListener(this::registerPayloadHandler);
        SimpleTextOverlay.init();
        SimpleTextOverlay.initConfig();
    }

    private void setup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new PlayerEventHandler());

        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.register(new LocalPlayerEventHandler());
        }
    }

    private void registerPayloadHandler(final RegisterPayloadHandlerEvent event) {
        event.registrar(SimpleTextOverlay.MODID).play(NeoForgeSyncData.ID, NeoForgeSyncData::new,
            handler -> handler.client(NeoForgeNetworkManager.getInstance()::processSyncData));
        event.registrar(SimpleTextOverlay.MODID).play(RequestSyncData.ID, RequestSyncData::new,
            handler -> handler.server(NeoForgeNetworkManager.getInstance()::processRequestSyncData));

        if (ModList.get().isLoaded("corpse")) {
            CorpseHelper.registerPayload(event);
        }
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
