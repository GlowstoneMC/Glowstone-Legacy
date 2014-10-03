package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;
import org.bukkit.inventory.ItemStack;

public final class CreativeItemMessage implements Message {

    private final int slot;
    private final ItemStack item;

    public CreativeItemMessage(int slot, ItemStack item) {
        this.slot = slot;
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "CreativeItemMessage{" +
                "slot=" + slot +
                ", item=" + item +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreativeItemMessage that = (CreativeItemMessage) o;

        if (slot != that.slot) return false;
        if (item != null ? !item.equals(that.item) : that.item != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = slot;
        result = 31 * result + (item != null ? item.hashCode() : 0);
        return result;
    }
}
