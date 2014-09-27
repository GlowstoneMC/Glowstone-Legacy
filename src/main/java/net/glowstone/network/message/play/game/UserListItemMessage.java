package net.glowstone.network.message.play.game;

import com.flowpowered.networking.Message;
import net.glowstone.entity.meta.PlayerProfile;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class UserListItemMessage implements Message {

    private final Action action;
    private final List<Entry> entries;

    public UserListItemMessage(Action action, List<Entry> entries) {
        this.action = action;
        this.entries = entries;

        for (Entry entry : entries) {
            if (entry.action != action) {
                throw new IllegalArgumentException("Entries must be " + action + ", not " + entry.action);
            }
        }
    }

    public UserListItemMessage(Action action, Entry... entries) {
        this(action, Arrays.asList(entries));
    }

    public Action getAction() {
        return action;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    // add

    public static Entry add(PlayerProfile profile) {
        return add(profile, 0, 0, null);
    }

    public static Entry add(PlayerProfile profile, int gameMode, int ping, JSONObject displayName) {
        return new Entry(Action.ADD_PLAYER, profile.getUniqueId(), profile, gameMode, ping, displayName);
    }

    public static UserListItemMessage addOne(PlayerProfile profile) {
        return new UserListItemMessage(Action.ADD_PLAYER, add(profile));
    }

    // gamemode

    public static Entry gameMode(UUID uuid, int gameMode) {
        return new Entry(Action.UPDATE_GAMEMODE, uuid, null, gameMode, 0, null);
    }

    public static UserListItemMessage gameModeOne(UUID uuid, int gameMode) {
        return new UserListItemMessage(Action.UPDATE_GAMEMODE, gameMode(uuid, gameMode));
    }

    // latency

    public static Entry latency(UUID uuid, int ping) {
        return new Entry(Action.UPDATE_LATENCY, uuid, null, 0, ping, null);
    }

    public static UserListItemMessage latencyOne(UUID uuid, int ping) {
        return new UserListItemMessage(Action.UPDATE_LATENCY, latency(uuid, ping));
    }

    // display name

    public static Entry displayName(UUID uuid, JSONObject displayName) {
        return new Entry(Action.UPDATE_DISPLAY_NAME, uuid, null, 0, 0, displayName);
    }

    public static UserListItemMessage displayNameOne(UUID uuid, JSONObject displayName) {
        return new UserListItemMessage(Action.UPDATE_DISPLAY_NAME, displayName(uuid, displayName));
    }

    // remove

    public static Entry remove(UUID uuid) {
        return new Entry(Action.REMOVE_PLAYER, uuid, null, 0, 0, null);
    }

    public static UserListItemMessage removeOne(UUID uuid) {
        return new UserListItemMessage(Action.REMOVE_PLAYER, remove(uuid));
    }

    @Override
    public String toString() {
        return "UserListItemMessage{" +
                "action=" + action +
                ", entries=" + entries +
                '}';
    }

    public static enum Action {
        ADD_PLAYER,
        UPDATE_GAMEMODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    public static final class Entry {
        private final Action action;
        public final UUID uuid;
        public final PlayerProfile profile;
        public final int gameMode;
        public final int ping;
        public final JSONObject displayName;

        private Entry(Action action, UUID uuid, PlayerProfile profile, int gameMode, int ping, JSONObject displayName) {
            this.action = action;
            this.uuid = uuid;
            this.profile = profile;
            this.gameMode = gameMode;
            this.ping = ping;
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "action=" + action +
                    ", uuid=" + uuid +
                    ", profile=" + profile +
                    ", gameMode=" + gameMode +
                    ", ping=" + ping +
                    ", displayName=" + displayName +
                    '}';
        }
    }
}
