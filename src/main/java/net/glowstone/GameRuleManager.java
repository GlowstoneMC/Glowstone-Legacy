package net.glowstone;

import java.util.HashMap;
import java.util.Map;

import net.glowstone.constants.GlowGameRule;

public final class GameRuleManager {

    private final Map<String, GameRuleValue> gameRules = new HashMap<>();

    public GameRuleManager() {
        set(GlowGameRule.COMMAND_BLOCK_OUTPUT, true);
        set(GlowGameRule.DO_DAYLIGHT_CYCLE, true);
        set(GlowGameRule.DO_ENTITY_DROPS, true);
        set(GlowGameRule.DO_FIRE_TICK, true);
        set(GlowGameRule.DO_MOB_LOOT, true);
        set(GlowGameRule.DO_MOB_SPAWNING, true);
        set(GlowGameRule.DO_TILE_DROPS, true);
        set(GlowGameRule.KEEP_INVENTORY, false);
        set(GlowGameRule.LOG_ADMIN_COMMANDS, true);
        set(GlowGameRule.MOB_GRIEFING, true);
        set(GlowGameRule.NATURAL_REGENERATION, true);
        set(GlowGameRule.RANDOM_TICK_SPEED, 3);
        set(GlowGameRule.REDUCED_DEBUG_INFO, false);
        set(GlowGameRule.SEND_COMMAND_FEEDBACK, true);
        set(GlowGameRule.SHOW_DEATH_MESSAGES, true);
    }

    public String[] getGameRules() {
        return gameRules.keySet().toArray(new String[gameRules.size()]);
    }

    public String getGameRuleValue(String rule) {
        if (rule != null && gameRules.containsKey(rule)) {
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
        return gameRules.containsKey(rule);
    }

    public boolean getGameRuleBooleanValue(GlowGameRule rule) {
        if (isGameRule(rule.toString())) {
            final GameRuleValue ruleValue = gameRules.get(rule.toString());
            if (ruleValue.getType() == GameRuleValueType.BOOLEAN) {
                return Boolean.parseBoolean(ruleValue.getValue());
            } else {
                throw new IllegalArgumentException("This GlowGameRule isn't BOOLEAN");
            }
        }
        return false;
    }

    public int getGameRuleNumericValue(GlowGameRule rule) {
        if (isGameRule(rule.toString())) {
            final GameRuleValue ruleValue = gameRules.get(rule.toString());
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

    private void set(GlowGameRule rule, boolean value) {
        gameRules.put(rule.toString(), new GameRuleValue(GameRuleValueType.BOOLEAN, Boolean.toString(value)));
    }

    private void set(GlowGameRule rule, int value) {
        gameRules.put(rule.toString(), new GameRuleValue(GameRuleValueType.NUMERIC, Integer.toString(value)));
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
