package net.glowstone;

import java.util.HashMap;
import java.util.Map;

public final class GameRuleManager {

    private final Map<String, GameRuleValue> gameRules = new HashMap<>();

    public GameRuleManager() {
        set("commandBlockOutput", true);
        set("doDaylightCycle", true);
        set("doEntityDrops", true);
        set("doFireTick", true);
        set("doMobLoot", true);
        set("doMobSpawning", true);
        set("doTileDrops", true);
        set("keepInventory", false);
        set("logAdminCommands", true);
        set("mobGriefing", true);
        set("naturalRegeneration", true);
        set("randomTickSpeed", 3);
        set("reducedDebugInfo", false);
        set("sendCommandFeedback", true);
        set("showDeathMessages", true);
    }

    public String[] getGameRules() {
        return gameRules.keySet().toArray(new String[gameRules.size()]);
    }

    public String getGameRuleValue(String rule) {
        if (rule != null && isGameRule(rule)) {
            return get(rule).getValue();
        }
        return null;
    }

    public boolean setGameRuleValue(String rule, String value) {
        if (rule != null && value != null) {
            if (isGameRule(rule)) {
                get(rule).setValue(value);
            } else {
                set(rule, value);
            }
            return true;
        }
        return false;
    }

    public boolean isGameRule(String rule) {
        return get(rule) != null;
    }

    public boolean getBoolean(String rule) {
        if (isGameRule(rule)) {
            final GameRuleValue ruleValue = get(rule);
            if (ruleValue.getType() == GameRuleValueType.BOOLEAN) {
                return Boolean.parseBoolean(ruleValue.getValue());
            } else {
                throw new IllegalArgumentException("This GlowGameRule isn't BOOLEAN");
            }
        }
        return false;
    }

    public int getNumeric(String rule) {
        if (isGameRule(rule)) {
            final GameRuleValue ruleValue = get(rule);
            if (ruleValue.getType() == GameRuleValueType.NUMERIC) {
                try {
                    return Integer.parseInt(ruleValue.getValue());
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("This GlowGameRule isn't NUMERIC");
                }
            } else {
                throw new IllegalArgumentException("This GlowGameRule isn't NUMERIC");
            }
        }
        return -1;
    }

    private GameRuleValue get(String rule) {
        return gameRules.get(rule);
    }

    private void set(String rule, String value) {
        gameRules.put(rule, new GameRuleValue(GameRuleValueType.ANY, value));
    }

    private void set(String rule, boolean value) {
        gameRules.put(rule, new GameRuleValue(GameRuleValueType.BOOLEAN, Boolean.toString(value)));
    }

    private void set(String rule, int value) {
        gameRules.put(rule, new GameRuleValue(GameRuleValueType.NUMERIC, Integer.toString(value)));
    }

    private static class GameRuleValue {
        private final GameRuleValueType type;
        private String value;

        public GameRuleValue(GameRuleValueType type, String value) {
            this.type = type;
            this.value = value;
        }

        public GameRuleValueType getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static enum GameRuleValueType {
        ANY,
        BOOLEAN,
        NUMERIC
    }
}
