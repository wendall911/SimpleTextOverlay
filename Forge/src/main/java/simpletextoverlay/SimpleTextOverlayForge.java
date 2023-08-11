package simpletextoverlay;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import simpletextoverlay.proxy.ClientProxy;
import simpletextoverlay.proxy.CommonProxy;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlayForge {

    public static CommonProxy PROXY;

    public SimpleTextOverlayForge() {
        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        PROXY.start();
    }

    private void setup(final FMLCommonSetupEvent event) {
        //MinecraftForge.EVENT_BUS.register(ServerEventListener.class);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SimpleTextOverlayClientForge::new);
    }

}
