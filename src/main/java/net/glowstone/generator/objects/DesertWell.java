package net.glowstone.generator.objects;

import net.glowstone.GlowServer;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class DesertWell {

    private static final int RADIUS = 2;
    private static final int HEIGHT = 4;

    public boolean canPlaceAt(World world, int sourceX, int sourceY, int sourceZ) {
        if (world.getBlockAt(sourceX, sourceY, sourceZ).getType() != Material.SAND) {
            return false;
        }
        for (int x = sourceX - RADIUS; x <= sourceX + RADIUS; x++) {
            for (int z = sourceZ - RADIUS; z <= sourceZ + RADIUS; z++) {
                if (world.getBlockAt(x, sourceY - 1, z).getType() == Material.AIR &&
                        world.getBlockAt(x, sourceY - 2, z).getType() == Material.AIR) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean generate(World world, int sourceX, int sourceY, int sourceZ) {

        while (world.getBlockAt(sourceX, sourceY, sourceZ).getType() == Material.AIR &&
                sourceY > 2) {
            sourceY--;
        }

        if (!canPlaceAt(world, sourceX, sourceY, sourceZ)) {
            return false;
        }

        // generates a sandstone base for the well, the top is at floor level
        for (int x = sourceX - RADIUS; x <= sourceX + RADIUS; x++) {
            for (int z = sourceZ - RADIUS; z <= sourceZ + RADIUS; z++) {
                for (int y = sourceY - 1; y <= sourceY; y++) {
                    final Block block = world.getBlockAt(x, y, z);
                    block.setType(Material.SANDSTONE);
                    block.setData((byte) 0);
                }
            }
        }

        // put the sandstone borders above floor
        for (int x = sourceX - RADIUS; x <= sourceX + RADIUS; x++) {
            for (int z = sourceZ - RADIUS; z <= sourceZ + RADIUS; z++) {
                if (x == sourceX - RADIUS || x == sourceX + RADIUS ||
                        z == sourceZ - RADIUS || z == sourceZ + RADIUS) {
                    final Block block = world.getBlockAt(x, sourceY + 1, z);
                    block.setType(Material.SANDSTONE);
                    block.setData((byte) 0);
                }
            }
        }
        // put sandstone steps on middle of each side of the well
        world.getBlockAt(sourceX - RADIUS, sourceY + 1, sourceZ).setType(Material.STEP);
        world.getBlockAt(sourceX - RADIUS, sourceY + 1, sourceZ).setData((byte) 1);
        world.getBlockAt(sourceX + RADIUS, sourceY + 1, sourceZ).setType(Material.STEP);
        world.getBlockAt(sourceX + RADIUS, sourceY + 1, sourceZ).setData((byte) 1);
        world.getBlockAt(sourceX, sourceY + 1, sourceZ - RADIUS).setType(Material.STEP);
        world.getBlockAt(sourceX, sourceY + 1, sourceZ - RADIUS).setData((byte) 1);
        world.getBlockAt(sourceX, sourceY + 1, sourceZ + RADIUS).setType(Material.STEP);
        world.getBlockAt(sourceX, sourceY + 1, sourceZ + RADIUS).setData((byte) 1);

        // generates the sandstone pillars
        for (int y = sourceY + 1; y <= sourceY + HEIGHT - 1; y++) {
            world.getBlockAt(sourceX - RADIUS + 1, y, sourceZ - RADIUS + 1).setType(Material.SANDSTONE);
            world.getBlockAt(sourceX - RADIUS + 1, y, sourceZ - RADIUS + 1).setData((byte) 0);
            world.getBlockAt(sourceX - RADIUS + 1, y, sourceZ + RADIUS - 1).setType(Material.SANDSTONE);
            world.getBlockAt(sourceX - RADIUS + 1, y, sourceZ + RADIUS - 1).setData((byte) 0);
            world.getBlockAt(sourceX + RADIUS - 1, y, sourceZ - RADIUS + 1).setType(Material.SANDSTONE);
            world.getBlockAt(sourceX + RADIUS - 1, y, sourceZ - RADIUS + 1).setData((byte) 0);
            world.getBlockAt(sourceX + RADIUS - 1, y, sourceZ + RADIUS - 1).setType(Material.SANDSTONE);
            world.getBlockAt(sourceX + RADIUS - 1, y, sourceZ + RADIUS - 1).setData((byte) 0);
        }

        // generates the well roof
        for (int x = sourceX - RADIUS + 1; x <= sourceX + RADIUS - 1; x++) {
            for (int z = sourceZ - RADIUS + 1; z <= sourceZ + RADIUS - 1; z++) {
                if (x != sourceX || z != sourceZ) {
                    world.getBlockAt(x, sourceY + HEIGHT, z).setType(Material.STEP);
                    world.getBlockAt(x, sourceY + HEIGHT, z).setData((byte) 1);
                } else {
                    world.getBlockAt(x, sourceY + HEIGHT, z).setType(Material.SANDSTONE);
                    world.getBlockAt(x, sourceY + HEIGHT, z).setData((byte) 0);
                }
            }
        }

        // places the water in the middle of the well
        world.getBlockAt(sourceX, sourceY, sourceZ).setType(Material.STATIONARY_WATER);
        world.getBlockAt(sourceX, sourceY, sourceZ).setData((byte) 0);
        world.getBlockAt(sourceX - 1, sourceY, sourceZ).setType(Material.STATIONARY_WATER);
        world.getBlockAt(sourceX - 1, sourceY, sourceZ).setData((byte) 0);
        world.getBlockAt(sourceX + 1, sourceY, sourceZ).setType(Material.STATIONARY_WATER);
        world.getBlockAt(sourceX + 1, sourceY, sourceZ).setData((byte) 0);
        world.getBlockAt(sourceX, sourceY, sourceZ - 1).setType(Material.STATIONARY_WATER);
        world.getBlockAt(sourceX, sourceY, sourceZ - 1).setData((byte) 0);
        world.getBlockAt(sourceX, sourceY, sourceZ + 1).setType(Material.STATIONARY_WATER);
        world.getBlockAt(sourceX, sourceY, sourceZ + 1).setData((byte) 0);

        GlowServer.logger.info("desert well generated: " + sourceX + "," + sourceY + "," + sourceZ);
        return true;
    }
}
