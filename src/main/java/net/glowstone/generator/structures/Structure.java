package net.glowstone.generator.structures;

import java.util.Random;

import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class Structure {

    protected final Random random;
    protected final Location location;
    protected final BlockStateDelegate delegate;
    private final BlockFace facing;
    private Cuboid cuboid;

    public Structure(Random random, Location location, BlockStateDelegate delegate) {
        this.random = random;
        this.location = location;
        this.delegate = delegate;
        facing = getFacingFromOrdinal(random.nextInt(4));
    }

    protected final void setSize(Vector size) {
        switch (facing) {
            case EAST:
            case WEST:
                size = new Vector(size.getBlockZ(), size.getBlockY(), size.getBlockX());
                break;
            default:
                break;
        }
        cuboid = new Cuboid(
                new Vector(location.getBlockX(),
                           location.getBlockY(),
                           location.getBlockZ()),
                new Vector(location.getBlockX() + size.getBlockX() - 1,
                           location.getBlockY() + size.getBlockY() - 1,
                           location.getBlockZ() + size.getBlockZ() - 1));
    }

    protected final void setBlock(Vector pos, Material type) {
        setBlock(pos, type, 0);
    }

    protected final void setBlock(Vector pos, Material type, int data) {
        final Vector vec = translate(pos);
        delegate.setTypeAndRawData(location.getWorld(), vec.getBlockX(), vec.getBlockY(), vec.getBlockZ(), type, data);
    }

    protected final void setBlock(Vector pos, Material type, MaterialData data) {
        final Vector vec = translate(pos);
        delegate.setTypeAndData(location.getWorld(), vec.getBlockX(), vec.getBlockY(), vec.getBlockZ(), type, data);
    }

    protected final void fill(Vector min, Vector max, Material type) {
        fill(min, max, type, 0);
    }

    protected final void fill(Vector min, Vector max, Material type, int data) {
        fill(min, max, type, data, type, data);
    }

    protected final void fill(Vector min, Vector max, Material type, MaterialData data) {
        fill(min, max, type, data, type, data);
    }

    protected final void fill(Vector min, Vector max, Material outerType, Material innerType) {
        fill(min, max, outerType, 0, innerType, 0);
    }

    protected final void fill(Vector min, Vector max, Material outerType, Material innerType, int innerData) {
        fill(min, max, outerType, 0, innerType, innerData);
    }

    protected final void fill(Vector min, Vector max, Material outerType, Material innerType, MaterialData innerData) {
        fill(min, max, outerType, 0, innerType, innerData);
    }

    protected final void fill(Vector min, Vector max, Material outerType, int outerData, Material innerType, MaterialData innerData) {
        fill(min, max, outerType, outerData, innerType, innerData);
    }

    protected final void fill(Vector min, Vector max, Material outerType, int outerData, Material innerType) {
        fill(min, max, outerType, outerData, innerType, 0);
    }

    protected final void fill(Vector min, Vector max, Material outerType, MaterialData outerData, Material innerType) {
        fill(min, max, outerType, outerData, innerType, 0);
    }

    protected final void fill(Vector min, Vector max, Material outerType, MaterialData outerData, Material innerType, int innerData) {
        fill(min, max, outerType, outerData, innerType, innerData);
    }

    protected final void fill(Vector min, Vector max, Material outerType, int outerData, Material innerType, int innerData) {
        for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
            for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    final Material type;
                    final int data;
                    if (x != min.getBlockX() && x != max.getBlockX() &&
                            z != min.getBlockZ() && z != max.getBlockZ() &&
                            y != min.getBlockY() && y != max.getBlockY()) {
                        type = innerType;
                        data = innerData;
                    } else {
                        type = outerType;
                        data = outerData;
                    }
                    setBlock(new Vector(x, y, z), type, data);
                }
            }
        }
    }

    protected final void fill(Vector min, Vector max, Material outerType, MaterialData outerData, Material innerType, MaterialData innerData) {
        for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
            for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    final Material type;
                    final MaterialData data;
                    if (x != min.getBlockX() && x != max.getBlockX() &&
                            z != min.getBlockZ() && z != max.getBlockZ() &&
                            y != min.getBlockY() && y != max.getBlockY()) {
                        type = innerType;
                        data = innerData;
                    } else {
                        type = outerType;
                        data = outerData;
                    }
                    setBlock(new Vector(x, y, z), type, data);
                }
            }
        }
    }

    protected final BlockFace getFacingFromOrdinal(int n) {
        switch (n) {
            case 1:
                return BlockFace.EAST;
            case 2:
                return BlockFace.SOUTH;
            case 3:
                return BlockFace.WEST;
            default:
                return BlockFace.NORTH;
        }
    }

    protected final BlockFace getRelativeFacing(BlockFace face) {
        final BlockFace f = getFacingFromOrdinal((facing.ordinal() + face.ordinal()) & 0x3);
        if ((facing == BlockFace.SOUTH || facing == BlockFace.WEST) &&
                (face == BlockFace.EAST || face == BlockFace.WEST)) {
            return f.getOppositeFace();
        }
        return f;
    }

    protected final Vector translate(Vector pos) {
        switch (facing) {
            case EAST:
                return new Vector(cuboid.getMax().getBlockX() - pos.getBlockZ(),
                        cuboid.getMin().getBlockY() + pos.getBlockY(),
                        cuboid.getMin().getBlockZ() + pos.getBlockX());
            case SOUTH:
                return new Vector(cuboid.getMin().getBlockX() + pos.getBlockX(),
                        cuboid.getMin().getBlockY() + pos.getBlockY(),
                        cuboid.getMax().getBlockZ() - pos.getBlockZ());
            case WEST:
                return new Vector(cuboid.getMin().getBlockX() + pos.getBlockZ(),
                        cuboid.getMin().getBlockY() + pos.getBlockY(),
                        cuboid.getMin().getBlockZ() + pos.getBlockX());
            default: // NORTH
                return new Vector(cuboid.getMin().getBlockX() + pos.getBlockX(),
                        cuboid.getMin().getBlockY() + pos.getBlockY(),
                        cuboid.getMin().getBlockZ() + pos.getBlockZ());
        }
    }

    public boolean generate() {
        return false;
    }

    public class Cuboid {

        private final Vector min;
        private final Vector max;

        public Cuboid(Vector min, Vector max) {
            this.min = min;
            this.max = max;
        }

        public Vector getMin() {
            return min;
        }

        public Vector getMax() {
            return max;
        }

        public void offset(Vector offset) {
            min.add(offset);
            max.add(offset);
        }
    }
}
