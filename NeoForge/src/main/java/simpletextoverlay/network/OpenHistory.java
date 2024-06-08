package simpletextoverlay.network;

import java.util.ArrayList;
import java.util.List;

import de.maxhenkel.corpse.corelib.death.Death;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import simpletextoverlay.SimpleTextOverlay;

public class OpenHistory implements CustomPacketPayload {

    private List<Death> deaths;

    public static final ResourceLocation ID = new ResourceLocation(SimpleTextOverlay.MODID, "open_history");

    public OpenHistory(List<Death> deaths) {
        this.deaths = deaths;
    }

    public OpenHistory(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();

        if (tag != null) {
            ListTag list = tag.getList("Deaths", 10);

            deaths = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                deaths.add(Death.fromNBT(list.getCompound(i)));
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        ListTag list = new ListTag();
        CompoundTag tag = new CompoundTag();

        for (Death death : deaths) {
            CompoundTag dTag = death.toNBT(false);

            list.add(dTag);
        }

        tag.put("Deaths", list);
        buf.writeNbt(tag);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public List<Death>  getDeaths() {
        return deaths;
    }

}
