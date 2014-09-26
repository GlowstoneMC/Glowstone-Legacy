package net.glowstone.block.state;

import net.glowstone.GlowServer;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockChest;
import net.glowstone.block.entity.TEChest;
import net.glowstone.block.entity.TEContainer;
import net.glowstone.inventory.GlowDoubleChestInventory;
import net.glowstone.inventory.GlowInventory;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GlowChest extends GlowBlockState implements Chest {

    public GlowChest(GlowBlock block) {
        super(block);
    }

    private TEChest getTileEntity() {
        return (TEChest) getBlock().getTileEntity();
    }

    @Override
    public Inventory getBlockInventory() {
        return getTileEntity().getInventory();
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        ItemStack[] contents = getBlockInventory().getContents();

        boolean result = super.update(force, applyPhysics);

        if (result) {
            TEChest tileEntity = getTileEntity();
            tileEntity.setContent(contents);
            tileEntity.updateInRange();
        }

        return result;
    }

    @Override
    public Inventory getInventory() {
        BlockChest blockType = (BlockChest) ItemTable.instance().getBlock(getType());

        GlowBlock me = this.getBlock();

        BlockFace otherChestPart = blockType.getAttachedChest(me);
        if (otherChestPart == null) {
            return getBlockInventory();
        }

        GlowBlock otherChest = me.getRelative(otherChestPart);

        switch (otherChestPart) {
            case SOUTH:
                return getDoubleInventory(me, otherChest, true);
            case EAST:
                return getDoubleInventory(me, otherChest, false);
            case WEST:
                return getDoubleInventory(otherChest, me, false);
            case NORTH:
                return getDoubleInventory(otherChest, me, true);

            default:
                GlowServer.logger.warning("GlowChest#getInventory() can only handle N/O/S/W BlockFaces, got " + otherChestPart);
                return getBlockInventory();
        }
    }

    private static Inventory getDoubleInventory(GlowBlock firstBlock, GlowBlock secondBlock, boolean isFirstLeft) {
        Inventory firstSlots = ((TEContainer) firstBlock.getTileEntity()).getInventory();
        Inventory secondSlots = ((TEContainer) secondBlock.getTileEntity()).getInventory();

        return new GlowDoubleChestInventory((GlowInventory) secondSlots, (GlowInventory) firstSlots, isFirstLeft);
    }
}
