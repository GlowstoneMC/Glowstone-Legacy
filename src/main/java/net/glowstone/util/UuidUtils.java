package net.glowstone.util;

import net.glowstone.GlowServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * Utility methods for dealing with UUIDs.
 */
public final class UuidUtils {

    private static String getURL = "https://api.mojang.com/users/profiles/minecraft/";

    private UuidUtils() {}

    public static UUID fromFlatString(String str) {
        // xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        return UUID.fromString(str.substring(0, 8) + "-" + str.substring(8, 12) + "-" + str.substring(12, 16) +
                "-" + str.substring(16, 20) + "-" + str.substring(20, 32));
    }

    public static String toFlatString(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public static UUID fetchUUID(String username) {
        try {
            URLConnection connection = new URL(getURL + username).openConnection();
            JSONObject json;

            try (InputStream is = connection.getInputStream()) {
                json = (JSONObject) new JSONParser().parse(new InputStreamReader(is));
                return fromFlatString((String) json.get("id"));
            }
        } catch (Exception e) {
            GlowServer.logger.warning("Failed to fetch UUID for \"" + username + "\"");
            return null;
        }
    }
}
