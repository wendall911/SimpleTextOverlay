package simpletextoverlay.tag;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.MinecraftServer;
//import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.World;

import simpletextoverlay.overlay.Info;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.MBlockPos;
import simpletextoverlay.util.Vector3f;

public abstract class Tag {

    protected static final Minecraft minecraft = Minecraft.getInstance();
    protected static final MBlockPos playerPosition = new MBlockPos();
    protected static final Vector3f playerMotion = new Vector3f();
    protected static MinecraftServer server;
    protected static World world;
    protected static ClientPlayerEntity player;
    protected static List<Info> info;
    protected static boolean hasSeed = false;
    protected static long seed = 0;
    protected static boolean eating = false;
    protected static String[] blacklist = new String[0];
    protected static boolean isLoggedIn = false;

    private String name = null;
    private String[] aliases = new String[0];

    public Tag setName(final String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Tag setAliases(final String... aliases) {
        this.aliases = aliases;
        return this;
    }

    public static void setIsLoggedIn(boolean bool) {
        isLoggedIn = bool;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public boolean isIndexed() {
        return false;
    }

    public int getMaximumIndex() {
        return -1;
    }

    public String getRawName() {
        return this.name;
    }

    public String getFormattedName() {
        return this.name + (isIndexed() ? String.format("[0..%d]", getMaximumIndex()) : "");
    }

    public String getLocalizedCategory() {
        return I18n.get(SimpleTextOverlay.MODID + ".tag.category." + getCategory() + ".name");
    }

    public String getLocalizedDescription() {
        return I18n.get(SimpleTextOverlay.MODID + ".tag." + getRawName() + ".desc");
    }

    public abstract String getCategory();

    public abstract String getValue();

    public static void setServer(final MinecraftServer server) {
        Tag.server = server;

        try {
            //setSeed(Tag.server.getLevel(DimensionType.OVERWORLD).getSeed());
            setSeed(1);
        } catch (final Exception e) {
            unsetSeed();
        }
    }

    public static void setSeed(final long seed) {
        Tag.hasSeed = true;
        Tag.seed = seed;
    }

    public static void unsetSeed() {
        Tag.hasSeed = false;
        Tag.seed = 0;
    }

    public static void setEating(final boolean status) {
        Tag.eating = status;
    }

    public static void setBlacklist(final String... blacklist) {
        Tag.blacklist = blacklist;
    }

    public static void setWorld(final World world) {
        Tag.world = world;
    }

    public static void setPlayer(final ClientPlayerEntity player) {
        Tag.player = player;

        if (player != null) {
            playerPosition.set(player);
            playerMotion.set((float)player.getX(), (float)player.getY(), (float)player.getZ());
        }
    }

    public static void setInfo(final List<Info> info) {
        Tag.info = info;
    }

    public static void releaseResources() {
        setWorld(null);
        setPlayer(null);
        //TagNearbyPlayer.releaseResources();
        //TagPlayerPotion.releaseResources();
    }

    public static String getIconTag(final Info info) {
        String str = "";
        for (int i = 0; i < info.getWidth() && minecraft.font.width(str) < info.getWidth(); i++) {
            str += " ";
        }
        return String.format("{ICON|%s}", str);
    }

}
