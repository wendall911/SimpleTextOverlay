package simpletextoverlay;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import simpletextoverlay.client.ModKeyBindings;
import simpletextoverlay.event.KeyEventHandler;
import simpletextoverlay.network.ForgeNetworkManager;

@Mod(SimpleTextOverlay.MODID)
public class SimpleTextOverlayForge {

    private static boolean setupDone = false;

    public SimpleTextOverlayForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(this::keyBindingSetup);
            bus.addListener(this::clientSetup);
        });

        SimpleTextOverlay.init();
        SimpleTextOverlay.initConfig();
        ForgeNetworkManager.init();
    }

    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyEventHandler());
    }

    @OnlyIn(Dist.CLIENT)
    public void keyBindingSetup(RegisterKeyMappingsEvent event) {
        if (!setupDone && ModList.get().isLoaded("corpse")) {
            ModKeyBindings.init(event);
            setupDone = true;
        }
    }

}
