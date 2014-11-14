package net.glowstone.generator.structures;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.glowstone.GlowServer;
import net.glowstone.generator.objects.RandomItemsContent;
import net.glowstone.generator.objects.RandomItemsContent.RandomAmountItem;
import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.material.Chest;
import org.bukkit.util.Vector;

public class Dungeon extends Structure {

    private final Map<RandomMaterial, Integer> stones = new HashMap<RandomMaterial, Integer>();
    private final Random random;
    private static final int HEIGHT = 5;
    private static final int MIN_RADIUS = 3;
    private final int radiusX;
    private final int radiusZ;
    private final RandomItemsContent chestContent;
    private final EntityType[] mobTypes = new EntityType[] {
        EntityType.SKELETON, EntityType.ZOMBIE,
        EntityType.ZOMBIE, EntityType.SPIDER
    };

    public Dungeon(Random random, Location location, BlockStateDelegate delegate) {
        super(location, delegate);
        this.random = random;
        // inner dungeon shape is 5x5, 5x7 or 7x7
        radiusX = random.nextInt(2) + MIN_RADIUS;
        radiusZ = random.nextInt(2) + MIN_RADIUS;
        setSize(new Vector(radiusX * 2 + 1, HEIGHT, radiusZ * 2 + 1));
        addRandomMaterial(stones, 1, Material.COBBLESTONE, 0);
        addRandomMaterial(stones, 3, Material.MOSSY_COBBLESTONE, 0);

        chestContent = new RandomItemsContent(random);
        chestContent.addItem(new RandomAmountItem(Material.SADDLE, 1, 1), 10);
        chestContent.addItem(new RandomAmountItem(Material.IRON_INGOT, 1, 4), 10);
        chestContent.addItem(new RandomAmountItem(Material.BREAD, 1, 1), 10);
        chestContent.addItem(new RandomAmountItem(Material.WHEAT, 1, 4), 10);
        chestContent.addItem(new RandomAmountItem(Material.SULPHUR, 1, 4), 10);
        chestContent.addItem(new RandomAmountItem(Material.STRING, 1, 4), 10);
        chestContent.addItem(new RandomAmountItem(Material.BUCKET, 1, 1), 10);
        chestContent.addItem(new RandomAmountItem(Material.GOLDEN_APPLE, 1, 1), 1);
        chestContent.addItem(new RandomAmountItem(Material.REDSTONE, 1, 1), 10);
        chestContent.addItem(new RandomAmountItem(Material.GOLD_RECORD, 1, 1), 10);
        chestContent.addItem(new RandomAmountItem(Material.GREEN_RECORD, 1, 1), 10);
        chestContent.addItem(new RandomAmountItem(Material.NAME_TAG, 1, 1), 10);
        chestContent.addItem(new RandomAmountItem(Material.GOLD_BARDING, 1, 1), 2);
        chestContent.addItem(new RandomAmountItem(Material.IRON_BARDING, 1, 1), 5);
        chestContent.addItem(new RandomAmountItem(Material.DIAMOND_BARDING, 1, 1), 1);
    }

    public boolean canPlace() {
        if (cuboid.getMin().getBlockY() < 1) {
            return false;
        }

        int i = 0;
        for (int x = 0; x <= radiusX * 2; x++) {
            for (int z = 0; z <= radiusZ * 2; z++) {
                for (int y = 0; y <= HEIGHT - 1; y++) {
                    final Material type = getBlockState(new Vector(x, y, z)).getType();
                    // checks we are between 2 solid material layers
                    if ((y == 0 || y == HEIGHT - 1) && !type.isSolid()) {
                        return false;
                    }
                    // checks a few blocks at bottom of walls are opened to air
                    // in order to have a natural door like access
                    if ((x == 0 || x == radiusX * 2 || z == 0 || z == radiusZ * 2)
                            && y == 1 && type == Material.AIR
                            && getBlockState(new Vector(x, y + 1, z)).getType() == Material.AIR) {
                        i++;
                        // TODO
                        // change min to 1 when caves will be generated !
                        // this will be required so that dungeons are minimally
                        // exposed to air
                        if (i < 0 || i > 5) {
                            return false;
                        }
                    }
                }
            }
        }

        GlowServer.logger.info("dungeon generated: " + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ());
        return true;
    }

    public boolean generate() {

        cuboid.offset(new Vector(-radiusX, -1, -radiusZ));

        if (!canPlace()) {
            return false;
        }

        for (int x = 0; x <= radiusX * 2; x++) {
            for (int z = 0; z <= radiusZ * 2; z++) {
                for (int y = HEIGHT - 2; y >= 0; y--) {
                    final BlockState state = getBlockState(new Vector(x, y, z));
                    if (y > 0 && x > 0 && z > 0  && x < radiusX * 2 && z < radiusZ * 2) {
                        // empty space inside
                        setBlock(new Vector(x, y, z), Material.AIR);
                    } else if (!getBlockState(new Vector(x, y - 1, z)).getType().isSolid()) {
                        // cleaning walls from non solid materials (because of air gaps below)
                        setBlock(new Vector(x, y, z), Material.AIR);
                    } else if (state.getType().isSolid()) {
                        // for walls we only replace solid material in order to
                        // preserve the air gaps
                        if (y == 0) {
                            setBlockWithRandomMaterial(new Vector(x, y, z), random, stones);
                        } else {
                            setBlock(new Vector(x, y, z), Material.COBBLESTONE);
                        }
                    }
                }
            }
        }

        createMobSpawner(new Vector(radiusX, 1, radiusZ), mobTypes[random.nextInt(mobTypes.length)]);

        int chestCount = 0;
        for (int i = 0; i < 6 && chestCount < 2; i++) {
            int x = random.nextInt(radiusX * 2 - 1) + 1;
            int z = random.nextInt(radiusZ * 2 - 1) + 1;
            if (getBlockState(new Vector(x, 1, z)).getType() == Material.AIR) {
                BlockFace face = null;
                int solidBlocksCount = 0;
                if (getBlockState(new Vector(x - 1, 1, z)).getType().isSolid()) {
                    solidBlocksCount++;
                    face = BlockFace.EAST;
                }
                if (getBlockState(new Vector(x + 1, 1, z)).getType().isSolid()) {
                    solidBlocksCount++;
                    face = BlockFace.WEST;
                }
                if (getBlockState(new Vector(x, 1, z - 1)).getType().isSolid()) {
                    solidBlocksCount++;
                    face = BlockFace.SOUTH;
                }
                if (getBlockState(new Vector(x, 1, z + 1)).getType().isSolid()) {
                    solidBlocksCount++;
                    face = BlockFace.NORTH;
                }
                if (solidBlocksCount == 1) {
                    createRandomItemsContainer(new Vector(x, 1, z), chestContent, new Chest(face), 8);
                    chestCount++;
                }
            }
        }

        return true;
    }
}
