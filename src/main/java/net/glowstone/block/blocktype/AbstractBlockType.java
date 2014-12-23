package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.GlowServer;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.block.itemtype.AbstractItemType;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.Collection;

public class AbstractBlockType extends AbstractItemType implements BlockType {

    ////////////////////////////////////////////////////////////////////////////
    // Helper methods

    /**
     * Display the warning for finding the wrong MaterialData subclass.
     * @param clazz The expected subclass of MaterialData.
     * @param data The actual MaterialData found.
     */
    protected void warnMaterialData(Class<?> clazz, MaterialData data) {
        GlowServer.logger.warning("Wrong MaterialData for " + getMaterial() + " (" + getClass().getSimpleName() + "): expected " + clazz.getSimpleName() + ", got " + data);
    }

    @Override
    protected BlockType getBase() {
        return (BlockType) super.getBase();
    }

    //////////////////////
    // Stubs
    @Override
    public Collection<ItemStack> getDrops(GlowBlock block, ItemStack tool) {
        return null;
    }

    @Override
    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return null;
    }

    @Override
    public Boolean canPlaceAt(GlowBlock block, BlockFace against) {
        return null;
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
    }

    @Override
    public void afterPlace(GlowPlayer player, GlowBlock block, ItemStack holding) {
    }

    @Override
    public Boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        return null;
    }

    @Override
    public void blockDestroy(GlowPlayer player, GlowBlock block, BlockFace face) {
    }

    @Override
    public Boolean canAbsorb(GlowBlock block, BlockFace face, ItemStack holding) {
        return null;
    }

    @Override
    public Boolean canOverride(GlowBlock block, BlockFace face, ItemStack holding) {
        return null;
    }

    @Override
    public void onNearBlockChanged(GlowBlock block, BlockFace face, GlowBlock changedBlock, Material oldType, byte oldData, Material newType, byte newData) {
    }

    @Override
    public void onBlockChanged(GlowBlock block, Material oldType, byte oldData, Material newType, byte data) {
    }

    @Override
    public void onBreak(GlowBlock block, GlowPlayer player, ItemStack itemInHand) {
    }

    @Override
    public void updatePhysics(GlowBlock me) {
    }
}
