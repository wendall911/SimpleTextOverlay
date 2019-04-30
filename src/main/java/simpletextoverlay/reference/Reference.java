package simpletextoverlay.reference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reference {
    public static final String MODID = "@MODID@";
    public static final String NAME = "@MOD_NAME@";
    public static final String VERSION = "@MOD_VERSION@";
    public static final String FORGE = "@FORGE_VERSION@";
    public static final String FINGERPRINT = "@FINGERPRINT@";
    public static final String PROXY_SERVER = "simpletextoverlay.proxy.ServerProxy";
    public static final String PROXY_CLIENT = "simpletextoverlay.proxy.ClientProxy";
    public static final String GUI_FACTORY = "simpletextoverlay.client.gui.config.GuiFactory";

    public static Logger logger = LogManager.getLogger(Reference.MODID);
}
