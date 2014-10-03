package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;
import org.bukkit.inventory.ItemStack;

public final class WindowClickMessage implements Message {

    private final int id, slot, button, transaction, mode;
    private final ItemStack item;

    public WindowClickMessage(int id, int slot, int button, int transaction, int mode, ItemStack item) {
        this.id = id;
        this.slot = slot;
        this.button = button;
        this.transaction = transaction;
        this.mode = mode;
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }

    public int getButton() {
        return button;
    }

    public int getTransaction() {
        return transaction;
    }

    public int getMode() {
        return mode;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "WindowClickMessage{" +
                "id=" + id +
                ", slot=" + slot +
                ", button=" + button +
                ", transaction=" + transaction +
                ", mode=" + mode +
                ", item=" + item +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WindowClickMessage that = (WindowClickMessage) o;

        if (button != that.button) return false;
        if (id != that.id) return false;
        if (mode != that.mode) return false;
        if (slot != that.slot) return false;
        if (transaction != that.transaction) return false;
        if (item != null ? !item.equals(that.item) : that.item != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + slot;
        result = 31 * result + button;
        result = 31 * result + transaction;
        result = 31 * result + mode;
        result = 31 * result + (item != null ? item.hashCode() : 0);
        return result;
    }
}
