package simpletextoverlay.attachments;

import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.core.HolderLookup;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.overlay.compass.DataManager;

public class AttachmentDataManager {

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPE_DEFERRED_REGISTER =
        DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, SimpleTextOverlay.MODID);
    public static final DataManagerProvider DATA_MANAGER_INSTANCE = new DataManagerProvider();
    public static final Supplier<AttachmentType<? extends DataManager>> COMPASS_DATA =
        ATTACHMENT_TYPE_DEFERRED_REGISTER.register(
            "sto_provider",
            () -> AttachmentType.serializable(() -> DATA_MANAGER_INSTANCE).copyOnDeath().build()
        );

    public static void init(IEventBus eventBus) {
        ATTACHMENT_TYPE_DEFERRED_REGISTER.register(eventBus);
    }

    public static Optional<DataManager> getData(final Player player) {
        return Optional.of(player.getData(COMPASS_DATA.get()));
    }

    /*
     * Just using this as an empty data object. Maybe should use capability? Probably. But the new NeoForge system
     * is kinda garbage, as-in I can't persist capability data per-player, and this is global, and I get ZERO context
     * about which player the save is for. Would be nice if was implemented like Cardinal Components. Maybe just give a
     * way to persist capabilities?
     */
    public static class DataManagerProvider extends DataManager implements INBTSerializable<ListTag> {

        private Player player = null;

        public DataManagerProvider() {}

        @Override
        public ListTag serializeNBT(HolderLookup.Provider provider) {
            return DataManager.write(player.getUUID());
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, ListTag nbt) {
            if (!nbt.isEmpty()) {
                DataManager.read(nbt);
            }
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

    }

}
