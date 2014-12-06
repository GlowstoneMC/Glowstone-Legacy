package net.glowstone.util;

public interface Validator<E> {
    boolean isValid(E object);
}
