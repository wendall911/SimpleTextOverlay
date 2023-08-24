package simpletextoverlay;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import simpletextoverlay.proxy.ClientProxy;
import simpletextoverlay.proxy.CommonProxy;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlayForge {

    public static CommonProxy PROXY;

    public SimpleTextOverlayForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);
        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        PROXY.start();

        SimpleTextOverlay.init();
        SimpleTextOverlay.initConfig();
    }

    private void setup(final FMLCommonSetupEvent event) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SimpleTextOverlayClientForge::new);
    }

}
