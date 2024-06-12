package simpletextoverlay;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;

import simpletextoverlay.attachments.AttachmentDataManager;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.event.PlayerEventHandler;
import simpletextoverlay.network.NeoForgeNetworkManager;
import simpletextoverlay.network.NeoForgeSyncData;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlayNeoForge {

    private static boolean setupDone = false;

    public SimpleTextOverlayNeoForge(IEventBus eventBus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            eventBus.addListener(GameOverlayEventHandler.INSTANCE::onRegisterOverlays);
        }
        AttachmentDataManager.init(eventBus);
        eventBus.addListener(this::setup);
        eventBus.addListener(this::registerPayloadHandler);
        SimpleTextOverlay.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    private void registerPayloadHandler(final RegisterPayloadHandlerEvent event) {
        event.registrar(SimpleTextOverlay.MODID).play(NeoForgeSyncData.ID, NeoForgeSyncData::new,
            handler -> handler.client(NeoForgeNetworkManager.getInstance()::processSyncData));
    }

}
