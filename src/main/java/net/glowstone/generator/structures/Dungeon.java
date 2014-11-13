package net.glowstone.generator.structures;

import java.util.Random;

import net.glowstone.GlowServer;
import net.glowstone.generator.objects.RandomItemsContent;
import net.glowstone.generator.objects.RandomItemsContent.WeightedItem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.material.Chest;

public class Dungeon {

    private final Random random;
    private final Location loc;
    private static final int HEIGHT = 4;
    private static final int MIN_RADIUS = 2;
    private final int radiusX;
    private final int radiusZ;
    private final RandomItemsContent chestContent;
    private final EntityType[] mobTypes = new EntityType[] {
        EntityType.SKELETON, EntityType.ZOMBIE,
        EntityType.ZOMBIE, EntityType.SPIDER
    };

    public Dungeon(Random random, Location location) {
        this.random = random;
        this.loc = location;
        // shape is 5x5, 5x7 or 7x7
        radiusX = random.nextInt(2) + MIN_RADIUS;
        radiusZ = random.nextInt(2) + MIN_RADIUS;

        chestContent = new RandomItemsContent(random);
        chestContent.addItem(new WeightedItem(Material.SADDLE, 1, 1), 10);
        chestContent.addItem(new WeightedItem(Material.IRON_INGOT, 1, 4), 10);
        chestContent.addItem(new WeightedItem(Material.BREAD, 1, 1), 10);
        chestContent.addItem(new WeightedItem(Material.WHEAT, 1, 4), 10);
        chestContent.addItem(new WeightedItem(Material.SULPHUR, 1, 4), 10);
        chestContent.addItem(new WeightedItem(Material.STRING, 1, 4), 10);
        chestContent.addItem(new WeightedItem(Material.BUCKET, 1, 1), 10);
        chestContent.addItem(new WeightedItem(Material.GOLDEN_APPLE, 1, 1), 1);
        chestContent.addItem(new WeightedItem(Material.REDSTONE, 1, 1), 10);
        chestContent.addItem(new WeightedItem(Material.GOLD_RECORD, 1, 1), 10);
        chestContent.addItem(new WeightedItem(Material.GREEN_RECORD, 1, 1), 10);
        chestContent.addItem(new WeightedItem(Material.NAME_TAG, 1, 1), 10);
        chestContent.addItem(new WeightedItem(Material.GOLD_BARDING, 1, 1), 2);
        chestContent.addItem(new WeightedItem(Material.IRON_BARDING, 1, 1), 5);
        chestContent.addItem(new WeightedItem(Material.DIAMOND_BARDING, 1, 1), 1);
    }

    public boolean canPlace() {
        int i = 0;
        for (int x = loc.getBlockX() - radiusX - 1; x <= loc.getBlockX() + radiusX + 1; x++) {
            for (int z = loc.getBlockZ() - radiusZ - 1; z <= loc.getBlockZ() + radiusZ + 1; z++) {
                for (int y = loc.getBlockY() - 1; y <= loc.getBlockY() + HEIGHT; y++) {
                    final Material type = loc.getWorld().getBlockAt(x, y, z).getType();
                    // checks we are between 2 solid material layers
                    if ((y == loc.getBlockY() - 1 || y == loc.getBlockY() + HEIGHT) && !type.isSolid()) {
                        return false;
                    }
                    // checks a few blocks at bottom of walls are opened to air
                    // in order to have a natural door like access
                    if ((x == loc.getBlockX() - radiusX - 1 || x == loc.getBlockX() + radiusX + 1
                            || z == loc.getBlockZ() - radiusZ - 1 || z == loc.getBlockZ() + radiusZ + 1)
                            && y == loc.getBlockY() && type == Material.AIR
                            && loc.getWorld().getBlockAt(x, y + 1, z).getType() == Material.AIR) {
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

        GlowServer.logger.info("dungeon generated: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
        return true;
    }

    public boolean generate() {

        if (!canPlace()) {
            return false;
        }

        for (int x = loc.getBlockX() - radiusX - 1; x <= loc.getBlockX() + radiusX + 1; x++) {
            for (int z = loc.getBlockZ() - radiusZ - 1; z <= loc.getBlockZ() + radiusZ + 1; z++) {
                for (int y = loc.getBlockY() + HEIGHT - 1; y >= loc.getBlockY() - 1; y--) {
                    final Block block = loc.getWorld().getBlockAt(x, y, z);
                    if (y > loc.getBlockY() - 1 && x != loc.getBlockX() - radiusX - 1 && z != loc.getBlockZ() - radiusZ - 1
                            && x != loc.getBlockX() + radiusX + 1 && z != loc.getBlockZ() + radiusZ + 1) {
                        // empty space inside
                        block.setType(Material.AIR);
                    } else if (y >= 0 && !loc.getWorld().getBlockAt(x, y - 1, z).getType().isSolid()) {
                        // cleaning walls from non solid materials (because of air gaps below)
                        block.setType(Material.AIR);
                    } else if (block.getType().isSolid()) {
                        // for walls we only replace solid material in order to
                        // preserve the air gaps
                        if (y == loc.getBlockY() - 1 && random.nextInt(4) != 0) {
                            block.setType(Material.MOSSY_COBBLESTONE);
                        } else {
                            block.setType(Material.COBBLESTONE);
                        }
                    }
                }
            }
        }

        // monster spawner placement
        final Block block = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        block.setType(Material.MOB_SPAWNER);
        final BlockState state = block.getState();
        if (state instanceof CreatureSpawner) {
            ((CreatureSpawner) state).setSpawnedType(mobTypes[random.nextInt(mobTypes.length)]);
        }

        // chest placement
        int chestCount = 0;
        for (int i = 0; i < 6 && chestCount < 2; i++) {
            int x = loc.getBlockX() + random.nextInt(radiusX * 2 + 1) - radiusX;
            int z = loc.getBlockZ() + random.nextInt(radiusZ * 2 + 1) - radiusZ;
            if (loc.getWorld().getBlockAt(x, loc.getBlockY(), z).getType() == Material.AIR) {
                BlockFace face = null;
                int solidBlocksCount = 0;
                if (loc.getWorld().getBlockAt(x - 1, loc.getBlockY(), z).getType().isSolid()) {
                    solidBlocksCount++;
                    face = BlockFace.EAST;
                }
                if (loc.getWorld().getBlockAt(x + 1, loc.getBlockY(), z).getType().isSolid()) {
                    solidBlocksCount++;
                    face = BlockFace.WEST;
                }
                if (loc.getWorld().getBlockAt(x, loc.getBlockY(), z - 1).getType().isSolid()) {
                    solidBlocksCount++;
                    face = BlockFace.SOUTH;
                }
                if (loc.getWorld().getBlockAt(x, loc.getBlockY(), z + 1).getType().isSolid()) {
                    solidBlocksCount++;
                    face = BlockFace.NORTH;
                }
                if (solidBlocksCount == 1) {
                    final BlockState chestState = loc.getWorld().getBlockAt(x, loc.getBlockY(), z).getState();

                    final Chest chest = new Chest(face);
                    chestState.setType(Material.CHEST);
                    chestState.setData(chest);
                    chestState.update(true);

                    chestContent.fillContainer(chest, chestState, 8);
                    chestCount++;
                }
            }
        }

        return true;
    }
}
