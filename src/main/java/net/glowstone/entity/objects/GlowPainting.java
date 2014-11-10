package net.glowstone.entity.objects;

import com.flowpowered.networking.Message;
import net.glowstone.entity.GlowHanging;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.SpawnPaintingMessage;
import net.glowstone.util.CapitalizeEnum;
import org.bukkit.Art;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GlowPainting extends GlowHanging implements Painting {

    Art motive;

    public GlowPainting(Location location) {
        this(location, Art.BURNINGSKULL);
    }

    public GlowPainting(Location location, Art art) {
        super(location, EntityType.PAINTING);
        this.motive = art;
    }

    public static boolean fitsOnWall(World world, Location location, BlockFace facingDirection, Art motive) {
        Block[][] blocks;
        BlockFace right;
        switch (facingDirection) {
            case SOUTH:
                right = BlockFace.EAST;
                break;
            case NORTH:
                right = BlockFace.WEST;
                break;
            case WEST:
                right = BlockFace.SOUTH;
                break;
            case EAST:
                right = BlockFace.NORTH;
                break;
            default:
                right = BlockFace.WEST;
                break;
        }
        if (motive == Art.KEBAB || motive == Art.AZTEC || motive == Art.ALBAN || motive == Art.AZTEC2 || motive == Art.BOMB || motive == Art.PLANT
            || motive == Art.WASTELAND) {
            blocks = new Block[1][1];
            blocks[0][0] = world.getBlockAt(location).getRelative(facingDirection.getOppositeFace());
        } else if (motive == Art.WANDERER || motive == Art.GRAHAM) {
            blocks = new Block[2][1];
            blocks[0][0] = world.getBlockAt(location).getRelative(facingDirection.getOppositeFace()).getRelative(BlockFace.UP);
            blocks[1][0] = world.getBlockAt(location).getRelative(facingDirection.getOppositeFace());
        } else if (motive == Art.POOL || motive == Art.COURBET || motive == Art.SUNSET || motive == Art.SEA || motive == Art.CREEBET) {
            blocks = new Block[1][2];
            blocks[0][0] = world.getBlockAt(location).getRelative(facingDirection.getOppositeFace());
            blocks[0][1] = world.getBlockAt(location).getRelative(facingDirection.getOppositeFace()).getRelative(right);
        } else if (motive == Art.MATCH || motive == Art.BUST || motive == Art.STAGE || motive == Art.VOID || motive == Art.SKULL_AND_ROSES
                   || motive == Art.WITHER) {
            blocks = new Block[2][2];
            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 2; x++) {
                    blocks[y][x] =
                            world.getBlockAt(location).getRelative(facingDirection.getOppositeFace()).getRelative(right, x)
                                    .getRelative(BlockFace.UP, y);
                }
            }
        } else if (motive == Art.FIGHTERS) {
            blocks = new Block[2][4];
            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 4; x++) {
                    blocks[y][x] =
                            world.getBlockAt(location).getRelative(facingDirection.getOppositeFace()).getRelative(right, x - 1)
                                    .getRelative(BlockFace.UP, y);
                }
            }
        } else if (motive == Art.SKELETON || motive == Art.DONKEYKONG) {
            blocks = new Block[3][4];
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 4; x++) {
                    blocks[y][x] =
                            world.getBlockAt(location).getRelative(facingDirection.getOppositeFace()).getRelative(right, x - 1)
                                    .getRelative(BlockFace.UP, y - 1);
                }
            }
        } else if (motive == Art.POINTER || motive == Art.PIGSCENE || motive == Art.BURNINGSKULL) {
            blocks = new Block[4][4];
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    blocks[y][x] =
                            world.getBlockAt(location).getRelative(facingDirection.getOppositeFace()).getRelative(right, x - 1)
                                    .getRelative(BlockFace.UP, y - 1);
                }
            }
        } else {
            blocks = new Block[0][0];
        }

        for (Block[] y : blocks) {
            for (Block block : y) {
                if (!block.getType().isSolid()) {
                    return false;
                }
                if (block.getRelative(facingDirection).getType().isSolid()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String artToString(Art art) {
        switch (art) {
            case SKULL_AND_ROSES:
                return "SkullAndRoses";
            case DONKEYKONG:
                return "DonkeyKong";
            case BURNINGSKULL:
                return "BurningSkull";
            default:
                return CapitalizeEnum.capitalizeEnum(art);
        }
    }

    public static int getFacingInt(BlockFace facing) {
        switch (facing) {
            case SOUTH:
                return 0;
            case WEST:
                return 1;
            case NORTH:
                return 2;
            case EAST:
                return 3;
            default:
                return 2;
        }
    }

    @Override
    public List<Message> createSpawnMessage() {
        int x = getTileX();
        int y = getTileY();
        int z = getTileZ();
        return Arrays.asList(new SpawnPaintingMessage(id, artToString(motive), x, y, z, getFacingInt(facingDirection)),
                             new EntityMetadataMessage(id, metadata.getEntryList()));
    }

    @Override
    public List<Message> createUpdateMessage() {
        List<Message> result = new LinkedList<>();
        return result;
    }

    @Override
    public Art getArt() {
        return motive;
    }

    @Override
    public boolean setArt(Art motive) {
        return setArt(motive, false);
    }

    @Override
    public boolean setArt(Art motive, boolean force) {
        this.motive = motive;
        if (!force && !fitsOnWall(getWorld(), location, facingDirection, motive)) {
            remove();
            return false;
        } else {
            return true;
        }
    }

    public int getTileX() {
        return (int) location.getX();
    }

    public void setTileX(int tileX) {
        location.setX(tileX);
    }

    public int getTileY() {
        return (int) location.getY();
    }

    public void setTileY(int tileY) {
        location.setY(tileY);
    }

    public int getTileZ() {
        return (int) location.getZ();
    }

    public void setTileZ(int tileZ) {
        location.setZ(tileZ);
    }

    public void setTilePosition(int tileX, int tileY, int tileZ) {
        setTileX(tileX);
        setTileY(tileY);
        setTileZ(tileZ);
    }

    @Override
    public boolean hasMoved() {
        return false;
    }

    @Override
    public boolean hasRotated() {
        return false;
    }

    @Override
    public Vector getVelocity() {
        return new Vector(0, 0, 0);
    }

    @Override
    public void setVelocity(Vector velocity) {
        return;
    }

    @Override
    public boolean setFacingDirection(BlockFace blockFace, boolean force) {
        if (super.setFacingDirection(blockFace, force)) {
            if (!force && !fitsOnWall(getWorld(), location, facingDirection, motive)) {
                remove();
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void pulse() {
        super.pulse();
        if (!fitsOnWall(getWorld(), location, facingDirection, motive)) {
            remove();
        }
    }



    @Override
    public void remove() {
        super.remove();
        world.dropItem(location, new ItemStack(Material.PAINTING));
    }
}
