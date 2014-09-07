package net.glowstone.block.state;

import net.glowstone.GlowChunk;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.TEChest;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

public class GlowChest extends GlowBlockState implements Chest {

	public int state;
    public GlowChest(GlowBlock block) {
        super(block);
        state = 0;
    }

    private TEChest getTileEntity() {
        return (TEChest) getBlock().getTileEntity();
    }

    @Override
    public Inventory getBlockInventory() {
        return getTileEntity().getInventory();
    }

    @Override
    public Inventory getInventory() {
        // todo: handle double chests
        return getBlockInventory();
    }
    
    //closed 0, open 1
    public int getState() {
        return this.state;
    }
    
    public void setState(Byte state) {
        this.state = state;
    }
    
    public boolean ChestAnimation(byte state) {
        if (getBlock().getType() != Material.CHEST) {
            return false;
        }

        Location location = getBlock().getLocation();

        GlowChunk.Key key = new GlowChunk.Key(getX() >> 4, getZ() >> 4);
        for (GlowPlayer player : getWorld().getRawPlayers()) {
            if (player.canSee(key)) {
                player.playChestAnimation(location, state);
            }
        }

        return true;
    }
    
}
