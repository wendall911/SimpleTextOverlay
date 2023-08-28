package simpletextoverlay.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import de.maxhenkel.corpse.corelib.death.Death;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import simpletextoverlay.client.gui.SetDeathHistoryScreen;

public class OpenHistory implements IData {

    private List<Death> deaths;

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

    public static void init(int idx) {
        NetworkManager.registerMessage(idx, OpenHistory.class, OpenHistory::new);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
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
    public void process(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            openScreen(ctx);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void openScreen(Supplier<NetworkEvent.Context> ctx) {
        if (deaths.size() > 0) {
            ctx.get().enqueueWork(() -> Minecraft.getInstance().setScreen(new SetDeathHistoryScreen(deaths)));
        } else if (Minecraft.getInstance().player != null) {
            ctx.get().enqueueWork(() -> Minecraft.getInstance().player.displayClientMessage(Component.translatable("message.corpse.no_death_history"), true));
        }
    }

}
