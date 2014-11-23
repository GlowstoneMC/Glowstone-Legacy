package net.glowstone.generator.structures;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public abstract class GlowTemplePiece extends GlowStructurePiece {

    public GlowTemplePiece(Random random, Location location, Vector size) {
        super(random, location, size);
    }

    protected void adjustHorizontalPosition(World world) {
        int sumY = 0, blockCount = 0;
        for (int x = boundingBox.getMin().getBlockX(); x <= boundingBox.getMax().getBlockX(); x++) {
            for (int z = boundingBox.getMin().getBlockZ(); z <= boundingBox.getMax().getBlockZ(); z++) {
                int y = world.getHighestBlockYAt(x, z);
                Material type = world.getBlockAt(x, y - 1, z).getType();
                while ((type == Material.LEAVES || type == Material.LEAVES_2 ||
                        type == Material.LOG || type == Material.LOG_2) && y > 1) {
                    y--;
                    type = world.getBlockAt(x, y - 1, z).getType();
                }
                sumY += Math.max(world.getSeaLevel(), y + 1);
                blockCount++;
            }
        }
        boundingBox.offset(new Vector(0, (sumY / blockCount) - boundingBox.getMin().getBlockY(), 0));
    }
}
