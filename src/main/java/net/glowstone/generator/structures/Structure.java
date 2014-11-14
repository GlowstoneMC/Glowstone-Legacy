package net.glowstone.generator.structures;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.glowstone.generator.objects.RandomItemsContent;
import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.DirectionalContainer;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class Structure {

    protected final Location location;
    protected final BlockStateDelegate delegate;
    private BlockFace facing;
    protected Cuboid cuboid;

    public Structure(Location location, BlockStateDelegate delegate) {
        this.location = location;
        this.delegate = delegate;
        facing = BlockFace.NORTH;
    }

    public Structure(Random random, Location location, BlockStateDelegate delegate) {
        this(location, delegate);
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

    protected final void addRandomMaterial(Map<RandomMaterial, Integer> materials, int weigth, Material type, int data) {
        materials.put(new RandomMaterial(type,  data), weigth);
    }

    protected final RandomMaterial getRandomMaterial(Random random, Map<RandomMaterial, Integer> materials) {
        int totalWeight = 0;
        for (int weigth : materials.values()) {
            totalWeight += weigth;
        }
        int weight = random.nextInt(totalWeight);
        for (Entry<RandomMaterial, Integer> entry : materials.entrySet()) {
            weight -= entry.getValue();
            if (weight < 0) {
                return entry.getKey();
            }
        }
        return new RandomMaterial(Material.AIR);
    }

    protected final void setBlock(Vector pos, Material type) {
        setBlock(pos, type, 0);
    }

    protected final void setBlock(Vector pos, Material type, int data) {
        final Vector vec = translate(pos);
        if (cuboid.isVectorInside(vec)) {
            delegate.setTypeAndRawData(location.getWorld(), vec.getBlockX(), vec.getBlockY(), vec.getBlockZ(), type, data);
        }
    }

    protected final void setBlock(Vector pos, Material type, MaterialData data) {
        final Vector vec = translate(pos);
        if (cuboid.isVectorInside(vec)) {
            delegate.setTypeAndData(location.getWorld(), vec.getBlockX(), vec.getBlockY(), vec.getBlockZ(), type, data);
        }
    }

    protected final void setBlockDownward(Vector pos, Material type) {
        setBlockDownward(pos, type, 0);
    }

    protected final void setBlockDownward(Vector pos, Material type, int data) {
        final Vector vec = translate(pos);
        int y = vec.getBlockY();
        while (!location.getWorld().getBlockAt(vec.getBlockX(), y, vec.getBlockZ()).getType().isSolid() && y > 1) {
            delegate.setTypeAndRawData(location.getWorld(), vec.getBlockX(), y, vec.getBlockZ(), type, data);
            y--;
        }
    }

    protected final void setBlockDownward(Vector pos, Material type, MaterialData data) {
        final Vector vec = translate(pos);
        int y = vec.getBlockY();
        while (!location.getWorld().getBlockAt(vec.getBlockX(), y, vec.getBlockZ()).getType().isSolid() && y > 1) {
            delegate.setTypeAndData(location.getWorld(), vec.getBlockX(), y, vec.getBlockZ(), type, data);
            y--;
        }
    }

    protected final void setBlockWithRandomMaterial(Vector pos, Random random, Map<RandomMaterial, Integer> materials) {
        final RandomMaterial material = getRandomMaterial(random, materials);
        setBlock(pos, material.getType(), material.getData());
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

    protected final void fillWithRandomMaterial(Vector min, Vector max, Random random, Map<RandomMaterial, Integer> materials) {
        for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
            for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    final RandomMaterial material = getRandomMaterial(random, materials);
                    setBlock(new Vector(x, y, z), material.getType(), material.getData());
                }
            }
        }
    }

    protected final void createRandomItemsContainer(Vector pos, RandomItemsContent content, DirectionalContainer container, int maxStacks) {
        final Vector vec = translate(pos);
        if (cuboid.isVectorInside(vec)) {
            final BlockState state = location.getWorld().getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getState();
            delegate.backupBlockState(state.getBlock());

            state.setType(container.getItemType());
            state.setData(container);
            state.update(true);

            content.fillContainer(container, state, maxStacks);
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

        private Vector min;
        private Vector max;

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

        public boolean isVectorInside(Vector vec) {
            if (vec.getBlockX() >= min.getBlockX() && vec.getBlockX() <= max.getBlockX() &&
                    vec.getBlockY() >= min.getBlockY() && vec.getBlockY() <= max.getBlockY() &&
                    vec.getBlockZ() >= min.getBlockZ() && vec.getBlockZ() <= max.getBlockZ()) {
                return true;
            }
            return false;
        }

        public void offset(Vector offset) {
            min = min.add(offset);
            max = max.add(offset);
        }
    }

    public class RandomMaterial {

        private Material type;
        private int data;

        public RandomMaterial(Material type) {
            this(type, 0);
        }

        public RandomMaterial(Material type, int data) {
            this.type = type;
            this.data = data;
        }

        public Material getType() {
            return type;
        }

        public int getData() {
            return data;
        }
    }
}
