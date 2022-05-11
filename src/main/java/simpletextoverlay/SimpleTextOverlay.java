package simpletextoverlay;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import simpletextoverlay.proxy.ClientProxy;
import simpletextoverlay.proxy.CommonProxy;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlay {

    public static final String MODID = "simpletextoverlay";

    public static final Logger LOGGER = LogManager.getFormatterLogger(MODID);
    public static CommonProxy PROXY;

    public SimpleTextOverlay() {
        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        PROXY.start();
    }

}
