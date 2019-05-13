package simpletextoverlay.tag;

import com.google.common.collect.UnmodifiableIterator;

import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import simpletextoverlay.tag.registry.TagRegistry;

public abstract class TagMouseOver extends Tag {
    @Override
    public String getCategory() {
        return "mouseover";
    }

    public static class Name extends TagMouseOver {
        @Override
        public String getValue() {
            final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
            if (objectMouseOver != null) {
                if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                    return objectMouseOver.entityHit.getDisplayName().getFormattedText();
                } else if (objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    final IBlockState blockState = getBlockState(objectMouseOver);
                    final Block block = blockState.getBlock();
                    final ItemStack pickBlock = block.getPickBlock(blockState, objectMouseOver, world, objectMouseOver.getBlockPos(), player);
                    return pickBlock.getDisplayName();
                }
            }
            return "";
        }
    }

    public static class UniqueName extends TagMouseOver {
        @Override
        public String getValue() {
            final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
            if (objectMouseOver != null) {
                if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                    final String name = EntityList.getEntityString(objectMouseOver.entityHit);
                    if (name != null) {
                        return name;
                    }
                } else if (objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    final Block block = world.getBlockState(objectMouseOver.getBlockPos()).getBlock();
                    return String.valueOf(Block.REGISTRY.getNameForObject(block));
                }
            }
            return "";
        }
    }

    public static class Id extends TagMouseOver {
        @Override
        public String getValue() {
            final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
            if (objectMouseOver != null) {
                if (objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                    return String.valueOf(objectMouseOver.entityHit.getEntityId());
                } else if (objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    final Block block = world.getBlockState(objectMouseOver.getBlockPos()).getBlock();
                    return String.valueOf(Block.REGISTRY.getIDForObject(block));
                }
            }
            return "0";
        }
    }

    public static class Metadata extends TagMouseOver {
        @Override
        public String getValue() {
            final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
            if (objectMouseOver != null) {
                if (objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    final IBlockState blockState = getBlockState(objectMouseOver);
                    return String.valueOf(blockState.getBlock().getMetaFromState(blockState));
                }
            }
            return "0";
        }
    }

    public static class Snowy extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("snowy");
        }
    }

    public static class Facing extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("facing");
        }
    }

    public static class Half extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("half");
        }
    }

    public static class CheckDecay extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("check_decay");
        }
    }

    public static class Type extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("type");
        }
    }

    public static class Explode extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("explode");
        }
    }

    public static class Delay extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("delay");
        }
    }

    public static class Locked extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("locked");
        }
    }

    public static class Hinge extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("hinge");
        }
    }

    public static class Open extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("open");
        }
    }

    public static class Powered extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("powered");
        }
    }

    public static class InWall extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("in_wall");
        }
    }

    public static class Triggered extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("triggered");
        }
    }

    public static class Extended extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("extended");
        }
    }

    public static class Attached extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("attached");
        }
    }

    public static class Shape extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("shape");
        }
    }

    public static class Level extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("level");
        }
    }

    public static class HasBottle0 extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("has_bottle_0");
        }
    }

    public static class HasBottle1 extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("has_bottle_1");
        }
    }

    public static class HasBottle2 extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("has_bottle_2");
        }
    }

    public static class North extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("north");
        }
    }

    public static class South extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("south");
        }
    }

    public static class East extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("east");
        }
    }

    public static class West extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("west");
        }
    }

    public static class Layers extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("layers");
        }
    }

    public static class Age extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("age");
        }
    }

    public static class Decayable extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("decayable");
        }
    }

    public static class Axis extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("axis");
        }
    }

    public static class Variant extends TagMouseOver {
        @Override
        public String getValue() {
            return getProp("variant");
        }
    }

    private static IBlockState getBlockState(RayTraceResult objectMouseOver) {
        final BlockPos blockPos = objectMouseOver.getBlockPos();
        final IBlockState blockState = world.getBlockState(blockPos);

        return blockState.getActualState(world, blockPos);
    }

    protected <T extends Comparable<T>> String getProp(String prop) {
        final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
        IProperty<T> iproperty;

        if (objectMouseOver != null) {
            if (objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                final IBlockState blockState = getBlockState(objectMouseOver);

                for (UnmodifiableIterator unmodifiableiterator = blockState.getProperties().entrySet().iterator(); unmodifiableiterator.hasNext();) {
                    Entry < IProperty<?>, Comparable<? >> entry = (Entry)unmodifiableiterator.next();
                    iproperty = (IProperty)entry.getKey();
                    if (iproperty.getName() == prop) {
                        return iproperty.getName((T)entry.getValue());
                    }
                }
            }
        }

        return "";
    }

    public static class PowerWeak extends TagMouseOver {
        @Override
        public String getValue() {
            final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
            if (objectMouseOver != null) {
                if (objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    int power = -1;
                    for (final EnumFacing side : EnumFacing.VALUES) {
                        final BlockPos pos = objectMouseOver.getBlockPos().offset(side);
                        IBlockState blockState = world.getBlockState(pos);
                        blockState = blockState.getActualState(world, pos);
                        power = Math.max(power, blockState.getWeakPower(world, pos, side));

                        if (power >= 15) {
                            break;
                        }
                    }
                    return String.valueOf(power);
                }
            }
            return "-1";
        }
    }

    public static class PowerStrong extends TagMouseOver {
        @Override
        public String getValue() {
            final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
            if (objectMouseOver != null) {
                if (objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    final BlockPos pos = objectMouseOver.getBlockPos();
                    IBlockState blockState = world.getBlockState(pos);
                    blockState = blockState.getActualState(world, pos);
                    int power = -1;
                    for (final EnumFacing side : EnumFacing.VALUES) {
                        power = Math.max(power, blockState.getStrongPower(world, pos, side));

                        if (power >= 15) {
                            break;
                        }
                    }
                    return String.valueOf(power);
                }
            }
            return "-1";
        }
    }

    public static class PowerInput extends TagMouseOver {
        @Override
        public String getValue() {
            final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
            if (objectMouseOver != null) {
                if (objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return String.valueOf(isBlockIndirectlyGettingPowered(world, objectMouseOver.getBlockPos()));
                }
            }
            return "-1";
        }
    }

    public static int isBlockIndirectlyGettingPowered(World world, BlockPos pos) {
        int i = 0;

        for (EnumFacing enumfacing : EnumFacing.values()) {
            final BlockPos offset = pos.offset(enumfacing);
            if (world.isBlockLoaded(offset)) {
                int j = world.getRedstonePower(offset, enumfacing);

                if (j >= 15) {
                    return 15;
                }

                if (j > i) {
                    i = j;
                }
            }
        }

        return i;
    }

    public static class LookingAtX extends TagMouseOver {
        @Override
        public String getValue() {
            final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
            if (objectMouseOver != null && objectMouseOver.getBlockPos() != null) {
                BlockPos blockpos = objectMouseOver.getBlockPos();
                return String.valueOf(blockpos.getX());
            }
            return "";
        }
    }

    public static class LookingAtY extends TagMouseOver {
        @Override
        public String getValue() {
            final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
            if (objectMouseOver != null && objectMouseOver.getBlockPos() != null) {
                BlockPos blockpos = objectMouseOver.getBlockPos();
                return String.valueOf(blockpos.getY());
            }
            return "";
        }
    }

    public static class LookingAtZ extends TagMouseOver {
        @Override
        public String getValue() {
            final RayTraceResult objectMouseOver = minecraft.objectMouseOver;
            if (objectMouseOver != null && objectMouseOver.getBlockPos() != null) {
                BlockPos blockpos = objectMouseOver.getBlockPos();
                return String.valueOf(blockpos.getZ());
            }
            return "";
        }
    }

    public static void register() {
        TagRegistry.INSTANCE.register(new Age().setName("blockage"));
        TagRegistry.INSTANCE.register(new Attached().setName("blockattached"));
        TagRegistry.INSTANCE.register(new Axis().setName("blockaxis"));
        TagRegistry.INSTANCE.register(new CheckDecay().setName("blockcheckdecay"));
        TagRegistry.INSTANCE.register(new Decayable().setName("blockdecayable"));
        TagRegistry.INSTANCE.register(new Delay().setName("blockdelay"));
        TagRegistry.INSTANCE.register(new East().setName("blockeast"));
        TagRegistry.INSTANCE.register(new Explode().setName("blockexplode"));
        TagRegistry.INSTANCE.register(new Extended().setName("blockextended"));
        TagRegistry.INSTANCE.register(new Facing().setName("blockfacing"));
        TagRegistry.INSTANCE.register(new Half().setName("blockhalf"));
        TagRegistry.INSTANCE.register(new HasBottle0().setName("blockhasbottle0"));
        TagRegistry.INSTANCE.register(new HasBottle1().setName("blockhasbottle1"));
        TagRegistry.INSTANCE.register(new HasBottle2().setName("blockhasbottle2"));
        TagRegistry.INSTANCE.register(new Hinge().setName("blockhinge"));
        TagRegistry.INSTANCE.register(new InWall().setName("blockinwall"));
        TagRegistry.INSTANCE.register(new Layers().setName("blocklayers"));
        TagRegistry.INSTANCE.register(new Level().setName("blocklevel"));
        TagRegistry.INSTANCE.register(new Locked().setName("blocklocked"));
        TagRegistry.INSTANCE.register(new Name().setName("mouseovername"));
        TagRegistry.INSTANCE.register(new Open().setName("blockopen"));
        TagRegistry.INSTANCE.register(new Powered().setName("blockpowered"));
        TagRegistry.INSTANCE.register(new Shape().setName("blockshape"));
        TagRegistry.INSTANCE.register(new UniqueName().setName("mouseoveruniquename"));
        TagRegistry.INSTANCE.register(new Id().setName("mouseoverid"));
        TagRegistry.INSTANCE.register(new LookingAtX().setName("lookingatx"));
        TagRegistry.INSTANCE.register(new LookingAtY().setName("lookingaty"));
        TagRegistry.INSTANCE.register(new LookingAtZ().setName("lookingatz"));
        TagRegistry.INSTANCE.register(new Metadata().setName("mouseovermetadata"));
        TagRegistry.INSTANCE.register(new North().setName("blocknorth"));
        TagRegistry.INSTANCE.register(new PowerWeak().setName("mouseoverpowerweak"));
        TagRegistry.INSTANCE.register(new PowerStrong().setName("mouseoverpowerstrong"));
        TagRegistry.INSTANCE.register(new PowerInput().setName("mouseoverpowerinput"));
        TagRegistry.INSTANCE.register(new Snowy().setName("blocksnowy"));
        TagRegistry.INSTANCE.register(new South().setName("blocksouth"));
        TagRegistry.INSTANCE.register(new Triggered().setName("blocktriggered"));
        TagRegistry.INSTANCE.register(new Type().setName("blocktype"));
        TagRegistry.INSTANCE.register(new Variant().setName("blockvariant"));
        TagRegistry.INSTANCE.register(new West().setName("blockwest"));
    }
}
