package net.glowstone.io.nbt;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.io.WorldMetadataService;
import net.glowstone.util.nbt.ByteTag;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.DoubleTag;
import net.glowstone.util.nbt.FloatTag;
import net.glowstone.util.nbt.IntTag;
import net.glowstone.util.nbt.ListTag;
import net.glowstone.util.nbt.LongTag;
import net.glowstone.util.nbt.NBTInputStream;
import net.glowstone.util.nbt.NBTOutputStream;
import net.glowstone.util.nbt.ShortTag;
import net.glowstone.util.nbt.StringTag;
import net.glowstone.util.nbt.Tag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class NbtWorldMetadataService implements WorldMetadataService {
    private final GlowWorld world;
    private final File dir;
    private final GlowServer server;
    private Map<String, Tag> unknownTags = new HashMap<String, Tag>();

    public NbtWorldMetadataService(GlowWorld world, File dir) {
        this.world = world;
        if (!dir.exists())
            dir.mkdirs();
        this.dir = dir;
        server = (GlowServer) Bukkit.getServer();
    }

    public Map<WorldData, Object> readWorldData() throws IOException {
        Map<String, Tag> level = new HashMap<String, Tag>();
        Map<WorldData, Object> ret = new HashMap<WorldData, Object>();

        File levelFile = new File(dir, "level.dat");
        if (!levelFile.exists()) {
            try {
                levelFile.createNewFile();
            } catch (IOException e) {
                handleWorldException("level.dat", e);
            }
        } else {
            try {
                NBTInputStream in = new NBTInputStream(new FileInputStream(levelFile));
                CompoundTag levelTag = (CompoundTag) in.readTag();
                in.close();
                if (levelTag != null) level.putAll(levelTag.getValue());
            } catch (EOFException e) {
            } catch (IOException e) {
                handleWorldException("level.dat", e);
            }
        }

        File uuidFile = new File(dir, "uid.dat");
        if (!uuidFile.exists()) {
            try {
                uuidFile.createNewFile();
                ret.put(WorldData.UUID, UUID.randomUUID());
            } catch (IOException e) {
                handleWorldException("uid.dat", e);
            }
        } else {
            DataInputStream str = null;
            try {
            str = new DataInputStream(new FileInputStream(uuidFile));
            ret.put(WorldData.UUID, new UUID(str.readLong(), str.readLong()));
            } catch (EOFException e) {
            } finally {
                if (str != null) {
                    str.close();
                }
            }
        }


        if (level.containsKey("thundering")) {
            ByteTag thunderTag = (ByteTag) level.remove("thundering");
            world.setThundering(thunderTag.getValue() == 1);
        } else {
            ret.put(WorldData.THUNDERING, false);
        }
        if (level.containsKey("raining")) {
            ByteTag rainTag = (ByteTag) level.remove("raining");
            ret.put(WorldData.RAINING, Boolean.parseBoolean(rainTag.getValue().toString()));

        } else {
            ret.put(WorldData.RAINING, false);
        }
        if (level.containsKey("thunderTime")) {
            IntTag thunderTimeTag = (IntTag) level.remove("thunderTime");
            ret.put(WorldData.THUNDER_TIME, thunderTimeTag.getValue());
        } else {
            ret.put(WorldData.THUNDER_TIME, 0);
        }
        if (level.containsKey("rainTime")) {
            IntTag rainTimeTag = (IntTag) level.remove("rainTime");
            ret.put(WorldData.RAIN_TIME, rainTimeTag.getValue());
        } else {
            ret.put(WorldData.RAIN_TIME, 0);
        }
        if (level.containsKey("seed")) {
            LongTag seedTag = (LongTag) level.remove("seed");
            ret.put(WorldData.SEED, seedTag.getValue());
        } else {
            ret.put(WorldData.SEED, 0L);
        }
        if (level.containsKey("Time")) {
            LongTag timeTag = (LongTag) level.remove("Time");
            ret.put(WorldData.TIME, timeTag.getValue());
        } else {
            ret.put(WorldData.TIME, 0L);
        }
        if (level.containsKey("SpawnX") && level.containsKey("SpawnY") && level.containsKey("SpawnZ")) {
            IntTag spawnXTag = (IntTag) level.remove("SpawnX");
            IntTag spawnYTag = (IntTag) level.remove("SpawnY");
            IntTag spawnZTag = (IntTag) level.remove("SpawnZ");
            ret.put(WorldData.SPAWN_LOCATION, new Location(world, spawnXTag.getValue(), spawnYTag.getValue(), spawnZTag.getValue()));
        }
        unknownTags.putAll(level);
        return ret;
    }

    private void handleWorldException(String file, IOException e) {
        server.unloadWorld(world, false);
        server.getLogger().severe("Unable to access " + file + " for world " + world.getName());
        e.printStackTrace();
    }

    public void writeWorldData() throws IOException {
        Map<String, Tag> out = new HashMap<String, Tag>();
        File uuidFile = new File(dir, "uid.dat");
        if (!uuidFile.exists()) {
            try {
                uuidFile.createNewFile();
            } catch (IOException e) {
                handleWorldException("uid.dat", e);
            }
        } else {
            UUID uuid = world.getUID();
            DataOutputStream str = new DataOutputStream(new FileOutputStream(uuidFile));
            str.writeLong(uuid.getLeastSignificantBits());
            str.writeLong(uuid.getMostSignificantBits());
            str.close();
        }
        out.putAll(unknownTags);
        unknownTags.clear();
        // Normal level data
        out.put("thundering", new ByteTag("thundering", (byte) (world.isThundering() ? 1 : 0)));
        out.put("RandomSeed", new LongTag("RandomSeed", world.getSeed()));
        out.put("Time", new LongTag("Time", world.getTime()));
        out.put("raining", new ByteTag("raining", (byte) (world.hasStorm() ? 1 : 0)));
        out.put("thunderTime", new IntTag("thunderTime", world.getThunderDuration()));
        out.put("rainTime", new IntTag("rainTime", world.getWeatherDuration()));
        Location loc = world.getSpawnLocation();
        out.put("SpawnX", new IntTag("SpawnX", loc.getBlockX()));
        out.put("SpawnY", new IntTag("SpawnY", loc.getBlockY()));
        out.put("SpawnZ", new IntTag("SpawnZ", loc.getBlockZ()));
        // Format-specific
        out.put("LevelName", new StringTag("LevelName", world.getName()));
        out.put("LastPlayed", new LongTag("LastPlayed", Calendar.getInstance().getTimeInMillis()));
        out.put("version", new IntTag("version", 19132));

        if (!out.containsKey("SizeOnDisk"))
            out.put("SizeOnDisk", new LongTag("SizeOnDisk", 0)); // Not sure how to calculate this, so ignoring for now
        try {
            NBTOutputStream nbtOut = new NBTOutputStream(new FileOutputStream(new File(dir, "level.dat")));
            nbtOut.writeTag(new CompoundTag("Data", out));
            nbtOut.close();
        } catch (IOException e) {
            handleWorldException("level.dat", e);
        }
    }

    public Map<PlayerData, Object> readPlayerData(GlowPlayer player) {
        Map<String, Tag> playerData = new HashMap<String, Tag>();
        Map<PlayerData, Object> ret = new HashMap<PlayerData, Object>();

        File playerDir = new File(world.getName(), "players");
        if (!playerDir.exists())
            playerDir.mkdirs();

        File playerFile = new File(playerDir, player.getName() + ".dat");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                NBTInputStream in = new NBTInputStream(new FileInputStream(playerFile));
                CompoundTag playerTag = (CompoundTag) in.readTag();
                in.close();
                if (playerTag != null) playerData.putAll(playerTag.getValue());
            } catch (EOFException e) {
            } catch (IOException e) {
                player.kickPlayer("Failed to read " + player.getName() + ".dat!");
                server.getLogger().severe("Failed to read player.dat for player " + player.getName() + " in world " + world.getName() + "!");
                e.printStackTrace();
            }
        }
        
        World playerWorld = null;
        if (playerData.containsKey("UUIDLeast") && playerData.containsKey("UUIDMost")) {
            LongTag uuidLeastTag = (LongTag) playerData.get("UUIDLeast");
            LongTag uuidMostTag = (LongTag) playerData.get("UUIDMost");
            playerWorld = server.getWorld(new UUID(uuidLeastTag.getValue(), uuidMostTag.getValue()));
        }
        if (playerWorld == null && playerData.containsKey("World")) {
            StringTag worldTag = (StringTag) playerData.get("World");
            playerWorld = server.getWorld(worldTag.getValue());
        }
        if (playerWorld == null  && playerData.containsKey("Dimension")) {
            IntTag dimensionTag = (IntTag) playerData.get("Dimension");
            int dim = dimensionTag.getValue();
            for (World world : server.getWorlds()) {
                if (world.getEnvironment().getId() == dim)
                    playerWorld = world;
            }
        }
        if (playerWorld == null) {
            playerWorld = world;
        }
        if (playerData.containsKey("SleepTimer")) {
            ShortTag sleepTimerTag = (ShortTag) playerData.get("SleepTimer");
            ret.put(PlayerData.SLEEP_TICKS, sleepTimerTag.getValue());
        } else {
            ret.put(PlayerData.SLEEP_TICKS, (short) 0);
        }
        if (playerData.containsKey("HurtTime")) {
            ShortTag hurtTimeTag = (ShortTag) playerData.get("HurtTime");
            ret.put(PlayerData.HURT_TICKS, hurtTimeTag.getValue());
        } else {
            ret.put(PlayerData.HURT_TICKS, (short) 0);
        }
        if (playerData.containsKey("Health")) {
            player.setHealth(((ShortTag) playerData.get("Health")).getValue());
        }
        if (playerData.containsKey("Air")) {
            ShortTag airTag = (ShortTag) playerData.get("Air");
            // player.setRemainingAir(airTag.getValue());
        }
        if (playerData.containsKey("Fire")) {
            ShortTag fireTimeTag = (ShortTag) playerData.get("Fire");
            // player.setFireTicks(fireTimeTag.getValue());
        }
        if (playerData.containsKey("AttackTime")) {
            ShortTag attackTimeTag = (ShortTag) playerData.get("AttackTime");
            ret.put(PlayerData.ATTACK_TICKS, attackTimeTag.getValue());
        } else {
            ret.put(PlayerData.ATTACK_TICKS, (short) 0);
        }
        if (playerData.containsKey("DeathTime")) {
            ShortTag deathTimeTag = (ShortTag) playerData.get("DeathTime");
            ret.put(PlayerData.DEATH_TICKS, deathTimeTag.getValue());
        } else {
            ret.put(PlayerData.DEATH_TICKS, (short) 0);
        }
        if (playerData.containsKey("FallDistance")) {
            FloatTag fallDistanceTag = (FloatTag) playerData.get("FallDistance");
            // player.setFallDistance(fallDistanceTag.getValue());
        }
        if (playerData.containsKey("Pos") && playerData.containsKey("Rotation")) {
            ListTag posTag = (ListTag) playerData.get("Pos");
            ListTag rotTag = (ListTag) playerData.get("Rotation");
            player.teleport(NbtFormattingUtils.listTagsToLocation(playerWorld, posTag, rotTag));
        } else {
            player.teleport(playerWorld.getSpawnLocation());
        }
        if (playerData.containsKey("Inventory")) {
            ListTag<CompoundTag> inventoryTag = (ListTag<CompoundTag>) playerData.get("Inventory");
            player.getInventory().setContents(NbtFormattingUtils.tagToInventory(inventoryTag, player.getInventory().getSize()));
        }
        if (playerData.containsKey("Motion")) {
            ListTag<DoubleTag> motionTag = (ListTag<DoubleTag>) playerData.get("Motion");
            // player.setVelocity(NbtFormattingUtils.listTagToVector(motionTag));
        }
        if (playerData.containsKey("OnGround")) {
            ByteTag onGroundTag = (ByteTag) playerData.get("OnGround");
            player.setOnGround(onGroundTag.getValue() == 1);
        }
        if (playerData.containsKey("Sleeping")) {
            ByteTag isSleepingTag = (ByteTag) playerData.get("Sleeping");
            ret.put(PlayerData.IS_SLEEPING, isSleepingTag.getValue() == 1);
        }
        if (playerData.containsKey("SpawnX") && playerData.containsKey("SpawnY") && playerData.containsKey("SpawnZ")) {
            IntTag spawnXTag = (IntTag) playerData.get("SpawnX");
            IntTag spawnYTag = (IntTag) playerData.get("SpawnY");
            IntTag spawnZTag = (IntTag) playerData.get("SpawnZ");
            ret.put(PlayerData.BED_LOCATION, new Location(world, spawnXTag.getValue(), spawnYTag.getValue(), spawnZTag.getValue()));
        }
        return ret;
    }

    public void writePlayerData(GlowPlayer player, Map<PlayerData, Object> data) {
        Map<String, Tag> out = new HashMap<String, Tag>();

        File playerDir = new File(world.getName(), "players");
        if (!playerDir.exists())
            playerDir.mkdirs();

        File playerFile = new File(playerDir, player.getName() + ".dat");
        if (!playerFile.exists()) try {
            playerFile.createNewFile();
        } catch (IOException e) {
            player.getSession().disconnect("Failed to access player.dat");
            server.getLogger().severe("Failed to access player.dat for player " + player.getName() + " in world " + world.getName() + "!");
        }

        Location loc = player.getLocation();
        out.putAll(NbtFormattingUtils.locationToListTags(loc));
        UUID worldUUID = loc.getWorld().getUID();
        out.put("UUIDLeast", new LongTag("UUIDLeast", worldUUID.getLeastSignificantBits()));
        out.put("UUIDMost", new LongTag("UUIDMost", worldUUID.getMostSignificantBits()));
        out.put("World", new StringTag("world", loc.getWorld().getName()));
        out.put("Dimension", new IntTag("Dimension", loc.getWorld().getEnvironment().getId()));
        out.putAll(NbtFormattingUtils.locationToListTags(loc));
        out.put("SleepTimer", new ShortTag("SleepTimer", (short) player.getSleepTicks()));
        // out.put("HurtTime", new ShortTag("HurtTime", (short) 0)); // NYI
        out.put("Health", new ShortTag("Health", (short) player.getHealth()));
        // out.put("Air", new ShortTag("Air", (short) player.getRemainingAir()));
        // out.put("Fire", new ShortTag("Fire", (short) player.getFireTicks()));
        if (data.containsKey(PlayerData.ATTACK_TICKS)) {
            out.put("AttackTime", new ShortTag("AttackTime", (Short) data.get(PlayerData.ATTACK_TICKS)));
        } else {
            out.put("AttackTime", new ShortTag("AttackTime", (short) 0));
        }
        if (data.containsKey(PlayerData.DEATH_TICKS)) {
            out.put("DeathTime", new ShortTag("DeathTime", (Short) data.get(PlayerData.DEATH_TICKS)));
        } else {
            out.put("DeathTime", new ShortTag("DeathTime", (short) 0));
        }
        // out.put("FallDistance", new FloatTag("FallDistance", player.getFallDistance()));
        out.put("Inventory", NbtFormattingUtils.inventoryToTag(player.getInventory().getContents()));
        out.put("Motion", NbtFormattingUtils.vectorToListTag(player.getVelocity()));
        out.put("OnGround", new ByteTag("OnGround", (byte) (player.isOnGround() ? 1 : 0)));
        out.put("Sleeping", new ByteTag("Sleeping", (byte) (player.isSleeping() ? 1 : 0)));
        Location bedLoc = player.getBedSpawnLocation();
        if (bedLoc != null) {
            out.put("SpawnX", new IntTag("SpawnX", bedLoc.getBlockX()));
            out.put("SpawnY", new IntTag("SpawnY", bedLoc.getBlockY()));
            out.put("SpawnZ", new IntTag("SpawnZ", bedLoc.getBlockZ()));
        }
        try {
            NBTOutputStream outStream = new NBTOutputStream(new FileOutputStream(playerFile));
            outStream.writeTag(new CompoundTag("", out));
            outStream.close();
        } catch (IOException e) {
            player.getSession().disconnect("Failed to write player.dat", true);
            server.getLogger().severe("Failed to write player.dat for player " + player.getName() + " in world " + world.getName() + "!");
        }
    }
}
