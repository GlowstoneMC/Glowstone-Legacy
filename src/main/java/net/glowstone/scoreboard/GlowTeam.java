package net.glowstone.scoreboard;

import com.flowpowered.networking.Message;
import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.glowstone.net.message.play.scoreboard.ScoreboardTeamMessage;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.NametagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Implementation for scoreboard teams.
 */
public final class GlowTeam implements Team {

    private GlowScoreboard scoreboard;
    private final String name;

    private final HashSet<OfflinePlayer> players = new HashSet<>();

    // properties
    private String displayName;
    private String prefix = "";
    private String suffix = "";
    private boolean friendlyFire = false;
    private boolean seeInvisible = true;
    private NametagVisibility nametagVisibility = NametagVisibility.ALWAYS;
    private ChatColor color = ChatColor.RESET;

    public GlowTeam(GlowScoreboard scoreboard, String name) {
        this.scoreboard = scoreboard;
        this.name = name;
        displayName = name;
    }

    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public void unregister() throws IllegalStateException {
        checkValid();
        scoreboard.removeTeam(this);
        scoreboard = null;
    }

    void checkValid() {
        if (scoreboard == null) {
            throw new IllegalStateException("Cannot manipulate unregistered team");
        }
    }

    Message getCreateMessage() {
        List<String> playerNames = new ArrayList<>(players.size());
        for (OfflinePlayer player : players) {
            playerNames.add(player.getName());
        }
        
        return ScoreboardTeamMessage.create(name, displayName, prefix, suffix, friendlyFire, seeInvisible
                , CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, nametagVisibility.name()), color.isColor() ? color.ordinal() : -1, playerNames);
    }

    private void update() {
        scoreboard.broadcast(ScoreboardTeamMessage.update(name, displayName, prefix, suffix, friendlyFire, seeInvisible
                , CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, nametagVisibility.name()), color.isColor() ? color.ordinal() : -1));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Properties

    @Override
    public String getName() throws IllegalStateException {
        checkValid();
        return name;
    }

    @Override
    public String getDisplayName() throws IllegalStateException {
        checkValid();
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(displayName, "Display name cannot be null");
        checkValid();
        this.displayName = displayName;
        update();
    }

    @Override
    public String getPrefix() throws IllegalStateException {
        checkValid();
        return prefix;
    }

    @Override
    public void setPrefix(String prefix) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(prefix, "Prefix cannot be null");
        checkValid();
        this.prefix = prefix;
        update();
    }

    @Override
    public String getSuffix() throws IllegalStateException {
        checkValid();
        return suffix;
    }

    @Override
    public void setSuffix(String suffix) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(suffix, "Suffix cannot be null");
        checkValid();
        this.suffix = suffix;
        update();
    }

    @Override
    public boolean allowFriendlyFire() throws IllegalStateException {
        checkValid();
        return friendlyFire;
    }

    @Override
    public void setAllowFriendlyFire(boolean enabled) throws IllegalStateException {
        checkValid();
        friendlyFire = enabled;
        update();
    }

    @Override
    public boolean canSeeFriendlyInvisibles() throws IllegalStateException {
        checkValid();
        return seeInvisible;
    }

    @Override
    public void setCanSeeFriendlyInvisibles(boolean enabled) throws IllegalStateException {
        checkValid();
        seeInvisible = enabled;
        update();
    }

    @Override
    public NametagVisibility getNametagVisibility() {
        return nametagVisibility;
    }

    @Override
    public void setNametagVisibility(NametagVisibility visibility) {
        Validate.notNull(visibility, "Visibility cannot be null");
        nametagVisibility = visibility;
        update();
    }

    @Override
    public ChatColor getColor() {
        return color;
    }

    @Override
    public void setColor(ChatColor color) {
        checkValid();
        Validate.notNull(color, "Color cannot be null");
        Validate.isTrue(!color.isFormat() , "Color must be either a color or RESET");
        this.color = color;
        update();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Player management

    @Override
    public Set<OfflinePlayer> getPlayers() throws IllegalStateException {
        checkValid();
        return ImmutableSet.copyOf(players);
    }

    @Override
    public int getSize() throws IllegalStateException {
        checkValid();
        return players.size();
    }

    @Override
    public void addPlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(player, "Player cannot be null");
        checkValid();
        players.add(player);
        scoreboard.setPlayerTeam(player, this);
    }

    @Override
    public boolean removePlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(player, "Player cannot be null");
        checkValid();
        if (players.remove(player)) {
            scoreboard.setPlayerTeam(player, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull(player, "Player cannot be null");
        checkValid();
        return players.contains(player);
    }

    /**
     * Remove a player without propagating the change to the scoreboard.
     * @param player The player to remove.
     */
    void rawRemovePlayer(OfflinePlayer player) {
        players.remove(player);
    }
}
