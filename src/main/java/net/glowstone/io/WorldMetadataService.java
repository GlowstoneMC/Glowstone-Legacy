package net.glowstone.io;

import net.glowstone.entity.GlowPlayer;

import java.io.IOException;
import java.util.Map;


public interface WorldMetadataService {

    /**
     * Reads the data from a chunk's world
     * @return a map with world information
     * @throws IOException if an I/O error occurs
     */
    public Map<WorldData, Object> readWorldData() throws IOException;

    /**
     * Writes data for a chunk's world
     * @throws IOException in the event of unanticipated error
     */
    public void writeWorldData() throws IOException;


    public enum WorldData {
        SEED,
        UUID,
        SPAWN_LOCATION,
        TIME,
        RAINING,
        THUNDERING,
        RAIN_TIME,
        THUNDER_TIME
    }

    /**
     * Read  player's data from their storage file
     * @param player The player to fetch data for
     * @return a Map with the player's data
     * @throws IOException in the event of unanticipated error
     */
    public Map<PlayerData, Object> readPlayerData(GlowPlayer player);

    /**
     * Write a player's data to their storage file
     * @param player The player to save data for
     * @param data The data to save for the player
     */
    public void writePlayerData(GlowPlayer player, Map<PlayerData, Object> data);


    public enum PlayerData {
        LOCATION,
        INVENTORY,
        SLEEP_TICKS,
        MOTION,
        ON_GROUND,
        HEALTH,
        AIR_TICKS,
        IS_SLEEPING,
        FIRE_TICKS,
        FALL_DISTANCE,
        DEATH_TICKS,
        HURT_TICKS,
        ATTACK_TICKS,
        BED_LOCATION
    }
}
