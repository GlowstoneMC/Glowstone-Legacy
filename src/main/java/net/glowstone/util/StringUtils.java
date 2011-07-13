package net.glowstone.util;

/**
 * Some useful methods for handling strings.
 * @author Graham Edgecombe
 */

public final class StringUtils {

	public static String join(String[] items, String glue) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < items.length; i++) {
			builder.append(items[i]);
			if (i != items.length - 1) {
				builder.append(glue);
			}
		}
		return builder.toString();
	}

	private StringUtils() {

	}

}

