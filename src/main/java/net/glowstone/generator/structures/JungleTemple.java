package net.glowstone.generator.structures;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Diode;
import org.bukkit.material.Dispenser;
import org.bukkit.material.Lever;
import org.bukkit.material.PistonBaseMaterial;
import org.bukkit.material.Stairs;
import org.bukkit.material.TripwireHook;
import org.bukkit.material.Vine;
import org.bukkit.util.Vector;

public class JungleTemple extends Structure {

    private final Map<RandomMaterial, Integer> stones = new HashMap<RandomMaterial, Integer>();
    private final Random random;

    public JungleTemple(Random random, Location location, BlockStateDelegate delegate) {
        super(random, location, delegate);
        this.random = random;
        setSize(new Vector(12, 14, 15));
        addRandomMaterial(stones, 4, Material.COBBLESTONE, 0);
        addRandomMaterial(stones, 6, Material.MOSSY_COBBLESTONE, 0);
    }

    @Override
    public boolean generate() {

        int sumY = 0, blockCount = 0;
        for (int x = cuboid.getMin().getBlockX(); x <= cuboid.getMax().getBlockX(); x++) {
            for (int z = cuboid.getMin().getBlockZ(); z <= cuboid.getMax().getBlockZ(); z++) {
                sumY += Math.max(64, location.getWorld().getHighestBlockYAt(x, z));
                blockCount++;
            }
        }
        cuboid.offset(new Vector(0, (sumY / blockCount) - cuboid.getMin().getBlockY(), 0));
        cuboid.offset(new Vector(0, -4, 0));

        // 1st floor
        fillWithRandomMaterial(new Vector(0, 0, 0), new Vector(11, 0, 14), random, stones);
        fillWithRandomMaterial(new Vector(0, 1, 0), new Vector(11, 3, 0), random, stones);
        fillWithRandomMaterial(new Vector(11, 1, 1), new Vector(11, 3, 13), random, stones);
        fillWithRandomMaterial(new Vector(0, 1, 1), new Vector(0, 3, 13), random, stones);
        fillWithRandomMaterial(new Vector(0, 1, 14), new Vector(11, 3, 14), random, stones);
        fillWithRandomMaterial(new Vector(0, 4, 0), new Vector(11, 4, 14), random, stones);
        final Stairs entranceStairs = new Stairs(Material.COBBLESTONE_STAIRS);
        entranceStairs.setFacingDirection(getRelativeFacing(BlockFace.SOUTH));
        fill(new Vector(4, 4, 0), new Vector(7, 4, 0), entranceStairs.getItemType(), entranceStairs);
        fill(new Vector(1, 1, 1), new Vector(10, 3, 13), Material.AIR);
        fill(new Vector(5, 4, 7), new Vector(6, 4, 9), Material.AIR);

        // 2nd floor
        fillWithRandomMaterial(new Vector(2, 5, 2), new Vector(9, 6, 2), random, stones);
        fillWithRandomMaterial(new Vector(9, 5, 3), new Vector(9, 6, 11), random, stones);
        fillWithRandomMaterial(new Vector(2, 5, 12), new Vector(9, 6, 12), random, stones);
        fillWithRandomMaterial(new Vector(2, 5, 3), new Vector(2, 6, 11), random, stones);
        fillWithRandomMaterial(new Vector(1, 7, 1), new Vector(10, 7, 13), random, stones);
        fill(new Vector(3, 5, 3), new Vector(8, 6, 11), Material.AIR);
        fill(new Vector(4, 7, 6), new Vector(7, 7, 9), Material.AIR);
        fill(new Vector(5, 5, 2), new Vector(6, 6, 2), Material.AIR);
        fill(new Vector(5, 6, 12), new Vector(6, 6, 12), Material.AIR);

        // 3rd floor
        fillWithRandomMaterial(new Vector(1, 8, 1), new Vector(10, 9, 1), random, stones);
        fillWithRandomMaterial(new Vector(10, 8, 2), new Vector(10, 9, 12), random, stones);
        fillWithRandomMaterial(new Vector(1, 8, 13), new Vector(10, 9, 13), random, stones);
        fillWithRandomMaterial(new Vector(1, 8, 2), new Vector(1, 9, 12), random, stones);
        fill(new Vector(2, 8, 2), new Vector(9, 9, 12), Material.AIR);
        fill(new Vector(5, 9, 1), new Vector(6, 9, 1), Material.AIR);
        fill(new Vector(5, 9, 13), new Vector(6, 9, 13), Material.AIR);
        setBlock(new Vector(10, 9, 5), Material.AIR);
        setBlock(new Vector(10, 9, 9), Material.AIR);
        setBlock(new Vector(1, 9, 5), Material.AIR);
        setBlock(new Vector(1, 9, 9), Material.AIR);

        // roof
        fillWithRandomMaterial(new Vector(1, 10, 1), new Vector(10, 10, 4), random, stones);
        fillWithRandomMaterial(new Vector(8, 10, 5), new Vector(10, 10, 9), random, stones);
        fillWithRandomMaterial(new Vector(1, 10, 5), new Vector(3, 10, 9), random, stones);
        fillWithRandomMaterial(new Vector(1, 10, 10), new Vector(10, 10, 13), random, stones);
        fillWithRandomMaterial(new Vector(3, 11, 3), new Vector(8, 11, 5), random, stones);
        fillWithRandomMaterial(new Vector(7, 11, 6), new Vector(8, 11, 8), random, stones);
        fillWithRandomMaterial(new Vector(3, 11, 6), new Vector(4, 11, 8), random, stones);
        fillWithRandomMaterial(new Vector(3, 11, 9), new Vector(8, 11, 11), random, stones);
        fillWithRandomMaterial(new Vector(4, 12, 4), new Vector(7, 12, 10), random, stones);
        fill(new Vector(4, 10, 5), new Vector(7, 10, 9), Material.AIR);
        fill(new Vector(5, 11, 6), new Vector(6, 11, 8), Material.AIR);

        // outside walls decorations
        fillWithRandomMaterial(new Vector(2, 8, 0), new Vector(2, 9, 0), random, stones);
        fillWithRandomMaterial(new Vector(4, 8, 0), new Vector(4, 9, 0), random, stones);
        fillWithRandomMaterial(new Vector(7, 8, 0), new Vector(7, 9, 0), random, stones);
        fillWithRandomMaterial(new Vector(9, 8, 0), new Vector(9, 9, 0), random, stones);
        fillWithRandomMaterial(new Vector(5, 10, 0), new Vector(6, 10, 0), random, stones);
        for (int i = 0; i < 6; i++) {
            fillWithRandomMaterial(new Vector(11, 8, 2 + i * 2), new Vector(11, 9, 2 + i * 2), random, stones);
            fillWithRandomMaterial(new Vector(0, 8, 2 + i * 2), new Vector(0, 9, 2 + i * 2), random, stones);
        }
        setBlockWithRandomMaterial(new Vector(11, 10, 5), random, stones);
        setBlockWithRandomMaterial(new Vector(11, 10, 9), random, stones);
        setBlockWithRandomMaterial(new Vector(0, 10, 5), random, stones);
        setBlockWithRandomMaterial(new Vector(0, 10, 9), random, stones);
        fillWithRandomMaterial(new Vector(2, 8, 14), new Vector(2, 9, 14), random, stones);
        fillWithRandomMaterial(new Vector(4, 8, 14), new Vector(4, 9, 14), random, stones);
        fillWithRandomMaterial(new Vector(7, 8, 14), new Vector(7, 9, 14), random, stones);
        fillWithRandomMaterial(new Vector(9, 8, 14), new Vector(9, 9, 14), random, stones);

        // roof decorations
        fillWithRandomMaterial(new Vector(2, 11, 2), new Vector(2, 13, 2), random, stones);
        fillWithRandomMaterial(new Vector(9, 11, 2), new Vector(9, 13, 2), random, stones);
        fillWithRandomMaterial(new Vector(9, 11, 12), new Vector(9, 13, 12), random, stones);
        fillWithRandomMaterial(new Vector(2, 11, 12), new Vector(2, 13, 12), random, stones);
        setBlockWithRandomMaterial(new Vector(4, 13, 4), random, stones);
        setBlockWithRandomMaterial(new Vector(7, 13, 4), random, stones);
        setBlockWithRandomMaterial(new Vector(7, 13, 10), random, stones);
        setBlockWithRandomMaterial(new Vector(4, 13, 10), random, stones);
        final Stairs roofStairsN = new Stairs(Material.COBBLESTONE_STAIRS);
        roofStairsN.setFacingDirection(getRelativeFacing(BlockFace.SOUTH));
        fill(new Vector(5, 13, 6), new Vector(6, 13, 6), roofStairsN.getItemType(), roofStairsN);
        fillWithRandomMaterial(new Vector(5, 13, 7), new Vector(6, 13, 7), random, stones);
        final Stairs roofStairsS = new Stairs(Material.COBBLESTONE_STAIRS);
        roofStairsS.setFacingDirection(getRelativeFacing(BlockFace.NORTH));
        fill(new Vector(5, 13, 8), new Vector(6, 13, 8), roofStairsS.getItemType(), roofStairsS);

        // 1st floor inside
        for (int i = 0; i < 6; i++) {
            fillWithRandomMaterial(new Vector(1, 3, 2 + i * 2), new Vector(3, 3, 2 + i * 2), random, stones);
        }
        for (int i = 0; i < 7; i++) {
            fillWithRandomMaterial(new Vector(1, 1, 1 + i * 2), new Vector(1, 2, 1 + i * 2), random, stones);
        }
        setBlockWithRandomMaterial(new Vector(2, 2, 1), random, stones);
        setBlock(new Vector(3, 1, 1), Material.MOSSY_COBBLESTONE);
        fillWithRandomMaterial(new Vector(4, 2, 1), new Vector(5, 2, 1), random, stones);
        setBlockWithRandomMaterial(new Vector(6, 1, 1), random, stones);
        setBlockWithRandomMaterial(new Vector(6, 3, 1), random, stones);
        fillWithRandomMaterial(new Vector(7, 2, 1), new Vector(9, 2, 1), random, stones);
        setBlock(new Vector(8, 1, 1), Material.MOSSY_COBBLESTONE);
        fillWithRandomMaterial(new Vector(10, 1, 1), new Vector(10, 3, 7), random, stones);
        fillWithRandomMaterial(new Vector(9, 3, 1), new Vector(9, 3, 7), random, stones);
        setBlock(new Vector(9, 1, 2), Material.MOSSY_COBBLESTONE);
        setBlock(new Vector(9, 1, 4), Material.MOSSY_COBBLESTONE);
        setBlock(new Vector(8, 1, 5), Material.MOSSY_COBBLESTONE);
        fill(new Vector(7, 2, 5), new Vector(7, 3, 5), Material.MOSSY_COBBLESTONE);
        setBlock(new Vector(6, 1, 5), Material.MOSSY_COBBLESTONE);
        setBlockWithRandomMaterial(new Vector(6, 2, 5), random, stones);
        fill(new Vector(5, 2, 5), new Vector(5, 3, 5), Material.MOSSY_COBBLESTONE);
        setBlock(new Vector(4, 1, 5), Material.MOSSY_COBBLESTONE);
        fillWithRandomMaterial(new Vector(7, 1, 6), new Vector(7, 3, 11), random, stones);
        fillWithRandomMaterial(new Vector(4, 1, 6), new Vector(4, 3, 11), random, stones);
        fillWithRandomMaterial(new Vector(5, 3, 11), new Vector(6, 3, 11), random, stones);
        fillWithRandomMaterial(new Vector(8, 3, 11), new Vector(10, 3, 11), random, stones);
        fillWithRandomMaterial(new Vector(8, 1, 11), new Vector(10, 1, 11), random, stones);
        fillWithRandomMaterial(new Vector(5, 1, 8), new Vector(6, 1, 8), random, stones);
        fillWithRandomMaterial(new Vector(6, 1, 7), new Vector(6, 2, 7), random, stones);
        setBlockWithRandomMaterial(new Vector(5, 2, 7), random, stones);
        fillWithRandomMaterial(new Vector(6, 1, 6), new Vector(6, 3, 6), random, stones);
        fillWithRandomMaterial(new Vector(5, 2, 6), new Vector(5, 3, 6), random, stones);
        fillWithRandomMaterial(new Vector(8, 2, 6), new Vector(9, 2, 6), random, stones);
        setBlockWithRandomMaterial(new Vector(8, 3, 6), random, stones);
        fillWithRandomMaterial(new Vector(9, 1, 7), new Vector(9, 2, 7), random, stones);
        fillWithRandomMaterial(new Vector(8, 1, 7), new Vector(8, 3, 7), random, stones);       
        fillWithRandomMaterial(new Vector(10, 1, 8), new Vector(10, 1, 10), random, stones);
        setBlock(new Vector(10, 2, 9), Material.MOSSY_COBBLESTONE);
        fillWithRandomMaterial(new Vector(8, 1, 8), new Vector(8, 1, 10), random, stones);
        fill(new Vector(8, 2, 11), new Vector(10, 2, 11), Material.SMOOTH_BRICK, 3);
        final Lever lever = new Lever(Material.LEVER, (byte) 4); // workaround for bukkit, can't set an attached BlockFace
        lever.setFacingDirection(getRelativeFacing(BlockFace.SOUTH));
        fill(new Vector(8, 2, 12), new Vector(10, 2, 12), lever.getItemType(), lever);
        setBlock(new Vector(3, 2, 1), Material.DISPENSER, new Dispenser(getRelativeFacing(BlockFace.SOUTH)));
        setBlock(new Vector(9, 2, 3), Material.DISPENSER, new Dispenser(getRelativeFacing(BlockFace.WEST)));
        final Vine vine = new Vine(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
        setBlock(new Vector(3, 2, 2), vine.getItemType(), vine);
        fill(new Vector(8, 2, 3), new Vector(8, 3, 3), vine.getItemType(), vine);        
        fill(new Vector(2, 1, 8), new Vector(3, 1, 8), Material.TRIPWIRE);
        final TripwireHook hookE = new TripwireHook(getRelativeFacing(BlockFace.WEST));
        hookE.setConnected(true);
        setBlock(new Vector(4, 1, 8), hookE.getItemType(), hookE);
        final TripwireHook hookW = new TripwireHook(getRelativeFacing(BlockFace.EAST));
        hookW.setConnected(true);
        setBlock(new Vector(1, 1, 8), hookW.getItemType(), hookW);
        fill(new Vector(5, 1, 1), new Vector(5, 1, 7), Material.REDSTONE_WIRE);
        setBlock(new Vector(4, 1, 1), Material.REDSTONE_WIRE);
        fill(new Vector(7, 1, 2), new Vector(7, 1, 4), Material.TRIPWIRE);
        final TripwireHook hookN = new TripwireHook(getRelativeFacing(BlockFace.SOUTH));
        hookN.setConnected(true);
        setBlock(new Vector(7, 1, 1), hookN.getItemType(), hookN);
        final TripwireHook hookS = new TripwireHook(getRelativeFacing(BlockFace.NORTH));
        hookS.setConnected(true);
        setBlock(new Vector(7, 1, 5), hookS.getItemType(), hookS);
        fill(new Vector(8, 1, 6), new Vector(9, 1, 6), Material.REDSTONE_WIRE);
        setBlock(new Vector(9, 1, 5), Material.REDSTONE_WIRE);
        setBlock(new Vector(9, 2, 4), Material.REDSTONE_WIRE);
        final PistonBaseMaterial pistonE = new PistonBaseMaterial(Material.PISTON_STICKY_BASE);
        pistonE.setFacingDirection(getRelativeFacing(BlockFace.WEST));
        fill(new Vector(10, 2, 8), new Vector(10, 3, 8), pistonE.getItemType(), pistonE);
        final PistonBaseMaterial pistonUp = new PistonBaseMaterial(Material.PISTON_STICKY_BASE);
        pistonUp.setFacingDirection(BlockFace.UP);
        setBlock(new Vector(9, 2, 8), pistonUp.getItemType(), pistonUp);
        setBlock(new Vector(10, 3, 9), Material.REDSTONE_WIRE);
        fill(new Vector(8, 2, 9), new Vector(8, 2, 10), Material.REDSTONE_WIRE);
        final Diode repeater = new Diode(Material.DIODE_BLOCK_OFF);
        repeater.setDelay(1);
        repeater.setFacingDirection(getRelativeFacing(BlockFace.SOUTH));
        setBlock(new Vector(10, 2, 10), repeater.getItemType(), repeater);

        // 2nd floor inside
        for (int i = 0; i < 4; i++) {
            final Stairs stairsS = new Stairs(Material.COBBLESTONE_STAIRS);
            stairsS.setFacingDirection(getRelativeFacing(BlockFace.NORTH));
            fill(new Vector(5, 4 - i, 6 + i), new Vector(6, 4 - i, 6 + i), stairsS.getItemType(), stairsS);
        }
        fillWithRandomMaterial(new Vector(4, 5, 10), new Vector(7, 6, 10), random, stones);
        setBlockWithRandomMaterial(new Vector(4, 5, 9), random, stones);
        setBlockWithRandomMaterial(new Vector(7, 5, 9), random, stones);
        for (int i = 0; i < 3; i++) {
            final Stairs leftStairs = new Stairs(Material.COBBLESTONE_STAIRS);
            leftStairs.setFacingDirection(getRelativeFacing(BlockFace.SOUTH));
            setBlock(new Vector(7, 5 + i, 8 + i), leftStairs.getItemType(), leftStairs);
            final Stairs rightStairs = new Stairs(Material.COBBLESTONE_STAIRS);
            rightStairs.setFacingDirection(getRelativeFacing(BlockFace.SOUTH));
            setBlock(new Vector(4, 5 + i, 8 + i), rightStairs.getItemType(), rightStairs);
        }

        // 3rd floor inside
        fillWithRandomMaterial(new Vector(5, 8, 5), new Vector(6, 8, 5), random, stones);
        final Stairs stairsE = new Stairs(Material.COBBLESTONE_STAIRS);
        stairsE.setFacingDirection(getRelativeFacing(BlockFace.WEST));
        setBlock(new Vector(7, 8, 5), stairsE.getItemType(), stairsE);
        final Stairs stairsW = new Stairs(Material.COBBLESTONE_STAIRS);
        stairsW.setFacingDirection(getRelativeFacing(BlockFace.EAST));
        setBlock(new Vector(4, 8, 5), stairsW.getItemType(), stairsW);

        return true;
    }
}
