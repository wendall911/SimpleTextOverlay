package simpletextoverlay.util;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

public class ThaumcraftHelper {

    public static double getVis(World world, BlockPos pos) {
        return (double) AuraHelper.getVis(world, pos);
    }

    public static int getAuraBase(World world, BlockPos pos) {
        return AuraHelper.getAuraBase(world, pos);
    }

    public static double getFlux(World world, BlockPos pos) {
        return (double) AuraHelper.getFlux(world, pos);
    }

    public static IPlayerWarp getWarp(EntityPlayerSP player) {
        return player.getCapability(ThaumcraftCapabilities.WARP, null);
    }

    public static int getWarpNorm(EntityPlayerSP player) {
        return getWarp(player).get(IPlayerWarp.EnumWarpType.NORMAL);
    }

    public static int getWarpPerm(EntityPlayerSP player) {
        return getWarp(player).get(IPlayerWarp.EnumWarpType.PERMANENT);
    }

    public static int getWarpTemp(EntityPlayerSP player) {
        return getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY);
    }

}
