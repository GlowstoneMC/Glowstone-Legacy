package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import org.bukkit.inventory.ItemStack;

public final class BlockPlacementMessage implements Message {

    private final int x, y, z, direction;
    private final ItemStack heldItem;
    private final int cursorX, cursorY, cursorZ;

    public BlockPlacementMessage(int x, int y, int z, int direction, ItemStack heldItem, int cursorX, int cursorY, int cursorZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
        this.heldItem = heldItem;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.cursorZ = cursorZ;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getDirection() {
        return direction;
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public int getCursorX() {
        return cursorX;
    }

    public int getCursorY() {
        return cursorY;
    }

    public int getCursorZ() {
        return cursorZ;
    }

    @Override
    public String toString() {
        return "BlockPlacementMessage{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", direction=" + direction +
                ", heldItem=" + heldItem +
                ", cursorX=" + cursorX +
                ", cursorY=" + cursorY +
                ", cursorZ=" + cursorZ +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockPlacementMessage that = (BlockPlacementMessage) o;

        if (cursorX != that.cursorX) return false;
        if (cursorY != that.cursorY) return false;
        if (cursorZ != that.cursorZ) return false;
        if (direction != that.direction) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        if (heldItem != null ? !heldItem.equals(that.heldItem) : that.heldItem != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + direction;
        result = 31 * result + (heldItem != null ? heldItem.hashCode() : 0);
        result = 31 * result + cursorX;
        result = 31 * result + cursorY;
        result = 31 * result + cursorZ;
        return result;
    }
}
