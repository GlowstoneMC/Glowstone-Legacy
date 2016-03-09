package net.glowstone.block;

import org.bukkit.Material;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBuiltinMaterialValueManager {
    @Test
    public void correctMaterialValues() {
        //throws exception if wrong file format
        MaterialValueManager bvm = new BuiltinMaterialValueManager();

        //test some 'features'
        assertEquals(Float.MAX_VALUE, bvm.getValues(Material.BEDROCK).getHardness(), 0); //in yml -1

        //test some fixed values
        assertEquals(6000, bvm.getValues(Material.OBSIDIAN).getBlastResistance(), 0);
        assertEquals(50, bvm.getValues(Material.OBSIDIAN).getHardness(), 0);

        //test defaults
        assertEquals(1, bvm.getValues(Material.CAULDRON_ITEM).getHardness(), 0); //item doesn't exist at all
    }
}
