package simpletextoverlay.attachments;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
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
     * about which player the save is for. So I'll just store the data we need separately.
     */
    public static class DataManagerProvider extends DataManager implements INBTSerializable<ListTag> {

        private Player player = null;
        private MinecraftServer server = null;

        public DataManagerProvider() {}

        @Override
        public ListTag serializeNBT() {
            return DataManager.write(player.getUUID());
        }

        @Override
        public void deserializeNBT(ListTag nbt) {
            if (!nbt.isEmpty()) {
                CompoundTag tag = nbt.getCompound(0);

                if (tag.contains("UUID")) {
                    UUID uuid = tag.getUUID("UUID");
                    Player loggedInPlayer = server.getPlayerList().getPlayer(uuid);

                    if (loggedInPlayer != null) {
                        DataManager.read(loggedInPlayer, nbt);
                    }
                }
            }
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public void setServer(MinecraftServer server) {
            this.server = server;
        }

    }

}
