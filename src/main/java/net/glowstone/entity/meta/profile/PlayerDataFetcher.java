package net.glowstone.entity.meta.profile;

import net.glowstone.GlowServer;
import net.glowstone.util.UuidUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PlayerDataFetcher {

    private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final String PROFILE_URL_PREFIX = "?unsigned=false";

    private static final String UUID_URL = "https://api.mojang.com/profiles/minecraft";

    public static PlayerProfile getProfile(UUID uuid) {
        InputStream is;
        try {
            URL url = new URL(PROFILE_URL + UuidUtils.toFlatString(uuid) + PROFILE_URL_PREFIX);
            URLConnection conn = url.openConnection();
            is = conn.getInputStream();
        } catch (MalformedURLException e) {
            GlowServer.logger.severe("Couldn't get profile - something is wrong with the URL: " + e.toString());
            return null;
        } catch (IOException e) {
            GlowServer.logger.warning("Could't get profile - " + e.toString());
            return null;
        }

        JSONObject json;
        try {
            json = (JSONObject) new JSONParser().parse(new InputStreamReader(is));
        } catch (ParseException e) {
            GlowServer.logger.warning("Couldn't get profile for UUID " + uuid + " : " + e.toString());
            return null;
        } catch (IOException e) {
            GlowServer.logger.warning("Couldn't get profile due to IO error: " + e.toString());
            return null;
        }
        PlayerProfile profile = PlayerProfile.parseProfile(json);
        return profile;
    }

    public static UUID getUUID(String playerName) {
        HttpsURLConnection conn;
        try {
            URL url = new URL(UUID_URL);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
        } catch (MalformedURLException e) {
            GlowServer.logger.severe("Couldn't get UUID - something is wrong with the URL: " + e.toString());
            return null;
        } catch (IOException e) {
            GlowServer.logger.warning("Couldn't get UUID due to IO error: " + e.toString());
            return null;
        }

        List<String> playerList = new ArrayList<String>();
        playerList.add(playerName);

        JSONArray json;

        try {
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(JSONValue.toJSONString(playerList));
            os.flush();
            os.close();

            json = (JSONArray) JSONValue.parse(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            GlowServer.logger.warning("Couldn't get UUID due to IO error: " + e.toString());
            return null;
        }
        return parseUUIDResponse(json);
    }

    public static UUID parseUUIDResponse(JSONArray json) {
        if (json.size() > 0) {
            String uuid = (String) ((JSONObject) json.get(0)).get("id");
            return UuidUtils.fromFlatString(uuid);
        }
        return null;
    }
}
