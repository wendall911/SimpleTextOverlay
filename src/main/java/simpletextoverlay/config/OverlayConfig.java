package simpletextoverlay.config;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

import org.apache.commons.lang3.tuple.Pair;

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
        }

    }

    public static class Server {
        private static ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistTags;
        private static ForgeConfigSpec.BooleanValue forceDebug;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("Server Configuration");

            blacklistTags = builder
                .comment("List of tags disallowed.")
                .defineList("blacklistTags", Lists.newArrayList(), e -> e instanceof String);

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

    }
	
	public static void setup() {
		ModLoadingContext context = ModLoadingContext.get();
		context.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
		context.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
	}
	
    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading event) {
        Server.registerSyncOptions();
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading event) {
        Server.updateSyncOptions();

		MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
		if (currentServer == null) return;
		
		PlayerList players = currentServer.getPlayerList();
		for (PlayerEntity player : players.getPlayers()) {
            PacketHandlerHelper.sendServerConfigValues((ServerPlayerEntity)player);
		}
    }

    public static void addOption(ISyncedOption option, ForgeConfigSpec.ConfigValue<?> value) {
        SyncedConfig.addOption(option, String.valueOf(value.get()));
    }

    public static void updateOption(ISyncedOption option, ForgeConfigSpec.ConfigValue<?> value) {
        SyncedConfig.SyncedConfigOption entry = SyncedConfig.getEntry(option.getName());
        entry.value = String.valueOf(value.get());
	}



}
