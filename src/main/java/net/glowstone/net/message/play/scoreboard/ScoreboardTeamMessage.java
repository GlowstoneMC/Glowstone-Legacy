package net.glowstone.net.message.play.scoreboard;

import com.flowpowered.networking.Message;

import java.util.List;

public final class ScoreboardTeamMessage implements Message {

    private final String teamName;
    private final Action action;

    // CREATE and METADATA only
    private final String displayName;
    private final String prefix;
    private final String suffix;
    private final int flags;
    private final String nametagVisibility;
    private final int color;

    // CREATE, ADD_, and REMOVE_PLAYERS only
    private final List<String> entries;

    public enum Action {
        CREATE,
        REMOVE,
        UPDATE,
        ADD_PLAYERS,
        REMOVE_PLAYERS
    }

    private ScoreboardTeamMessage(String teamName, Action action, String displayName, String prefix, String suffix, boolean friendlyFire, boolean seeInvisible, String nametagVisibility, int color, List<String> entries) {
        this.teamName = teamName;
        this.action = action;
        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.flags = (friendlyFire ? 1 : 0) | (seeInvisible ? 2 : 0);
        this.nametagVisibility = nametagVisibility;
        this.color = color;
        this.entries = entries;
    }

    public static ScoreboardTeamMessage create(String teamName, String displayName, String prefix, String suffix, boolean friendlyFire, boolean seeInvisible, String nametagVisibility, int color, List<String> players) {
        return new ScoreboardTeamMessage(teamName, Action.CREATE, displayName, prefix, suffix, friendlyFire, seeInvisible, nametagVisibility, color, players);
    }

    public static ScoreboardTeamMessage remove(String teamName) {
        return new ScoreboardTeamMessage(teamName, Action.REMOVE, null, null, null, false, false, null, 0, null);
    }

    public static ScoreboardTeamMessage update(String teamName, String displayName, String prefix, String suffix, boolean friendlyFire, boolean seeInvisible, String nametagVisibility, int color) {
        return new ScoreboardTeamMessage(teamName, Action.UPDATE, displayName, prefix, suffix, friendlyFire, seeInvisible, nametagVisibility, color, null);
    }

    public static ScoreboardTeamMessage addPlayers(String teamName, List<String> entries) {
        return new ScoreboardTeamMessage(teamName, Action.ADD_PLAYERS, null, null, null, false, false, null, 0, entries);
    }

    public static ScoreboardTeamMessage removePlayers(String teamName, List<String> entries) {
        return new ScoreboardTeamMessage(teamName, Action.REMOVE_PLAYERS, null, null, null, false, false, null, 0, entries);
    }

    public String getTeamName() {
        return teamName;
    }

    public Action getAction() {
        return action;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getFlags() {
        return flags;
    }

    public String getNametagVisibility() {
        return nametagVisibility;
    }

    public int getColor() {
        return color;
    }

    public List<String> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return "ScoreboardTeamMessage{" +
                "teamName='" + teamName + '\'' +
                ", action=" + action +
                ", displayName='" + displayName + '\'' +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", flags=" + flags + '\'' +
                ", nameTagVisibility='" + nametagVisibility + '\'' +
                ", color='" + color + '\'' +
                ", entries=" + entries +
                '}';
    }
}

