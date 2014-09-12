package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;

public final class EnchantItemMessage implements Message {

    private final int window, enchantment;

    public EnchantItemMessage(int window, int enchantment) {
        this.window = window;
        this.enchantment = enchantment;
    }

    public int getWindow() {
        return window;
    }

    public int getEnchantment() {
        return enchantment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnchantItemMessage that = (EnchantItemMessage) o;

        if (enchantment != that.enchantment) return false;
        if (window != that.window) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = window;
        result = 31 * result + enchantment;
        return result;
    }

    @Override
    public String toString() {
        return "EnchantItemMessage{window=" + window + ",enchantment=" + enchantment + "}";
    }
}
