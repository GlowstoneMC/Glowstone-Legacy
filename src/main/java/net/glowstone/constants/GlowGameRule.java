package net.glowstone.constants;

public enum GlowGameRule {
    COMMAND_BLOCK_OUTPUT("commandBlockOutput"),
    DO_DAYLIGHT_CYCLE("doDaylightCycle"),
    DO_ENTITY_DROPS("doEntityDrops"),
    DO_FIRE_TICK("doFireTick"),
    DO_MOB_LOOT("doMobLoot"),
    DO_MOB_SPAWNING("doMobSpawning"),
    DO_TILE_DROPS("doTileDrops"),
    KEEP_INVENTORY("keepInventory"),
    LOG_ADMIN_COMMANDS("logAdminCommands"),
    MOB_GRIEFING("mobGriefing"),
    NATURAL_REGENERATION("naturalRegeneration"),
    RANDOM_TICK_SPEED("randomTickSpeed"),
    REDUCED_DEBUG_INFO("reducedDebugInfo"),
    SEND_COMMAND_FEEDBACK("sendCommandFeedback"),
    SHOW_DEATH_MESSAGES("showDeathMessages");

    private final String name;

    private GlowGameRule(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
