package net.glowstone.net.message.play.scoreboard;

import com.flowpowered.networking.Message;

public final class ScoreboardObjectiveMessage implements Message {

    private final String name;
    private final String displayName;
    private final String type;
    private final int action;

    private enum Action {
        CREATE,
        REMOVE,
        UPDATE
    }

    private ScoreboardObjectiveMessage(String name, String displayName, String type, Action action) {
        this.name = name;
        this.displayName = displayName;
        this.type = type;
        this.action = action.ordinal();
    }

    public static ScoreboardObjectiveMessage create(String name, String displayName, String type) {
        return new ScoreboardObjectiveMessage(name, displayName, type, Action.CREATE);
    }

    public static ScoreboardObjectiveMessage remove(String name) {
        return new ScoreboardObjectiveMessage(name, null, null, Action.REMOVE);
    }

    public static ScoreboardObjectiveMessage update(String name, String displayName, String type) {
        return new ScoreboardObjectiveMessage(name, displayName, type, Action.UPDATE);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getType() {
        return type;
    }

    public int getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "ScoreboardObjectiveMessage{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type=" + type + '\'' +
                ", action=" + action +
                '}';
    }
}

