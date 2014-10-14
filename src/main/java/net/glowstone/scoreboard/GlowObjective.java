package net.glowstone.scoreboard;

import net.glowstone.net.message.play.scoreboard.ScoreboardObjectiveMessage;
import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Scoreboard objective and associated data.
 */
public final class GlowObjective implements Objective {

    private GlowScoreboard scoreboard;
    private final String name;
    private final String criteria;

    private final HashMap<String, GlowScore> scores = new HashMap<>();

    private String displayName;
    DisplaySlot displaySlot;
    private RenderType type = RenderType.INTEGER;

    public GlowObjective(GlowScoreboard scoreboard, String name, String criteria) {
        this.scoreboard = scoreboard;
        this.name = name;
        this.criteria = criteria;
        displayName = name;
    }

    @Override
    public GlowScoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public void unregister() throws IllegalStateException {
        checkValid();
        for (Map.Entry<String, GlowScore> entry : scores.entrySet()) {
            scoreboard.getScoresForName(entry.getKey()).remove(entry.getValue());
        }
        scoreboard.removeObjective(this);
        scoreboard = null;
    }

    void checkValid() {
        if (scoreboard == null) {
            throw new IllegalStateException("Cannot manipulate unregistered objective");
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Properties

    @Override
    public String getName() throws IllegalStateException {
        checkValid();
        return name;
    }

    @Override
    public String getCriteria() throws IllegalStateException {
        checkValid();
        return criteria;
    }

    @Override
    public String getDisplayName() throws IllegalStateException {
        checkValid();
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException {
        checkValid();
        Validate.notNull(displayName, "displayName cannot be null");
        Validate.isTrue(displayName.length() <= 32, "displayName cannot be longer than 32 characters");

        this.displayName = displayName;
        scoreboard.broadcast(ScoreboardObjectiveMessage.update(name, displayName, type.name().toLowerCase()));
    }

    @Override
    public DisplaySlot getDisplaySlot() throws IllegalStateException {
        checkValid();
        return displaySlot;
    }

    @Override
    public void setDisplaySlot(DisplaySlot slot) throws IllegalStateException {
        checkValid();
        if (slot != displaySlot) {
            if (displaySlot != null) {
                scoreboard.setDisplaySlot(displaySlot, null);
            }
            if (slot != null) {
                scoreboard.setDisplaySlot(slot, this);
            }
        }
    }

    @Override
    public RenderType getType() {
        checkValid();
        return type;
    }

    @Override
    public void setType(RenderType type) {
        checkValid();
        Validate.notNull(type, "type cannot be null");
        this.type = type;
        scoreboard.broadcast(ScoreboardObjectiveMessage.update(name, displayName, type.name().toLowerCase()));
    }

    @Override
    public boolean isModifiable() throws IllegalStateException {
        checkValid();
        return !criteria.equalsIgnoreCase(Criterias.HEALTH);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Score management

    @Override
    public Score getScore(String entry) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull(entry, "Entry cannot be null");
        checkValid();

        GlowScore score = scores.get(entry);
        if (score == null) {
            score = new GlowScore(this, entry);
            scores.put(entry, score);
            scoreboard.getScoresForName(entry).add(score);
        }
        return score;
    }

    /**
     * Deletes a score directly.
     * @param entry The key to delete.
     */
    void deleteScore(String entry) {
        scores.remove(entry);
    }

    @Deprecated
    @Override
    public Score getScore(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull(player, "Player cannot be null");
        return getScore(player.getName());
    }
}
