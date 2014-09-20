package net.glowstone;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockTNT;
import net.glowstone.entity.GlowEntity;
import net.glowstone.entity.GlowLivingEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.*;

public class Explosion {
    public static final int POWER_TNT = 4;
    public static final int POWER_BED = 5;
    public static final int POWER_CREEPER = 3;
    public static final int POWER_CHARGED_CREEPER = 6;
    public static final int POWER_GHAST = 1;
    public static final int POWER_WITHER_SKULL = 1;
    public static final int POWER_WITHER_CREATION = 7;
    public static final int POWER_ENDER_CRYSTAL = 6;

    private float power;
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
     * @param power The power of the explosion
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
        if (power < 0.1f)
            return true;

        List<Block> droppedBlocks = calculateBlocks();

        EntityExplodeEvent event = EventFactory.callEvent(new EntityExplodeEvent(source, location, droppedBlocks, yield));

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
        if (!breakBlocks)
            return new ArrayList<>();

        Set<BlockVector> blocks = new HashSet<>();

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

    private void calculateRay(int x, int y, int z, Collection<BlockVector> result) {
        Vector direction = new Vector(x, y, z);
        direction.normalize();
        direction.multiply(0.3f); //0.3 blocks away with each step

        Location current = location.clone();

        float currentPower = calculateStartPower();

        while (currentPower > 0) {
            GlowBlock block = world.getBlockAt(current);

            if (block.getType() != Material.AIR) {
                double sub = getBlastDurability(block);
                sub /= 5;
                sub += 0.3F;
                sub *= 0.3F;
                currentPower -= sub;

                if (currentPower > 0) {
                    result.add(new BlockVector(block.getX(), block.getY(), block.getZ()));
                }
            }

            current.add(direction);
            currentPower -= 0.225f;
        }
    }

    private void handleBlockExplosion(GlowBlock block) {
        if (block.getType() == Material.AIR) {
            return;
        } else if (block.getType() == Material.TNT) {
            ((BlockTNT) itemTable.getBlock(Material.TNT)).explodeTNTBlock(block);
            return;
        }

        block.breakNaturally(yield);

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

    private List<Block> toBlockList(Collection<BlockVector> locs) {
        List<Block> blocks = new ArrayList<>(locs.size());
        for (BlockVector location : locs)
            blocks.add(world.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        return blocks;
    }


    private void setBlockOnFire(GlowBlock block) {
        if (!incendiary || block.getType() != Material.AIR)
            return;

        if (random.nextInt(3) != 0)
            return;

        Block below = block.getRelative(BlockFace.DOWN);
        //TODO check for flammable blocks

        BlockIgniteEvent event = EventFactory.callEvent(new BlockIgniteEvent(block, BlockIgniteEvent.IgniteCause.EXPLOSION, source));
        if (event.isCancelled())
            return;

        block.setType(Material.FIRE);
    }

    /////////////////////////////////////////
    // Damage entities

    private void damageEntities() {
        float power = this.power;
        this.power *= 2f;

        Collection<GlowLivingEntity> entities = getNearbyEntities();
        for (GlowLivingEntity entity : entities) {
            Vector vecDistance = distanceToHead(entity);
            double distanceDivPower = vecDistance.length() / power;
            if (distanceDivPower == 0.0) continue;

            vecDistance.normalize();

            double basicDamage = calculateDamage(entity, distanceDivPower);
            double explosionDamage = (basicDamage * basicDamage + basicDamage) * 4 * power + 1.0D;
            entity.damage(explosionDamage);

            double enchantedDamage = calculateEnchantedDamage(basicDamage, entity);
            vecDistance.multiply(enchantedDamage);
            entity.setVelocity(vecDistance);
        }

        this.power = power;
    }

    private double calculateEnchantedDamage(double basicDamage, GlowLivingEntity entity) {
        int level = 0; //TODO calculate explosion protection level of entity's equipment

        if (level > 0) {
            float sub = level * 0.15f;
            double damage = basicDamage * sub;
            damage = Math.floor(damage);
            return basicDamage - damage;
        }

        return basicDamage;
    }

    private double calculateDamage(GlowEntity entity, double distanceDivPower) {
        double damage = world.rayTrace(location, entity);
        return damage * (1D - distanceDivPower);
    }

    private Collection<GlowLivingEntity> getNearbyEntities() {
        //TODO fetch only necessary entities
        List<LivingEntity> entities = world.getLivingEntities();
        List<GlowLivingEntity> nearbyEntities = new ArrayList<>();

        for (LivingEntity entity : entities) {
            if (distanceTo(entity) / (double) power < 1.) {
                nearbyEntities.add((GlowLivingEntity) entity);
            }
        }

        return nearbyEntities;
    }

    private double distanceTo(LivingEntity entity) {
        return location.clone().subtract(entity.getLocation()).length();
    }

    private Vector distanceToHead(LivingEntity entity) {
        return location.clone().subtract(entity.getLocation().clone().add(0, entity.getEyeHeight(), 0)).toVector();
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
        //TODO: play SMOKE and EXPLOSION particles
    }
}
