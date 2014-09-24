package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

public class TitleMessage implements Message {

    public enum Action {
        TITLE(0),
        SUBTITLE(1),
        TIMES(2),
        CLEAR(3),
        RESET(4);

        private final int id;

        Action(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Action getAction(int id) {
            for (Action action : values()) {
                if (id == action.getId()) {
                    return action;
                }
            }
            return null;
        }
    }

    private final Action action;
    private final String text;
    private final int fadeIn, stay, fadeOut;

    public TitleMessage(Action action, String text) {
        this.action = action;
        this.text = text;
        this.fadeIn = 0;
        this.stay = 0;
        this.fadeOut = 0;
    }

    public TitleMessage(Action action, int fadeIn, int stay, int fadeOut) {
        this.action = action;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        this.text = null;
    }

    public TitleMessage(Action action) {
        this.action = action;
        this.text = null;
        this.fadeIn = 0;
        this.stay = 0;
        this.fadeOut = 0;
    }

    public Action getAction() {
        return action;
    }

    public String getText() {
        return text;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    @Override
    public String toString() {
        return "TitleMessage{" +
                "action=" + action +
                ", text='" + text + '\'' +
                ", fadeIn=" + fadeIn +
                ", stay=" + stay +
                ", fadeOut=" + fadeOut +
                '}';
    }

}
