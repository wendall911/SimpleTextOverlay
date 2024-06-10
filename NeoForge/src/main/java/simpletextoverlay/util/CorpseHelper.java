package simpletextoverlay.util;

import java.util.List;

import de.maxhenkel.corpse.corelib.death.Death;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.client.gui.SetDeathHistoryScreen;
import simpletextoverlay.network.NeoForgeNetworkManager;
import simpletextoverlay.network.OpenHistory;
import simpletextoverlay.network.RequestDeathHistory;
import simpletextoverlay.network.SetDeathLocation;

public class CorpseHelper {

    public static void registerPayload(RegisterPayloadHandlerEvent event) {
        event.registrar(SimpleTextOverlay.MODID).play(OpenHistory.ID, OpenHistory::new,
            handler -> handler.client(NeoForgeNetworkManager.getInstance()::processOpenHistory));
        event.registrar(SimpleTextOverlay.MODID).play(RequestDeathHistory.ID, RequestDeathHistory::new,
            handler -> handler
                .client(NeoForgeNetworkManager.getInstance()::processRequestDeathHistory)
                .server(NeoForgeNetworkManager.getInstance()::processRequestDeathHistory)
        );
        event.registrar(SimpleTextOverlay.MODID).play(SetDeathLocation.ID, SetDeathLocation::new,
            handler -> handler
                .client(NeoForgeNetworkManager.getInstance()::processSetDeathLocation)
                .server(NeoForgeNetworkManager.getInstance()::processSetDeathLocation)
        );
    }

    public static void openDeathHistoryScreen(List<Death> deaths) {
        SetDeathHistoryScreen.openScreen(deaths);
    }

}
