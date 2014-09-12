package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import org.bukkit.inventory.ItemStack;

public final class EntityEquipmentMessage implements Message {

    public static final int HELD_ITEM = 0;
    public static final int BOOTS_SLOT = 1;
    public static final int LEGGINGS_SLOT = 2;
    public static final int CHESTPLATE_SLOT = 3;
    public static final int HELMET_SLOT = 4;

    private final int id, slot;
    private final ItemStack stack;

    public EntityEquipmentMessage(int id, int slot, ItemStack stack) {
        this.id = id;
        this.slot = slot;
        this.stack = stack;
    }

    public int getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityEquipmentMessage that = (EntityEquipmentMessage) o;

        if (id != that.id) return false;
        if (slot != that.slot) return false;
        if (stack != null ? !stack.equals(that.stack) : that.stack != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + slot;
        result = 31 * result + (stack != null ? stack.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EntityEquipmentMessage{" +
                "id=" + id +
                ", slot=" + slot +
                ", stack=" + stack +
                '}';
    }
}
