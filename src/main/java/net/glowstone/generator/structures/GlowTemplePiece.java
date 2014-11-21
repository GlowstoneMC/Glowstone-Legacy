package net.glowstone.generator.structures;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public abstract class GlowTemplePiece extends GlowStructurePiece {

    private int width;
    private int height;
    private int depth;
    private int hPos = -1;

    public GlowTemplePiece() {
        super();
    }

    public GlowTemplePiece(Random random, Location location, Vector size) {
        super(random, location, size);
        width = size.getBlockX();
        height = size.getBlockY();
        depth = size.getBlockZ();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public void setHPos(int hPos) {
        this.hPos = hPos;
    }

    public int getHPos() {
        return hPos;
    }

    protected void adjustHorizontalPosition(World world) {
        if (hPos >= 0) {
            return;
        }

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
        hPos = sumY / blockCount;
        boundingBox.offset(new Vector(0, hPos - boundingBox.getMin().getBlockY(), 0));
    }
}
