package net.glowstone;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class Explosion {
    private final float power;
    private final Entity source;
    private final Location location;
    private final boolean incendiary;
    private final boolean breakBlocks;
    private final GlowWorld world;
    private float yield = 0.3f;

    private static final Random random = new Random();
    private final ItemTable itemTable;

    /**
     * Creates a new explosion
     * @param source The entity causing this explosion
     * @param world The world this explosion is in
     * @param x
     * @param y
     * @param z
     * @param power The power of the explosion (TNT=4, bed=5, creeper=3, charged creeper=6, Ghast/Wither Skull=1, Either on creation=7, Ender Crystal=6)
     * @param incendiary true if this explosion should set blocks on fire
     * @param breakBlocks whether blocks should break through this explosion
     */
    public Explosion(Entity source, GlowWorld world, double x, double y, double z, float power, boolean incendiary, boolean breakBlocks) {
        this.source = source;
        this.location = new Location(world, x, y, z);
        this.power = power;
        this.incendiary = incendiary;
        this.breakBlocks = breakBlocks;
        this.world = world;
        itemTable = ItemTable.instance();
    }

    public boolean explodeWithEvent() {
        List<Block> droppedBlocks = calculateBlocks();

        EntityExplodeEvent event = EventFactory.onEntityExplode(source, location, droppedBlocks, yield);

        if (event.isCancelled()) return false;

        this.yield = event.getYield();

        playOutSoundAndParticles();

        for (Block block : droppedBlocks) {
            handleBlockExplosion((GlowBlock) block);
        }

        damageEntities();

        return true;
    }

    ///////////////////////////////////////////////////
    // Calculate all the dropping blocks

    private List<Block> calculateBlocks() {
        if (!breakBlocks || power < 0.1F)
            return new ArrayList<>();

        Set<WorldlessLocation> blocks = new HashSet<>(); //No duplicate blocks

        final int value = 8;

        for (int x = -value; x < value; x++) {
            for (int y = -value; y < value; y++) {
                for (int z = -value; z < value; z++) {
                    if (!(x == -value || x == value - 1 || y == -value || y == value - 1 || z == -value || z == value - 1)) {
                        continue;
                    }
                    calculateRay(x, y, z, blocks);
                }
            }
        }

        return toBlockList(blocks);
    }

    public void calculateRay(int x, int y, int z, Collection<WorldlessLocation> result) {
        Vector direction = new Vector(x, y, z);
        direction.normalize();
        direction.multiply(0.3f); //0.3 blocks away with each step

        Location current = location.clone();

        float currentPower = calculateStartPower();

        while (currentPower > 0) {
            GlowBlock block = (GlowBlock) world.getBlockAt(current);

            if (block.getType() != Material.AIR) {
                double sub = getBlastDurability(block);
                sub /= 5;
                sub += 0.3F;
                sub *= 0.3F;
                currentPower -= sub;

                result.add(new WorldlessLocation(block.getX(), block.getY(), block.getZ()));
            }

            current.add(direction);
            currentPower -= 0.225f;
        }
    }

    private void handleBlockExplosion(GlowBlock block) {
        if (block.getType() == Material.AIR) {
            return;
        } else if (block.getType() == Material.TNT) {
            //TODO
            return;
        }

        if (random.nextFloat() < yield)
            block.breakNaturally();
        else
            block.setType(Material.AIR);

        setBlockOnFire(block);

        playOutExplodeSmoke(block.getLocation());
    }

    private float calculateStartPower() {
        float rand = random.nextFloat();
        rand *= 0.6F; //(max - 0.7)
        rand += 0.7; //min
        return rand * power;
    }

    private double getBlastDurability(GlowBlock block) {
        return 2.5; //Dirt
    }

    private List<Block> toBlockList(Collection<WorldlessLocation> locs) {
        List<Block> blocks = new ArrayList<>(locs.size());
        for (WorldlessLocation location : locs)
            blocks.add(world.getBlockAt(location.x, location.y, location.z));
        return blocks;
    }


    private void setBlockOnFire(GlowBlock block) {
        if (!incendiary || block.getType() != Material.AIR)
            return;

        if (random.nextInt(3) != 0)
            return;

        Block below = block.getRelative(BlockFace.DOWN);
        //TODO check for flammable blocks

        BlockIgniteEvent event = EventFactory.onBlockIgnite(block, BlockIgniteEvent.IgniteCause.EXPLOSION, source);
        if (event.isCancelled())
            return;

        block.setType(Material.FIRE);
    }

    /////////////////////////////////////////
    // Damage entities
    private void damageEntities() {

    }

    ///////////////////////////////////////
    // Visualize
    private void playOutSoundAndParticles() {
        world.playSound(location, Sound.EXPLODE, 4, (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F);

        if (this.power >= 2.0F && this.breakBlocks) {
            //send huge explosion
        } else {
            //send large explosion
        }
    }

    private void playOutExplodeSmoke(Location location) {

    }


    private static final class WorldlessLocation {
        public int x, y, z;

        public WorldlessLocation(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null || !(other instanceof WorldlessLocation))
                return false;
            WorldlessLocation o = (WorldlessLocation) other;
            return x == o.x && y == o.y && z == o.z;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }

    }
}
