package net.glowstone.io;

import java.io.IOException;
import java.util.Map;

import net.glowstone.GlowChunk;
import net.glowstone.GlowWorld;
import org.bukkit.Location;

/**
 * This interface should be implemented by classes which wish to provide some
 * way of performing chunk I/O e.g. the {@link NbtChunkIoService}. This
 * interface is abstracted away from the implementation because a new format is
 * due to arrive soon (McRegion).
 * @author Graham Edgecombe
 */
public interface ChunkIoService {

    /**
     * Reads a single chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @return The {@link GlowChunk} or {@code null} if it does not exist.
     * @throws IOException if an I/O error occurs.
     */
    public GlowChunk read(GlowWorld world, int x, int z) throws IOException;

    /**
     * Writes a single chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param chunk The {@link GlowChunk}.
     * @throws IOException if an I/O error occurs.
     */
    public void write(int x, int z, GlowChunk chunk) throws IOException;

    /**
     * Reads the data from a chunk's world
     * @param world A chunk in the world to get information from
     * @return a map with world information
     * @throws IOException if an I/O error occurs
     */
    public Map<WorldData, Object> readWorldData(GlowWorld world) throws IOException;

    /**
     * Writes data for a chunk's world
     * @param world The world
     * @throws IOException
     */
    public void writeWorldData(GlowWorld world) throws IOException;


    public enum WorldData {
        SEED(Long.class),
        SPAWN_LOCATION(Location.class),
        TIME(Long.class),
        RAINING(Boolean.class),
        THUNDERING(Boolean.class),
        RAIN_TIME(Integer.class),
        THUNDER_TIME(Integer.class);

        private final Class<?> clazz;
         WorldData(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getType() {
            return clazz;
        }

    }
}
