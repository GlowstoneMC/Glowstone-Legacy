package net.glowstone.block.blocktype;

import org.bukkit.Material;

public class BlockOre extends BlockType {
	
    public Material getDropType(Material Ore){
        Material o = Ore;
        
        switch(o){
            case EMERALD_ORE: {
                return Material.EMERALD;
            }
            case DIAMOND_ORE: {
                return Material.DIAMOND;
            }
            case REDSTONE_ORE: {
                return Material.REDSTONE;
            }
            case LAPIS_ORE: {
                return Material.INK_SACK; //TODO: add data values to this
            }
            case COAL_ORE: {
                return Material.COAL;
            }
            default:
                return Ore;
            }
    }
    
    public int getDropCount(){
        return 0;
    }
    
    public int getExpCount(){
        return 0;
    }
    
}
