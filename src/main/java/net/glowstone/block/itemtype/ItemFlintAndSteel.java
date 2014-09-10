package net.glowstone.block.itemtype;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemFlintAndSteel extends ItemType {
    public ItemFlintAndSteel() {
        setMaxStackSize(1);
    }

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        switch (target.getType()) {
            case TNT:
                fireTnt(target);
                break;
            case OBSIDIAN:
                fireNetherPortal();
                break;
            //TODO check for non flammable blocks
            default:
                setBlockOnFire(player, target, face, holding, clickedLoc);
        }
    }

    private void fireNetherPortal() {
        //TODO
    }

    private void fireTnt(GlowBlock tnt) {
        //TODO Event
        tnt.setType(Material.AIR);
        tnt.getWorld().spawnEntity(tnt.getLocation(), EntityType.PRIMED_TNT);
    }

    private void setBlockOnFire(GlowPlayer player, GlowBlock clicked, BlockFace face, ItemStack holding, Vector clickedLoc) {
        GlowBlock fireBlock = clicked.getRelative(face);
        if (fireBlock.getType() != Material.AIR) {
            return;
        }

        BlockIgniteEvent event = EventFactory.onBlockIgnite(fireBlock, BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL, player, null);
        if (event.isCancelled()) {
            player.setItemInHand(holding);
            return;
        }

        ItemStack afterUse = holding.clone();
        if (player.getGameMode() != GameMode.CREATIVE) {
            afterUse.setDurability((short) (afterUse.getDurability() + 1));
            if (afterUse.getDurability() == 65) {
                EventFactory.onPlayerItemBreakEvent(player, afterUse);
                afterUse = null;
            }
        }

        ItemTable.instance().getBlock(Material.FIRE).rightClickBlock(player, clicked, BlockFace.UP, holding, clickedLoc);

        player.setItemInHand(afterUse);
    }
}
