package simpletextoverlay;

import simpletextoverlay.config.OverlayConfig;
import simpletextoverlay.handler.PacketHandler;
import simpletextoverlay.proxy.CommonProxy;
import simpletextoverlay.proxy.ServerProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlay {

    public static final String MODID = "simpletextoverlay";

    public static final Logger logger = LogManager.getFormatterLogger(SimpleTextOverlay.MODID);

    public SimpleTextOverlay() {
        PacketHandler.setup();
		OverlayConfig.setup();

        DistExecutor.runForDist(() -> CommonProxy::new, () -> ServerProxy::new);
    }
    
}

