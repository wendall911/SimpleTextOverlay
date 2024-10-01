package simpletextoverlay;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import simpletextoverlay.attachments.AttachmentDataManager;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.event.PlayerEventHandler;
import simpletextoverlay.network.NeoForgeNetworkManager;
import simpletextoverlay.network.SyncDataPacket;

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

    private void registerPayloadHandler(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(SimpleTextOverlay.MODID).versioned("1.0");

        registrar.playToClient(SyncDataPacket.TYPE, SyncDataPacket.STREAM_CODEC, NeoForgeNetworkManager.getInstance()::processSyncData);
    }

}
