package net.glowstone.block.blocktype;

import org.bukkit.block.BlockFace;
import static org.bukkit.block.BlockFace.DOWN;
import static org.bukkit.block.BlockFace.EAST;
import static org.bukkit.block.BlockFace.NORTH;
import static org.bukkit.block.BlockFace.SOUTH;
import static org.bukkit.block.BlockFace.UP;
import static org.bukkit.block.BlockFace.WEST;
import org.bukkit.block.BlockState;

public class BlockAttachable extends BlockType {

    public void setAttachedFace(BlockState state, BlockFace attachedFace) {
        byte data = state.getRawData();
        switch (attachedFace) {
            case UP: {
                data |= 0;
                break;
            }
            case WEST: {
                data |= 1;
                break;
            }
            case EAST: {
                data |= 2;
                break;
            }
            case NORTH: {
                data |= 3;
                break;
            }
            case SOUTH: {
                data |= 4;
                break;
            }
            case DOWN: {
                data |= 5;
                break;
            }
        }
        state.setRawData(data);
    }

}
