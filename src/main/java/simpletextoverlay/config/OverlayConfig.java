package simpletextoverlay.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

import org.apache.commons.lang3.tuple.Pair;

import simpletextoverlay.overlay.OverlayManager;
import simpletextoverlay.reference.Names;
import simpletextoverlay.SimpleTextOverlay;
import simpletextoverlay.util.Alignment;
import simpletextoverlay.util.PacketHandlerHelper;

@Mod.EventBusSubscriber(modid = SimpleTextOverlay.MODID, bus = MOD)
public class OverlayConfig {

	private static String localizationPath(String path) {
		return "config." + SimpleTextOverlay.MODID + "." + path;
	}
	
	public static final Server SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;
	
	static {
		Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
		SERVER = specPair.getLeft();
		SERVER_SPEC = specPair.getRight();
	}
	
	public static final Client CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;
	
	static {
		Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT = specPair.getLeft();
		CLIENT_SPEC = specPair.getRight();
	}

    public static Side side;

    public static class Client {
        public static ForgeConfigSpec.IntValue topleftX;
        public static ForgeConfigSpec.IntValue topleftY;
        public static ForgeConfigSpec.IntValue topcenterX;
        public static ForgeConfigSpec.IntValue topcenterY;
        public static ForgeConfigSpec.IntValue toprightX;
        public static ForgeConfigSpec.IntValue toprightY;
        public static ForgeConfigSpec.IntValue middleleftX;
        public static ForgeConfigSpec.IntValue middleleftY;
        public static ForgeConfigSpec.IntValue middlecenterX;
        public static ForgeConfigSpec.IntValue middlecenterY;
        public static ForgeConfigSpec.IntValue middlerightX;
        public static ForgeConfigSpec.IntValue middlerightY;
        public static ForgeConfigSpec.IntValue bottomleftX;
        public static ForgeConfigSpec.IntValue bottomleftY;
        public static ForgeConfigSpec.IntValue bottomcenterX;
        public static ForgeConfigSpec.IntValue bottomcenterY;
        public static ForgeConfigSpec.IntValue bottomrightX;
        public static ForgeConfigSpec.IntValue bottomrightY;
        public static ForgeConfigSpec.ConfigValue<String> defaultOverlayFile;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> cycleOverlays;
        public static ForgeConfigSpec.BooleanValue replaceDebug;
        public static ForgeConfigSpec.ConfigValue<String> debugOverlayFile;
        public static ForgeConfigSpec.BooleanValue showInChat;
        public static ForgeConfigSpec.BooleanValue showOnPlayerList;
        public static ForgeConfigSpec.DoubleValue scale;
        public static ForgeConfigSpec.BooleanValue showOverlayPotions;
        public static ForgeConfigSpec.BooleanValue showOverlayItemIcons;

        Client(ForgeConfigSpec.Builder builder) {
            builder.push("Alignment");

            topleftX = builder
                .translation(localizationPath("topleftX"))
                .comment("Offsets for the top left side of the screen.")
                .defineInRange("topleftX", Alignment.TOPLEFT.defaultX, -100, 100);

            topleftY = builder
                .defineInRange("topleftY", Alignment.TOPLEFT.defaultY, -100, 100);

            topcenterX = builder
                .translation(localizationPath("topcenterX"))
                .comment("Offsets for the top center side of the screen.")
                .defineInRange("topcenterX", Alignment.TOPCENTER.defaultX, -100, 100);

            topcenterY = builder
                .defineInRange("topcenterY", Alignment.TOPCENTER.defaultY, -100, 100);

            toprightX = builder
                .translation(localizationPath("toprightX"))
                .comment("Offsets for the top right side of the screen.")
                .defineInRange("toprightX", Alignment.TOPRIGHT.defaultX, -100, 100);

            toprightY = builder
                .defineInRange("toprightY", Alignment.TOPRIGHT.defaultY, -100, 100);

            middleleftX = builder
                .translation(localizationPath("middleleftX"))
                .comment("Offsets for the middle left side of the screen.")
                .defineInRange("middleleftX", Alignment.MIDDLELEFT.defaultX, -100, 100);

            middleleftY = builder
                .defineInRange("middleleftY", Alignment.MIDDLELEFT.defaultY, -100, 100);

            middlecenterX = builder
                .translation(localizationPath("middlecenterX"))
                .comment("Offsets for the middle center side of the screen.")
                .defineInRange("middlecenterX", Alignment.MIDDLECENTER.defaultX, -100, 100);

            middlecenterY = builder
                .defineInRange("middlecenterY", Alignment.MIDDLECENTER.defaultY, -100, 100);

            middlerightX = builder
                .translation(localizationPath("middlerightX"))
                .comment("Offsets for the middle right side of the screen.")
                .defineInRange("middlerightX", Alignment.MIDDLERIGHT.defaultX, -100, 100);

            middlerightY = builder
                .defineInRange("middlerightY", Alignment.MIDDLERIGHT.defaultY, -100, 100);

            bottomleftX = builder
                .translation(localizationPath("bottomleftX"))
                .comment("Offsets for the bottom left side of the screen.")
                .defineInRange("bottomleftX", Alignment.BOTTOMLEFT.defaultX, -100, 100);

            bottomleftY = builder
                .defineInRange("bottomleftY", Alignment.BOTTOMLEFT.defaultY, -100, 100);

            bottomcenterX = builder
                .translation(localizationPath("bottomcenterX"))
                .comment("Offsets for the bottom center side of the screen.")
                .defineInRange("bottomcenterX", Alignment.BOTTOMCENTER.defaultX, -100, 100);

            bottomcenterY = builder
                .defineInRange("bottomcenterY", Alignment.BOTTOMCENTER.defaultY, -100, 100);

            bottomrightX = builder
                .translation(localizationPath("bottomrightX"))
                .comment("Offsets for the bottom right side of the screen.")
                .defineInRange("bottomrightX", Alignment.BOTTOMRIGHT.defaultX, -100, 100);

            bottomrightY = builder
                .defineInRange("bottomrightY", Alignment.BOTTOMRIGHT.defaultY, -100, 100);

            builder.pop();

            builder.push("General");

            defaultOverlayFile = builder
                .translation(localizationPath("defaultOverlayFile"))
                .comment("The configuration that should be loaded on startup.")
			    .define("defaultOverlayFile", Names.Files.FILE_JSON);

            cycleOverlays = builder
                .translation(localizationPath("cycleOverlays"))
                .comment("Overlay configs to cycle through when using /sto cycle or cycle Hotkey.")
                .defineList("cycleOverlays", Names.Files.BUILTINS, e -> e instanceof String);

            replaceDebug = builder
                .translation(localizationPath("replaceDebug"))
                .comment("Replace the debug overlay (F3) with the SimpleTextOverlay overlay.")
			    .define("replaceDebug", false);

            debugOverlayFile = builder
                .translation(localizationPath("debugOverlayFile"))
                .comment("The file used for the debug overlay (F3).")
			    .define("debugOverlayFile", Names.Files.FILE_DEBUG);

            showInChat = builder
                .translation(localizationPath("showInChat"))
                .comment("Display the overlay in chat.")
			    .define("showInChat", true);

            showOnPlayerList = builder
                .translation(localizationPath("showOnPlayerList"))
                .comment("Display the overlay on the player list.")
			    .define("showOnPlayerList", true);

            scale = builder
                .translation(localizationPath("scale"))
                .comment("The overlay will be scaled by this amount.")
			    .defineInRange("scale", 1.0, 0.5, 2.0);

            showOverlayPotions = builder
                .translation(localizationPath("showOverlayPotions"))
                .comment("Display the vanilla potion effects overlay.")
			    .define("showOverlayPotions", true);

            showOverlayItemIcons = builder
                .translation(localizationPath("showOverlayItemIcons"))
                .comment("Display the item overlay on icon (durability, stack size).")
			    .define("showOverlayItemIcons", true);

            builder.pop();
        }

        public static void onLoad() {
            side = side.CLIENT;
            OverlayManager overlayManager = OverlayManager.INSTANCE;
            Path configPath = FMLPaths.CONFIGDIR.get();
            Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString());

            overlayManager.init(modConfigPath.toFile());
            overlayManager.setTagBlacklist(SERVER.blacklistTags.get().stream().toArray(String[]::new));

            applyConfigSettings();
        }

        public static void onFileChange() {
            applyConfigSettings();
        }

    }

    public static class Server {
        private static ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistTags;
        private static ForgeConfigSpec.BooleanValue forceDebug;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("Server Configuration");

            blacklistTags = builder
                .comment("List of tags disallowed. Example: [\"tag1\", \"tag2\"]")
                .defineList("blacklistTags", new ArrayList<String>(), e -> e instanceof String);

            forceDebug = builder
                .comment("Force F3 debug screen to be replaced.")
                .define("forceDebug", false);

            builder.pop();
        }

        public static void registerSyncOptions() {
            addOption(OverlayOption.BLACKLIST_TAGS, blacklistTags);
            addOption(OverlayOption.FORCE_DEBUG, forceDebug);
        }

        public static void updateSyncOptions() {
            updateOption(OverlayOption.BLACKLIST_TAGS, blacklistTags);
            updateOption(OverlayOption.FORCE_DEBUG, forceDebug);
        }

        public static void onLoad() {
            side = side.SERVER;
            registerSyncOptions();
        }

        public static void onFileChange() {
            updateSyncOptions();

            MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
            if (currentServer == null) return;
            
            PlayerList players = currentServer.getPlayerList();
            for (PlayerEntity player : players.getPlayers()) {
                PacketHandlerHelper.sendServerConfigValues((ServerPlayerEntity)player);
            }
        }

    }
	
	public static void setup() {
		ModLoadingContext context = ModLoadingContext.get();
		context.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
		context.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
	}
	
    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading event) {
        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, ()->()->Server.onLoad());
        DistExecutor.runWhenOn(Dist.CLIENT, ()->()->Client.onLoad());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading event) {
        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, ()->()->Server.onFileChange());
        DistExecutor.runWhenOn(Dist.CLIENT, ()->()->Client.onFileChange());
    }

    public static void addOption(ISyncedOption option, ForgeConfigSpec.ConfigValue<?> value) {
        SyncedConfig.addOption(option, String.valueOf(value.get()));
    }

    public static void updateOption(ISyncedOption option, ForgeConfigSpec.ConfigValue<?> value) {
        SyncedConfig.SyncedConfigOption entry = SyncedConfig.getEntry(option.getName());
        entry.value = String.valueOf(value.get());
	}

    public static Map<String, Integer> getAlignment() {
        Map<String, Integer> alignmentMap = new HashMap<>();

        alignmentMap.put("topleftX", CLIENT.topleftX.get());
        alignmentMap.put("topleftY", CLIENT.topleftY.get());
        alignmentMap.put("topcenterX", CLIENT.topcenterX.get());
        alignmentMap.put("topcenterY", CLIENT.topcenterY.get());
        alignmentMap.put("toprightX", CLIENT.toprightX.get());
        alignmentMap.put("toprightY", CLIENT.toprightY.get());
        alignmentMap.put("middleleftX", CLIENT.middleleftX.get());
        alignmentMap.put("middleleftY", CLIENT.middleleftY.get());
        alignmentMap.put("middlecenterX", CLIENT.middlecenterX.get());
        alignmentMap.put("middlecenterY", CLIENT.middlecenterY.get());
        alignmentMap.put("middlerightX", CLIENT.middlerightX.get());
        alignmentMap.put("middlerightY", CLIENT.middlerightY.get());
        alignmentMap.put("bottomleftX", CLIENT.bottomleftX.get());
        alignmentMap.put("bottomleftY", CLIENT.bottomleftY.get());
        alignmentMap.put("bottomcenterX", CLIENT.bottomcenterX.get());
        alignmentMap.put("bottomcenterY", CLIENT.bottomcenterY.get());
        alignmentMap.put("bottomrightX", CLIENT.bottomrightX.get());
        alignmentMap.put("bottomrightY", CLIENT.bottomrightY.get());

        return alignmentMap;
    }

    private static void applyConfigSettings() {
        Map<String, Integer> alignmentMap = getAlignment();
        for (Alignment alignment : Alignment.values()) {
            String alignmentName = alignment.toString().toLowerCase(Locale.ENGLISH);
            String alignmentNameX = alignmentName + "X";
            String alignmentNameY = alignmentName + "Y";
            int x = alignmentMap.getOrDefault(alignmentNameX, alignment.defaultX);
            int y = alignmentMap.getOrDefault(alignmentNameY, alignment.defaultY);

            alignment.setX(x);
            alignment.setY(y);
        }
    }

    public enum Side {

        CLIENT(),
        SERVER();

        Side() {}

    }

}
