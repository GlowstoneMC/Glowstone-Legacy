package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;
import org.bukkit.util.Vector;

public class ItemMonsterEgg extends ItemType {

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        final SpawnEgg data = (SpawnEgg) holding.getData();
        final EntityType type = data.getSpawnedType();
        switch (face) {
            case UP:
                target.getWorld().spawnEntity(target.getLocation().add(0.5, 1.0, 0.5), type);
                break;
            case DOWN:
                target.getWorld().spawnEntity(target.getLocation().add(0.5, -1.0, 0.5), type);
                break;
            case NORTH:
                target.getWorld().spawnEntity(target.getLocation().add(0.5, -1.0, -0.5), type);
                break;
            case SOUTH:
                target.getWorld().spawnEntity(target.getLocation().add(0.5, -1.0, 1.5), type);
                break;
            case EAST:
                target.getWorld().spawnEntity(target.getLocation().add(1.5, -1.0, 0.5), type);
                break;
            case WEST:
                target.getWorld().spawnEntity(target.getLocation().add(-0.5, -1.0, 0.5), type);
                break;
            default:
                target.getWorld().spawnEntity(target.getLocation().add(0.5, 1.0, 0.5), type);
                break;
        }
    }
}
