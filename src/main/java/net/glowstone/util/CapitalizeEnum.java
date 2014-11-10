package net.glowstone.util;

public final class CapitalizeEnum {

    private CapitalizeEnum() { }

    public static <T> String capitalizeEnum(T value) {
        String string = value.toString();
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

}
