package net.glowstone.entity.objects;

import com.flowpowered.networking.Message;

import net.glowstone.entity.GlowEntity;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.EntityTeleportMessage;
import net.glowstone.net.message.play.entity.EntityVelocityMessage;
import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import net.glowstone.util.Position;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an item frame that is also an {@link net.glowstone.entity.GlowEntity} within the world.
 */
public final class GlowItemFrame extends GlowEntity implements ItemFrame {

    /**
     * Creates a new item frame entity.
     * @param location The location of the entity.
     * @param face The clicked item face
     */
	BlockFace face;
	
    public GlowItemFrame(Location location, BlockFace clickedface) {
        super(location);
        this.face = clickedface;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Overrides


    @Override
    public List<Message> createSpawnMessage() {
        int x = Position.getIntX(location);
        int y = Position.getIntY(location);
        int z = Position.getIntZ(location);
        int xoffset = 0;
        int zoffset = 0;
        int yaw = 0;
        switch (getFacingNumber(face)){
            case 1:
                xoffset = -32;
                yaw = 64;
                break;
            case 2:
                zoffset = -32;
                yaw = -128;
                break;
            case 3:
                xoffset = +32;
                yaw = -64;
                break;
            case 0:
                zoffset = +32;
                yaw = 0;
                break;
        }
        return Arrays.asList(
               ((Message) new SpawnObjectMessage(id, 71, x + xoffset, y, z +zoffset , 0, yaw, getFacingNumber(face), 0 ,0 ,0))
        );
    }

    private byte getFacingNumber(BlockFace face) {
        switch (face) {
            case SOUTH:
                return 0;
            case WEST:
                return 1;
            case NORTH:
                return 2;
            case EAST:
                return 3;
            default:
                return 0;
        }
    }
    
    private BlockFace getFace(int face) {
        switch (face) {
            case 0:
                return BlockFace.SOUTH;
            case 1:
                return BlockFace.WEST;
            case 2:
                return BlockFace.NORTH;
            case 3:
                return BlockFace.EAST;
            default:
                return BlockFace.SOUTH;
        }
    }
    
	public boolean setFacingDirection(BlockFace arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EntityType getType() {
		// TODO Auto-generated method stub
		return EntityType.ITEM_FRAME;
	}

	@Override
	public BlockFace getAttachedFace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockFace getFacing() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getFacingNumber() {
		return getFacingNumber(face);
	}

	@Override
	public void setFacingDirection(BlockFace arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setFacingDirectionNumber(int direction) {
		face = getFace(direction);
	}
	
	@Override
	public ItemStack getItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rotation getRotation() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void setItem(ItemStack arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRotation(Rotation arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

}
