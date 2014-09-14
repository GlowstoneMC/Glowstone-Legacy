package net.glowstone.generator.populators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Populates the world with ores.
 */
public class OrePopulator extends BlockPopulator {

    private static final int[] iterations = new int[]{10, 20, 20, 2, 8, 1, 1, 1};
    private static final int[] amount = new int[]{32, 16, 8, 8, 7, 7, 6};
    private static final Material[] type = new Material[]{Material.GRAVEL, Material.COAL_ORE,
        Material.IRON_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE,
        Material.DIAMOND_ORE, Material.LAPIS_ORE};
    private static final int[] maxHeight = new int[]{128, 128, 128, 128, 128, 64,
        32, 16, 16, 32};

    @Override
    public void populate(World world, Random random, Chunk source) {
        for (int i = 0; i < type.length; i++) {
            for (int j = 0; j < iterations[i]; j++) {
                makeOres(source, random, random.nextInt(16), random.nextInt(maxHeight[i]), random.nextInt(16), amount[i], type[i]);
            }
        }
    }

    private static void makeOres(Chunk source, Random random, int originX, int originY, int originZ, int amount, Material type) {
        for (int i = 0; i < amount; i++) {
        	int x = originX + random.nextInt(amount / 2) - amount / 4;
            int y = originY + random.nextInt(amount / 4) - amount / 8;
            int z = originZ + random.nextInt(amount / 2) - amount / 4;
            
            /* check for ores on what level they will spawn
             * TODO: Commonly found on, Rare on
             * http://minecraft.gamepedia.com/Ore
             */
            
            if(type == Material.COAL_ORE){
        		if (y > 132 || y < 0) {
                    continue;
                }
        	}
        	if(type == Material.IRON_ORE){
        		if (y > 68 || y < 0) {
                    continue;
                }
        	}
        	if(type == Material.LAPIS_ORE){
        		if (y > 34 || y < 0) {
                    continue;
                }
        	}
        	if(type == Material.GOLD_ORE){
        		if (y > 34 || y < 0) {
                    continue;
                }
        	}
        	if(type == Material.DIAMOND_ORE){
        		if (y > 16|| y < 0) {
                    continue;
                }
        	}
        	if(type == Material.DIAMOND_ORE){
        		if (y > 16|| y < 0) {
                    continue;
                }
        	}
        	if(type == Material.REDSTONE){
        		if (y > 16 || y < 0) {
                    continue;
                }
        	}
        	
            x &= 0xf;
            z &= 0xf;
            if (y > 127 || y < 0) {
                continue;
            }
            Block block = source.getBlock(x, y, z);
            if (block.getType() == Material.STONE) {
                block.setTypeId(type.getId(), false);
            }
        }
    }
}
