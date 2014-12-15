package net.glowstone;

import java.util.HashMap;
import java.util.Map;

public final class GameRuleManager {

    private final Map<String, String> gameRules = new HashMap<>();

    public GameRuleManager() {
        setValue("commandBlockOutput", true);
        setValue("doDaylightCycle", true);
        setValue("doEntityDrops", true);
        setValue("doFireTick", true);
        setValue("doMobLoot", true);
        setValue("doMobSpawning", true);
        setValue("doTileDrops", true);
        setValue("keepInventory", false);
        setValue("logAdminCommands", true);
        setValue("mobGriefing", true);
        setValue("naturalRegeneration", true);
        setValue("randomTickSpeed", 3);
        setValue("reducedDebugInfo", false);
        setValue("sendCommandFeedback", true);
        setValue("showDeathMessages", true);
    }

    /**
     * Gets all of the game rules defined
     *
     * @return the game rules defined, may be empty
     */
    public String[] getGameRules() {
        return gameRules.keySet().toArray(new String[gameRules.size()]);
    }

    /**
     * Sets the value of a game rule. The supplied value cannot be null. If the
     * value is not a string, the string representation of the object will be
     * used instead, which must also not return null. If the value is null, or
     * is converted to null through toString(), then this will return false.
     * <p/>
     * The actual object value is never stored, only the string value. The
     * helper methods provided in this class may be used to retrieve the value,
     * such as {@link #getAsBoolean(String)}.
     *
     * @param rule  the rule to set, cannot be null
     * @param value the value to set, cannot be null or be represented as null
     *
     * @return true if set, false otherwise
     */
    public boolean setValue(String rule, Object value) {
        if (rule != null && value != null && value.toString() != null) {
            gameRules.put(rule, value.toString());
            return true;
        }
        return false;
    }

    /**
     * Gets whether or not the supplied rule is defined
     *
     * @param rule the rule to lookup
     *
     * @return true if defined, false otherwise
     */
    public boolean isGameRule(String rule) {
        if (rule == null) return false;
        return gameRules.containsKey(rule);
    }

    /**
     * Gets the game rule value as a string. If the value does not exist, then
     * this will return null.
     *
     * @param rule the rule to lookup, must be defined
     *
     * @return the string value, or null if not defined
     */
    public String getAsString(String rule) {
        if (rule != null && isGameRule(rule)) {
            return gameRules.get(rule);
        }
        return null;
    }

    /**
     * Gets the game rule value as a boolean. If the value cannot be parsed or
     * does not exist, then this will return false.
     *
     * @param rule the rule to lookup, must be defined
     *
     * @return the boolean value
     */
    public boolean getAsBoolean(String rule) {
        if (isGameRule(rule)) {
            String value = getAsString(rule);
            if (value != null)
                return Boolean.parseBoolean(value); // Defaults to 'false'
        }
        return false;
    }

    /**
     * Gets the game rule value as an integer. If the value cannot be parsed or
     * does not exist then this will raise an IllegalStateException to indicate
     * as such.
     *
     * @param rule the rule to lookup, must be defined
     *
     * @return the integer value of the rule
     */
    public int getAsInteger(String rule) {
        if (isGameRule(rule)) {
            String value = getAsString(rule);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
                // Ignored exception: fall through to final throw
            }
        }
        throw new IllegalStateException("Value not set or cannot be parsed");
    }
}
