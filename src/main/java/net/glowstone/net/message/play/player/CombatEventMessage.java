package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public class CombatEventMessage implements Message {

    public enum Event {
        ENTER_COMBAT(0),
        END_COMBAT(1),
        ENTITY_DEAD(2);

        private final int id;

        Event(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Event getAction(int id) {
            for (Event action : values()) {
                if (id == action.getId()) {
                    return action;
                }
            }
            return null;
        }
    }

    private final Event event;
    private final int duration;
    private final int entityID, playerID;
    private final String message;

    private CombatEventMessage(Event event, int duration, int entityID, int playerID, String message) {
        this.event = event;
        this.duration = duration;
        this.entityID = entityID;
        this.playerID = playerID;
        this.message = message;
    }

    public CombatEventMessage(Event event) {
        this(event, 0, 0, 0, null);
    }

    public CombatEventMessage(Event event, int duration, int entityID) {
        this(event, duration, entityID, 0, null);
    }

    public CombatEventMessage(Event event, int entityID, int playerID, String message) {
        this(event, 0, entityID, playerID, message);
    }

    public Event getEvent() {
        return event;
    }

    public int getDuration() {
        return duration;
    }

    public int getEntityID() {
        return entityID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CombatEventMessage{" +
                "event=" + event +
                ", duration=" + duration +
                ", entityID=" + entityID +
                ", playerID=" + playerID +
                ", message='" + message + '\'' +
                '}';
    }
}
