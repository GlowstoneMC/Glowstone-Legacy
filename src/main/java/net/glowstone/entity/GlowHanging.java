package net.glowstone.entity;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;

public abstract class GlowHanging extends GlowEntity implements Hanging {

    protected BlockFace facingDirection;
    private final EntityType entityType;

    protected GlowHanging(Location location, EntityType entityType) {
        super(location);
        Block block = location.getWorld().getBlockAt(location);
        BlockFace face = BlockFace.SELF;
        if (block.getRelative(BlockFace.EAST).getTypeId() == 0) {
            face = BlockFace.EAST;
        } else if (block.getRelative(BlockFace.NORTH).getTypeId() == 0) {
            face = BlockFace.NORTH;
        } else if (block.getRelative(BlockFace.WEST).getTypeId() == 0) {
            face = BlockFace.WEST;
        } else if (block.getRelative(BlockFace.SOUTH).getTypeId() == 0) {
            face = BlockFace.SOUTH;
        }
        if (face == BlockFace.NORTH || face == BlockFace.SOUTH || face == BlockFace.WEST || face == BlockFace.EAST) {
            this.facingDirection = face;
        } else {
            this.facingDirection = BlockFace.SOUTH;
        }

        this.entityType = entityType;
    }

    @Override
    public EntityType getType() {
        return this.entityType;
    }

    @Override
    public boolean setFacingDirection(BlockFace blockFace, boolean force) {
        if (blockFace == BlockFace.NORTH || blockFace == BlockFace.SOUTH || blockFace == BlockFace.WEST || blockFace == BlockFace.EAST) {
            this.facingDirection = blockFace;
        } else {
            this.facingDirection = BlockFace.SOUTH;
        }
        return true;
    }

    @Override
    public BlockFace getAttachedFace() {
        return facingDirection.getOppositeFace();
    }

    @Override
    public BlockFace getFacing() {
        return facingDirection;
    }

    @Override
    public void setFacingDirection(BlockFace blockFace) {
        setFacingDirection(blockFace, false);
    }
}
