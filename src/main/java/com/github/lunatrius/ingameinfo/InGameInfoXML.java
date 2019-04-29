package com.github.lunatrius.ingameinfo;

import com.github.lunatrius.ingameinfo.proxy.CommonProxy;
import com.github.lunatrius.ingameinfo.reference.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod(modid = Reference.MODID,
     certificateFingerprint = Reference.FINGERPRINT,
     name = Reference.NAME,
     version = Reference.VERSION,
     guiFactory = Reference.GUI_FACTORY)

public class InGameInfoXML {
    @Instance(Reference.MODID)
    public static InGameInfoXML instance;

    @SidedProxy(serverSide = Reference.PROXY_SERVER, clientSide = Reference.PROXY_CLIENT)
    public static CommonProxy proxy;

    @NetworkCheckHandler
    public boolean checkModList(final Map<String, String> versions, final Side side) {
        return true;
    }

	@Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        Reference.logger.warn("Invalid fingerprint detected!");
    }

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(final FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStopping(final FMLServerStoppingEvent event) {
        proxy.serverStopping(event);
    }
}
