package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import net.glowstone.util.nbt.CompoundTag;

public final class UpdateBlockEntityMessage implements Message {

    private final int x, y, z, action;

    private final CompoundTag nbt;

    public UpdateBlockEntityMessage(int x, int y, int z, int action, CompoundTag nbt) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.action = action;
        this.nbt = nbt;
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

    public int getAction() {
        return action;
    }

    public CompoundTag getNbt() {
        return nbt;
    }

    @Override
    public String toString() {
        return "UpdateBlockEntityMessage{x=" + x + ",y=" + y + ",z=" + z + ",type=" + action + ",nbt=" + nbt.toString() + "}";
    }
}
