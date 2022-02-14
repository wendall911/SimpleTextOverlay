package simpletextoverlay.tag;

import java.util.Locale;

import net.minecraft.client.resources.I18n;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.DifficultyInstance;
//import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
//import net.minecraft.world.storage.WorldInfo;

//import net.minecraftforge.common.DimensionManager;

import simpletextoverlay.tag.registry.TagRegistry;
import simpletextoverlay.util.ChunkHelper;

public abstract class TagWorld extends Tag {

    @Override
    public String getCategory() {
        return "world";
    }

    public static class Name extends TagWorld {
        @Override
        public String getValue() {
            /*
            if (server != null) {
                final ServerWorld worldServer = DimensionManager.getWorld(server, player.dimension, false, false);
                if (worldServer != null) {
                    return worldServer.getLevelData().getLevelName();
                }
            }
            */
            return world.getLevelData().getLevelName();
        }
    }

    public static class Size extends TagWorld {
        @Override
        public String getValue() {
            /*
            if (server != null) {
                final ServerWorld worldServer = DimensionManager.getWorld(server, player.dimension, false, false);
                if (worldServer != null) {
                    return String.valueOf(worldServer.getLevel().getLevelData().sizeOnDisk);
                }
            }
            */
            return String.valueOf(world.getLevel().getLevelData().sizeOnDisk);
        }
    }

    public static class SizeMB extends TagWorld {
        @Override
        public String getValue() {
            /*
            if (server != null) {
                final ServerWorld worldServer = DimensionManager.getWorld(server, player.dimension, false, false);
                if (worldServer != null) {
                    return String.format(Locale.ENGLISH, "%.1f", worldServer.getLevel().getLevelData().sizeOnDisk / 1048576.0);
                }
            }
            */
            return String.format(Locale.ENGLISH, "%.1f", world.getLevel().getLevelData().sizeOnDisk / 1048576.0);
        }
    }

    public static class Seed extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(seed);
        }
    }

    public static class Difficulty extends TagWorld {
        @Override
        public String getValue() {
            String dname = "";
            /*
            if (server != null) {
                final ServerWorld worldServer = DimensionManager.getWorld(server, player.dimension, false, false);
                if (worldServer != null) {
                    dname = worldServer.getDifficulty().name();
                }
            }
            else {
                dname = minecraft.options.difficulty.name();
            }
            */

            return I18n.get(dname.substring(0,1).toUpperCase() + dname.substring(1).toLowerCase());
        }
    }

    public static class DifficultyId extends TagWorld {
        @Override
        public String getValue() {
            /*
            if (server != null) {
                final ServerWorld worldServer = DimensionManager.getWorld(server, player.dimension, false, false);
                if (worldServer != null) {
                    return String.valueOf(worldServer.getDifficulty().ordinal());
                }
            }
            */

            return String.valueOf(minecraft.options.difficulty.ordinal());
        }
    }

    public static class LocalDifficulty extends TagWorld {
        @Override
        public String getValue() {
            DifficultyInstance difficulty = world.getLevel().getCurrentDifficultyAt(playerPosition);
            /*
            if (server != null) {
                final ServerWorld worldServer = DimensionManager.getWorld(server, player.dimension, false, false);
                if (worldServer != null) {
                    difficulty = worldServer.getLevel().getCurrentDifficultyAt(playerPosition);
                }
            }
            */
            return String.format(Locale.ENGLISH, "%.2f", difficulty.getEffectiveDifficulty());
        }
    }

    public static class LocalDifficultyClamped extends TagWorld {
        @Override
        public String getValue() {
            DifficultyInstance difficulty = world.getLevel().getCurrentDifficultyAt(playerPosition);
            /*
            if (server != null) {
                final ServerWorld worldServer = DimensionManager.getWorld(server, player.dimension, false, false);
                if (worldServer != null) {
                    difficulty = worldServer.getLevel().getCurrentDifficultyAt(playerPosition);
                }
            }
            */
            return String.format(Locale.ENGLISH, "%.2f", difficulty.getSpecialMultiplier());
        }
    }

    public static class Dimension extends TagWorld {
        @Override
        public String getValue() {
            return "";
            //return DimensionType.getName(player.level.dimension.getType()).toString();
            //return player.level.dimension.getType().toString();
        }
    }

    public static class DimensionId extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(player.dimension.getId());
        }
    }

    public static class Biome extends TagWorld {
        @Override
        public String getValue() {
            return world.getBiome(playerPosition).getName().getContents();
        }
    }

    public static class Daytime extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(world.getLevel().isDay());
        }
    }

    public static class MoonPhase extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(world.getMoonPhase());
        }
    }

    public static class Raining extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(world.isRaining() && world.getBiome(playerPosition).getDownfall() > 0.0f);
        }
    }

    public static class Thundering extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(world.isThundering() && world.getBiome(playerPosition).getDownfall() > 0.0f);
        }
    }

    public static class Snowing extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(world.isRaining() && world.getBiome(playerPosition).shouldSnow(world.getLevel(), playerPosition));
        }
    }

    public static class NextWeatherChange extends TagWorld {
        @Override
        public String getValue() {
            if (server == null) {
                return "?";
            }

            /*
            final WorldInfo worldInfo = world.getLevelData();
            final int clearTime = worldInfo.getClearWeatherTime();
            final float seconds = (clearTime > 0 ? clearTime : worldInfo.getRainTime()) / 20f;
            if (seconds < 60) {
                return String.format(Locale.ENGLISH, "%.1fs", seconds);
            } else if (seconds < 3600) {
                return String.format(Locale.ENGLISH, "%.1fm", seconds / 60);
            }
            return String.format(Locale.ENGLISH, "%.1fh", seconds / 3600);
            */
            return "";
        }
    }

    public static class Slimes extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(hasSeed && ChunkHelper.isSlimeChunk(seed, playerPosition) || world.getBiome(playerPosition) == Biomes.SWAMP && playerPosition.y > 50 && playerPosition.y < 70);
        }
    }

    public static class SlimeChunk extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(hasSeed && ChunkHelper.isSlimeChunk(seed, playerPosition));
        }
    }

    public static class Hardcore extends TagWorld {
        @Override
        public String getValue() {
            return String.valueOf(world.getLevelData().isHardcore());
        }
    }

    public static class Temperature extends TagWorld {
        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.0f", world.getBiome(playerPosition).getTemperature() * 100);
        }
    }

    public static class LocalTemperature extends TagWorld {
        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.2f", world.getBiome(playerPosition).getTemperature(playerPosition) * 100);
        }
    }

    public static class Humidity extends TagWorld {
        @Override
        public String getValue() {
            return String.format(Locale.ENGLISH, "%.0f", world.getBiome(playerPosition).getDownfall() * 100);
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new Name().setName("worldname"));
        TagRegistry.INSTANCE.register(new Size().setName("worldsize"));
        TagRegistry.INSTANCE.register(new SizeMB().setName("worldsizemb"));
        TagRegistry.INSTANCE.register(new Seed().setName("seed"));
        TagRegistry.INSTANCE.register(new Difficulty().setName("difficulty"));
        TagRegistry.INSTANCE.register(new DifficultyId().setName("difficultyid"));
        TagRegistry.INSTANCE.register(new LocalDifficulty().setName("localdifficulty"));
        TagRegistry.INSTANCE.register(new LocalDifficultyClamped().setName("localdifficultyclamped"));
        TagRegistry.INSTANCE.register(new Dimension().setName("dimension"));
        TagRegistry.INSTANCE.register(new DimensionId().setName("dimensionid"));
        TagRegistry.INSTANCE.register(new Biome().setName("biome"));
        TagRegistry.INSTANCE.register(new Daytime().setName("daytime"));
        TagRegistry.INSTANCE.register(new MoonPhase().setName("moonphase"));
        TagRegistry.INSTANCE.register(new Raining().setName("raining"));
        TagRegistry.INSTANCE.register(new Thundering().setName("thundering"));
        TagRegistry.INSTANCE.register(new Snowing().setName("snowing"));
        TagRegistry.INSTANCE.register(new NextWeatherChange().setName("nextweatherchange").setAliases("nextrain"));
        TagRegistry.INSTANCE.register(new Slimes().setName("slimes"));
        TagRegistry.INSTANCE.register(new SlimeChunk().setName("slimechunk"));
        TagRegistry.INSTANCE.register(new Hardcore().setName("hardcore"));
        TagRegistry.INSTANCE.register(new Temperature().setName("temperature"));
        TagRegistry.INSTANCE.register(new LocalTemperature().setName("localtemperature"));
        TagRegistry.INSTANCE.register(new Humidity().setName("humidity"));
    }

}
