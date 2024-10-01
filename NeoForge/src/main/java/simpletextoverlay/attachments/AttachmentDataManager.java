package simpletextoverlay.attachments;

import java.util.function.Supplier;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.overlay.compass.DataManager;

public class AttachmentDataManager {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPE_DEFERRED_REGISTER =
        DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, SimpleTextOverlay.MODID);
    public static final Supplier<AttachmentType<? extends DataManager>> COMPASS_DATA =
        ATTACHMENT_TYPE_DEFERRED_REGISTER.register(
            "sto_provider",
            () -> AttachmentType.serializable(CompassDataProvider.DataManagerProvider::new).build()
            //() -> AttachmentType.serializable(DataManagerProvider::new).copyOnDeath().build()
        );

    public static void init(IEventBus eventBus) {
        ATTACHMENT_TYPE_DEFERRED_REGISTER.register(eventBus);
    }

}
