package net.glowstone.io;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.glowstone.io.region.RegionFile;
import net.glowstone.io.region.RegionFileCache;
import net.glowstone.GlowChunk;
import net.glowstone.GlowWorld;
import net.glowstone.util.nbt.*;
import org.bukkit.Location;

/**
 * An implementation of the {@link ChunkIoService} which reads and writes
 * McRegion maps.
 * <p />
 * Information on the McRegion file format can be found on the
 * <a href="http://mojang.com/2011/02/16/minecraft-save-file-format-in-beta-1-3">Mojang</a>
 * blog.
 * @author Zhuowei Zhang
 * @author Graham Edgecombe
 */
public final class McRegionChunkIoService implements ChunkIoService {

    /**
     * The size of a region - a 32x32 group of chunks.
     */
    private static final int REGION_SIZE = 32;

    /**
     * The root directory of the map.
     */
    private File dir;

    /**
     * The region file cache.
     */
    private RegionFileCache cache = new RegionFileCache();

    // TODO: consider the session.lock file

    public McRegionChunkIoService() {
        this(new File("world"));
    }

    public McRegionChunkIoService(File dir) {
        this.dir = dir;
    }

    @Override
    public GlowChunk read(GlowWorld world, int x, int z) throws IOException {
        RegionFile region = cache.getRegionFile(dir, x, z);
        int regionX = x & (REGION_SIZE - 1);
        int regionZ = z & (REGION_SIZE - 1);
        if (!region.hasChunk(regionX, regionZ)){
            return null;
        }

        DataInputStream in = region.getChunkDataInputStream(regionX, regionZ);
        GlowChunk chunk = new GlowChunk(world, x, z);

        NBTInputStream nbt = new NBTInputStream(in, false);
        CompoundTag tag = (CompoundTag) nbt.readTag();
        Map<String, Tag> levelTags = ((CompoundTag) tag.getValue().get("Level")).getValue();

        byte[] tileData = ((ByteArrayTag) levelTags.get("Blocks")).getValue();
        chunk.setTypes(tileData);

        byte[] skyLightData = ((ByteArrayTag) levelTags.get("SkyLight")).getValue();
        byte[] blockLightData = ((ByteArrayTag) levelTags.get("BlockLight")).getValue();
        byte[] metaData = ((ByteArrayTag) levelTags.get("Data")).getValue();

        for (int cx = 0; cx < GlowChunk.WIDTH; cx++) {
            for (int cz = 0; cz < GlowChunk.HEIGHT; cz++) {
                for (int cy = 0; cy < GlowChunk.DEPTH; cy++) {
                    boolean mostSignificantNibble = ((cx * GlowChunk.HEIGHT + cz) * GlowChunk.DEPTH + cy) % 2 == 1;
                    int offset = ((cx * GlowChunk.HEIGHT + cz) * GlowChunk.DEPTH + cy) / 2;

                    int skyLight, blockLight, meta;
                    if (mostSignificantNibble) {
                        skyLight = (skyLightData[offset] & 0xF0) >> 4;
                        blockLight = (blockLightData[offset] & 0xF0) >> 4;
                        meta = (metaData[offset] & 0xF0) >> 4;
                    } else {
                        skyLight = skyLightData[offset] & 0x0F;
                        blockLight = blockLightData[offset] & 0x0F;
                        meta = metaData[offset] & 0x0F;
                    }

                    chunk.setSkyLight(cx, cz, cy, skyLight);
                    chunk.setBlockLight(cx, cz, cy, blockLight);
                    chunk.setMetaData(cx, cz, cy, meta);
                }
            }
        }

        return chunk;
    }

    @Override
    public void write(int x, int z, GlowChunk chunk) throws IOException {
        RegionFile region = cache.getRegionFile(dir, x, z);
        int regionX = x & (REGION_SIZE - 1);
        int regionZ = z & (REGION_SIZE - 1);

        DataOutputStream out = region.getChunkDataOutputStream(regionX, regionZ);


        NBTOutputStream nbt = new NBTOutputStream(out, false);
        Map<String, Tag> levelTags = new HashMap<String, Tag>();

        levelTags.put("Blocks", new ByteArrayTag("Blocks", chunk.getTypes()));

        byte[] skyLightData = new byte[GlowChunk.DEPTH * GlowChunk.WIDTH * GlowChunk.HEIGHT];
        byte[] blockLightData = new byte[GlowChunk.DEPTH * GlowChunk.WIDTH * GlowChunk.HEIGHT];
        byte[] metaData = new byte[GlowChunk.DEPTH * GlowChunk.WIDTH * GlowChunk.HEIGHT];
        byte[] heightMap = new byte[GlowChunk.WIDTH * GlowChunk.HEIGHT];

        for (int cx = 0; cx < GlowChunk.WIDTH; cx++) {
            for (int cz = 0; cz < GlowChunk.HEIGHT; cz++) {
                for (int cy = 0; cy < GlowChunk.DEPTH; cy++) {
                    boolean mostSignificantNibble = ((cx * GlowChunk.HEIGHT + cz) * GlowChunk.DEPTH + cy) % 2 == 1;
                    int offset = ((cx * GlowChunk.HEIGHT + cz) * GlowChunk.DEPTH + cy) / 2;
                    if (mostSignificantNibble) {
                        skyLightData[offset] = (byte)(chunk.getSkyLight(cx, cz, cy) << 4);
                        blockLightData[offset] = (byte)(chunk.getBlockLight(cx, cz, cy) << 4);
                        metaData[offset] = (byte)(chunk.getMetaData(cx, cz, cy) << 4);
                    } else {
                        skyLightData[offset] = (byte)chunk.getSkyLight(cx, cz, cy);
                        blockLightData[offset] = (byte)chunk.getBlockLight(cx, cz, cy);
                        metaData[offset] = (byte)chunk.getMetaData(cx, cz, cy);
                    }

                }
            }
        }

        for (int cx = 0; cx < GlowChunk.WIDTH; cx++) {
            for (int cz = 0; cz < GlowChunk.HEIGHT; cz++) {
                int offset = (cx * GlowChunk.HEIGHT + cz) / 2;
                heightMap[offset] = 0;
            }
        }
        levelTags.put("xPos", new IntTag("xPos", x));
        levelTags.put("zPos", new IntTag("zPos", z));
        levelTags.put("SkyLight", new ByteArrayTag("SkyLight", skyLightData));
        levelTags.put("BlockLight", new ByteArrayTag("BlockLight", blockLightData));
        levelTags.put("Data", new ByteArrayTag("Data", metaData));
        Map<String, Tag> levelOut = new HashMap<String, Tag>();
        levelOut.put("Level", new CompoundTag("Level", levelTags));
        nbt.writeTag(new CompoundTag("", levelOut));
        nbt.close();
        out.close();

    }

    @Override
    public Map<WorldData, Object> readWorldData(GlowWorld world) {
        Map<String, Tag> level = new HashMap<String, Tag>();
        Map<WorldData, Object> ret = new HashMap<WorldData, Object>();
        File levelDir = new File(world.getName());
        if (!levelDir.exists()) levelDir.mkdirs();
        File levelFile = new File(levelDir, "level.dat");
        if (!levelFile.exists()) try {
            levelFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            NBTInputStream in = new NBTInputStream(new FileInputStream(levelFile));
            CompoundTag levelTag = (CompoundTag)in.readTag();
            in.close();
            if (levelTag != null) level.putAll(levelTag.getValue());
        } catch (IOException e) {
        }
        if (!level.isEmpty()) {
            ByteTag thunderTag = (ByteTag)level.get("thundering");
            ByteTag rainTag = (ByteTag)level.get("raining");
            IntTag thunderTimeTag = (IntTag)level.get("thunderTime");
            IntTag rainTimeTag = (IntTag)level.get("rainTime");
            LongTag seedTag = (LongTag)level.get("seed");
            LongTag timeTag = (LongTag)level.get("Time");
            IntTag spawnXTag = (IntTag)level.get("SpawnX");
            IntTag spawnYTag = (IntTag)level.get("SpawnY");
            IntTag spawnZTag = (IntTag)level.get("SpawnZ");

            ret.put(WorldData.SEED , (seedTag == null) ? 0L : seedTag.getValue());
            ret.put(WorldData.SPAWN_LOCATION, new Location(world, spawnXTag.getValue(), spawnYTag.getValue(), spawnZTag.getValue()));
            ret.put(WorldData.TIME, timeTag.getValue());
            ret.put(WorldData.RAINING, Boolean.parseBoolean(rainTag.getValue().toString()));
            ret.put(WorldData.THUNDERING, Boolean.parseBoolean(thunderTag.getValue().toString()));
            ret.put(WorldData.RAIN_TIME, rainTimeTag.getValue());
            ret.put(WorldData.THUNDER_TIME, thunderTimeTag.getValue());
        }
        return ret;
    }

    @Override
    public void writeWorldData(GlowWorld world) {
        try {
            Map<String, Tag> out = new HashMap<String, Tag>();
            NBTOutputStream nbtOut= new NBTOutputStream(new FileOutputStream(new File(world.getName() + File.separator + "level.dat")));
            out.put("thundering", new ByteTag("thundering", (byte)(world.isThundering() ? 1 : 0)));
            out.put("LastPlayed", new LongTag("LastPlayed", Calendar.getInstance().getTimeInMillis()));
            out.put("RandomSeed", new LongTag("RandomSeed", world.getSeed()));
            out.put("version", new IntTag("version", 19132));
            out.put("Time", new LongTag("Time", world.getTime()));
            out.put("raining", new ByteTag("raining", (byte)(world.hasStorm() ? 1 : 0)));
            out.put("SpawnX", new IntTag("SpawnX", (int)world.getSpawnLocation().getX()));
            out.put("thunderTime", new IntTag("thunderTime", world.getThunderDuration()));
            out.put("SpawnY", new IntTag("SpawnY", (int)world.getSpawnLocation().getY()));
            out.put("SpawnZ", new IntTag("SpawnZ", (int)world.getSpawnLocation().getZ()));
            out.put("LevelName", new StringTag("LevelName", world.getName()));
            if (!out.containsKey("SizeOnDisk")) out.put("SizeOnDisk", new LongTag("SizeOnDisk", 0));
            out.put("rainTime", new IntTag("rainTime", world.getWeatherDuration()));


            nbtOut.writeTag(new CompoundTag("Data", out));
            nbtOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
