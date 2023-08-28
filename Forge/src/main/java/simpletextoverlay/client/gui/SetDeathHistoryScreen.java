package simpletextoverlay.client.gui;

import java.util.List;

import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.gui.DeathHistoryScreen;

import simpletextoverlay.network.NetworkManager;
import simpletextoverlay.network.SetDeathLocation;

public class SetDeathHistoryScreen extends DeathHistoryScreen {

    private final int hSplit;

    public SetDeathHistoryScreen(List<Death> deaths) {
        super(deaths);

        imageWidth = 248;

        hSplit = imageWidth / 2;
    }

    @Override
    public boolean mouseClicked(double x, double y, int clickType) {
        if (x >= leftPos + 7 && x <= leftPos + hSplit && y >= topPos + 70 && y <= topPos + 100 + font.lineHeight) {
            Death death = getCurrentDeath();

            NetworkManager.INSTANCE.sendToServer(new SetDeathLocation(death));

            if (minecraft != null) {
                minecraft.setScreen(null);
            }

            return true;
        }

        return super.mouseClicked(x, y, clickType);
    }

}
