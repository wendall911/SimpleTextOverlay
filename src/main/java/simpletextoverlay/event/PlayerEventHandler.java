package simpletextoverlay.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;

import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import simpletextoverlay.client.gui.overlay.OverlayManager;
import simpletextoverlay.config.ConfigHandler;
import simpletextoverlay.event.GameOverlayEventHandler;
import simpletextoverlay.integrations.TagBloodMagic;
import simpletextoverlay.tag.Tag;
import simpletextoverlay.util.PacketHandlerHelper;

public class PlayerEventHandler {

    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            PacketHandlerHelper.sendServerConfigValues(player);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onSinglePlayerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        OverlayManager overlayManager = OverlayManager.INSTANCE;
        overlayManager.setTagBlacklist(ConfigHandler.server.blacklistTags);
        GameOverlayEventHandler.INSTANCE.forceDebug = ConfigHandler.server.forceDebug;
        TagBloodMagic.init();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEating(final LivingEntityUseItemEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            if (event.getItem().getItem() instanceof ItemFood) {
                Tag.setEating(true);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEating(final LivingEntityUseItemEvent.Stop event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            if (event.getItem().getItem() instanceof ItemFood) {
                Tag.setEating(false);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEating(final LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            if (event.getItem().getItem() instanceof ItemFood) {
                Tag.setEating(false);
            }
        }
    }

}
