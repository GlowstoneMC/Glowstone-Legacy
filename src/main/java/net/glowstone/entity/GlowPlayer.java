package net.glowstone.entity;

import com.flowpowered.networking.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.glowstone.*;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.constants.GlowAchievement;
import net.glowstone.constants.GlowEffect;
import net.glowstone.constants.GlowSound;
import net.glowstone.entity.meta.MetadataIndex;
import net.glowstone.entity.meta.PlayerProperty;
import net.glowstone.inventory.InventoryMonitor;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.login.LoginSuccessMessage;
import net.glowstone.net.message.play.entity.DestroyEntitiesMessage;
import net.glowstone.net.message.play.game.*;
import net.glowstone.net.message.play.inv.*;
import net.glowstone.net.message.play.player.PlayerAbilitiesMessage;
import net.glowstone.net.protocol.PlayProtocol;
import net.glowstone.util.StatisticMap;
import net.glowstone.util.TextWrapper;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.scoreboard.Scoreboard;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;

/**
 * Represents an in-game player.
 * @author Graham Edgecombe
 */
@DelegateDeserialization(GlowOfflinePlayer.class)
public final class GlowPlayer extends GlowHumanEntity implements Player {

    /**
     * The normal height of a player's eyes above their feet.
     */
    public static final double EYE_HEIGHT = 1.62D;

    /**
     * This player's session.
     */
    private final GlowSession session;

    /**
     * This player's unique id.
     */
    private final UUID uuid;

    /**
     * The entities that the client knows about.
     */
    private final Set<GlowEntity> knownEntities = new HashSet<>();

    /**
     * The chunks that the client knows about.
     */
    private final Set<GlowChunk.Key> knownChunks = new HashSet<>();

    /**
     * A queue of BlockChangeMessages to be sent.
     */
    private final List<BlockChangeMessage> blockChanges = new LinkedList<>();

    /**
     * A queue of messages that should be sent after block changes are processed.
     * Used for sign updates and other situations where the block must be sent first.
     */
    private final List<Message> afterBlockChanges = new LinkedList<>();

    /**
     * The set of plugin channels this player is listening on
     */
    private final Set<String> listeningChannels = new HashSet<>();

    /**
     * The player's statistics, achievements, and related data.
     */
    private final StatisticMap stats = new StatisticMap();

    /**
     * The lock used to prevent chunks from unloading near the player.
     */
    private ChunkManager.ChunkLock chunkLock;

    /**
     * The tracker for changes to the currently open inventory.
     */
    private InventoryMonitor invMonitor;

    /**
     * The display name of this player, for chat purposes.
     */
    private String displayName;

    /**
     * The name a player has in the player list
     */
    private String playerListName;

    /**
     * Cumulative amount of experience points the player has collected.
     */
    private int experience = 0;

    /**
     * The current level (or skill point amount) of the player.
     */
    private int level = 0;

    /**
     * The human entity's current food level
     */
    private int food = 20;

    /**
     * The player's current exhaustion level.
     */
    private float exhaustion = 0;

    /**
     * The player's current saturation level.
     */
    private float saturation = 0;

    /**
     * Whether to perform special scaling of the player's health.
     */
    private boolean healthScaled = false;

    /**
     * The scale at which to display the player's health.
     */
    private double healthScale = 20;

    /**
     * This player's current time offset.
     */
    private long timeOffset = 0;

    /**
     * Whether the time offset is relative.
     */
    private boolean timeRelative = true;

    /**
     * The player-specific weather, or null for normal weather.
     */
    private WeatherType playerWeather = null;

    /**
     * The player's compass target.
     */
    private Location compassTarget;

    /**
     * Whether this player's sleeping state is ignored when changing time.
     */
    private boolean sleepingIgnored;

    /**
     * The bed spawn location of a player
     */
    private Location bedSpawn;

    /**
     * The location of the sign the player is currently editing, or null.
     */
    private Location signLocation;

    /**
     * Whether the player is permitted to fly.
     */
    private boolean canFly;

    /**
     * Whether the player is currently flying.
     */
    private boolean flying;

    /**
     * The player's base flight speed.
     */
    private float flySpeed = 0.1f;

    /**
     * The player's base walking speed.
     */
    private float walkSpeed = 0.2f;

    /**
     * Creates a new player and adds it to the world.
     * @param session The player's session.
     * @param name    The player's name.
     */
    public GlowPlayer(GlowSession session, String name, UUID uuid, List<PlayerProperty> properties) {
        super(session.getServer(), (GlowWorld) session.getServer().getWorlds().get(0), name, properties);
        this.session = session;
        this.uuid = uuid;

        chunkLock = world.newChunkLock(getName());

        // send login response
        session.send(new LoginSuccessMessage(uuid.toString(), name));
        session.setProtocol(new PlayProtocol(session.getServer()));

        // send join game
        // in future, handle hardcore, difficulty, and level type
        String type = world.getWorldType().getName().toLowerCase();
        int gameMode = getGameMode().getValue();
        if (server.isHardcore()) {
            gameMode |= 0x8;
        }
        session.send(new JoinGameMessage(getEntityId(), gameMode, world.getEnvironment().getId(), world.getDifficulty().getValue(), session.getServer().getMaxPlayers(), type));
        setAllowFlight(getGameMode() == GameMode.CREATIVE);

        // send server brand and supported plugin channels
        session.send(new PluginMessage("MC|Brand", server.getName()));
        sendSupportedChannels();

        loadData();
        saveData();

        streamBlocks(); // stream the initial set of blocks
        setCompassTarget(world.getSpawnLocation()); // set our compass target
        sendTime();
        sendWeather();
        sendAbilities();

        invMonitor = new InventoryMonitor(getOpenInventory());
        updateInventory(); // send inventory contents

        // send initial location
        double y = location.getY() + getEyeHeight() + 0.05;
        session.send(new PositionRotationMessage(location.getX(), y, location.getZ(), location.getYaw(), location.getPitch(), true));
    }

    @Override
    public String toString() {
        return "GlowPlayer{name=" + getName() + "}";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Internals

    /**
     * Get the network session attached to this player.
     * @return The GlowSession of the player.
     */
    public GlowSession getSession() {
        return session;
    }

    /**
     * Destroys this entity by removing it from the world and marking it as not
     * being active.
     */
    @Override
    public void remove() {
        knownChunks.clear();
        chunkLock.clear();
        saveData();
        getInventory().removeViewer(this);
        getInventory().getCraftingInventory().removeViewer(this);
        permissions.clearPermissions();
        super.remove();
    }

    @Override
    public void pulse() {
        super.pulse();

        // stream world
        streamBlocks();
        processBlockChanges();

        // add to playtime
        incrementStatistic(Statistic.PLAY_ONE_TICK);

        // update inventory
        for (InventoryMonitor.Entry entry : invMonitor.getChanges()) {
            sendItemChange(entry.slot, entry.item);
        }

        // update or remove entities
        for (Iterator<GlowEntity> it = knownEntities.iterator(); it.hasNext(); ) {
            GlowEntity entity = it.next();
            boolean withinDistance = !entity.isDead() && isWithinDistance(entity);

            if (withinDistance) {
                for (Message msg : entity.createUpdateMessage()) {
                    session.send(msg);
                }
            } else {
                session.send(new DestroyEntitiesMessage(entity.getEntityId()));
                it.remove();
            }
        }

        // add entities
        for (GlowEntity entity : world.getEntityManager()) {
            if (entity == this)
                continue;
            boolean withinDistance = !entity.isDead() && isWithinDistance(entity);

            if (withinDistance && !knownEntities.contains(entity)) {
                knownEntities.add(entity);
                for (Message msg : entity.createSpawnMessage()) {
                    session.send(msg);
                }
            }
        }
    }

    /**
     * Process and send pending BlockChangeMessages.
     */
    private void processBlockChanges() {
        List<BlockChangeMessage> messages = new ArrayList<>(blockChanges);
        blockChanges.clear();

        // separate messages by chunk
        Map<GlowChunk.Key, List<BlockChangeMessage>> chunks = new HashMap<>();
        for (BlockChangeMessage message : messages) {
            GlowChunk.Key key = new GlowChunk.Key(message.getX() >> 4, message.getZ() >> 4);
            List<BlockChangeMessage> list = chunks.get(key);
            if (list == null) {
                list = new LinkedList<>();
                chunks.put(key, list);
            }
            list.add(message);
        }

        // send away
        for (Map.Entry<GlowChunk.Key, List<BlockChangeMessage>> entry : chunks.entrySet()) {
            GlowChunk.Key key = entry.getKey();
            List<BlockChangeMessage> value = entry.getValue();

            if (value.size() == 1) {
                session.send(value.get(0));
            } else if (value.size() > 1) {
                session.send(new MultiBlockChangeMessage(key.getX(), key.getZ(), value));
            }
        }

        // now send post-block-change messages
        List<Message> postMessages = new ArrayList<>(afterBlockChanges);
        afterBlockChanges.clear();
        for (Message message : postMessages) {
            session.send(message);
        }
    }

    /**
     * Streams chunks to the player's client.
     */
    private void streamBlocks() {
        Set<GlowChunk.Key> previousChunks = new HashSet<>(knownChunks);
        ArrayList<GlowChunk.Key> newChunks = new ArrayList<>();

        int centralX = location.getBlockX() >> 4;
        int centralZ = location.getBlockZ() >> 4;

        int radius = server.getViewDistance();
        for (int x = (centralX - radius); x <= (centralX + radius); x++) {
            for (int z = (centralZ - radius); z <= (centralZ + radius); z++) {
                GlowChunk.Key key = new GlowChunk.Key(x, z);
                if (knownChunks.contains(key)) {
                    previousChunks.remove(key);
                } else {
                    newChunks.add(key);
                }
            }
        }

        if (newChunks.size() == 0 && previousChunks.size() == 0) {
            return;
        }

        Collections.sort(newChunks, new Comparator<GlowChunk.Key>() {
            public int compare(GlowChunk.Key a, GlowChunk.Key b) {
                double dx = 16 * a.getX() + 8 - location.getX();
                double dz = 16 * a.getZ() + 8 - location.getZ();
                double da = dx * dx + dz * dz;
                dx = 16 * b.getX() + 8 - location.getX();
                dz = 16 * b.getZ() + 8 - location.getZ();
                double db = dx * dx + dz * dz;
                return Double.compare(da, db);
            }
        });

        List<GlowChunk> bulkChunks = null;
        if (newChunks.size() > knownChunks.size() * 2 / 5) {
            // send a bulk message
            bulkChunks = new LinkedList<>();
        }

        // populate then send chunks to the player

        // done in two steps so that all the new chunks are finalized before any of them are sent
        // this prevents sending a chunk then immediately sending block changes in it because
        // one of its neighbors has populated
        for (GlowChunk.Key key : newChunks) {
            world.getChunkManager().forcePopulation(key.getX(), key.getZ());
        }
        for (GlowChunk.Key key : newChunks) {
            GlowChunk chunk = world.getChunkAt(key.getX(), key.getZ());
            if (bulkChunks == null) {
                session.send(chunk.toMessage());
            } else {
                bulkChunks.add(chunk);
            }
            knownChunks.add(key);
            chunkLock.acquire(key);
        }

        if (bulkChunks != null) {
            boolean skylight = world.getEnvironment() == World.Environment.NORMAL;
            session.send(new ChunkBulkMessage(skylight, bulkChunks));
        }

        for (GlowChunk.Key key : newChunks) {
            GlowChunk chunk = world.getChunkAt(key.getX(), key.getZ());
            for (TileEntity entity : chunk.getRawTileEntities()) {
                entity.update(this);
            }
        }

        for (GlowChunk.Key key : previousChunks) {
            session.send(ChunkDataMessage.empty(key.getX(), key.getZ()));
            knownChunks.remove(key);
            chunkLock.release(key);
        }

        previousChunks.clear();
    }

    /**
     * Checks whether the player can see the given chunk.
     * @return If the chunk is known to the player's client.
     */
    public boolean canSee(GlowChunk.Key chunk) {
        return knownChunks.contains(chunk);
    }

    /**
     * Checks whether the player can see the given entity.
     * @return If the entity is known to the player's client.
     */
    public boolean canSee(GlowEntity entity) {
        return knownEntities.contains(entity);
    }

    /**
     * Open the sign editor interface at the specified location.
     * @param loc The location to open the editor at
     */
    public void openSignEditor(Location loc) {
        signLocation = loc.clone();
        signLocation.setX(loc.getBlockX());
        signLocation.setY(loc.getBlockY());
        signLocation.setZ(loc.getBlockZ());
        session.send(new SignEditorMessage(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    /**
     * Check that the specified location matches that of the last opened sign
     * editor, and if so, clears the last opened sign editor.
     * @param loc The location to check
     * @return Whether the location matched.
     */
    public boolean checkSignLocation(Location loc) {
        if (loc.equals(signLocation)) {
            signLocation = null;
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("name", getName());
        return ret;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Basic stuff

    public InetSocketAddress getAddress() {
        return session.getAddress();
    }

    public boolean isOnline() {
        return true;
    }

    public boolean isBanned() {
        return server.getBanList(BanList.Type.NAME).isBanned(getName());
    }

    @Deprecated
    public void setBanned(boolean banned) {
        server.getBanList(BanList.Type.NAME).addBan(getName(), null, null, null);
    }

    public boolean isWhitelisted() {
        return !server.hasWhitelist() || server.getWhitelist().contains(getName());
    }

    public void setWhitelisted(boolean value) {
        if (value) {
            server.getWhitelist().add(getName());
        } else {
            server.getWhitelist().remove(getName());
        }
    }

    public Player getPlayer() {
        return this;
    }

    public long getFirstPlayed() {
        return 0;
    }

    public long getLastPlayed() {
        return 0;
    }

    public boolean hasPlayedBefore() {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // HumanEntity overrides

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public boolean isOp() {
        return getServer().getOpsList().contains(getName());
    }

    @Override
    public void setOp(boolean value) {
        if (value) {
            getServer().getOpsList().add(getName());
        } else {
            getServer().getOpsList().remove(getName());
        }
        permissions.recalculatePermissions();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Editable properties

    public String getDisplayName() {
        return displayName == null ? getName() : displayName;
    }

    public void setDisplayName(String name) {
        displayName = name;
    }

    public String getPlayerListName() {
        return playerListName == null || "".equals(playerListName) ? getName() : playerListName;
    }

    public void setPlayerListName(String name) {
        if (name.length() > 16) {
            throw new IllegalArgumentException("The given name was " + name.length() + " chars long, longer than the maximum of 16");
        }
        for (Player player : server.getOnlinePlayers()) {
            if (player != this && player.getPlayerListName().equals(name)) {
                throw new IllegalArgumentException("The name given, " + name + ", is already used by " + player.getName() + ".");
            }
        }

        Message removeMessage = new UserListItemMessage(getPlayerListName(), false, 0);
        playerListName = name;
        Message reAddMessage = new UserListItemMessage(getPlayerListName(), true, 0);
        for (Player player : server.getOnlinePlayers()) {
            ((GlowPlayer) player).getSession().send(removeMessage);
            ((GlowPlayer) player).getSession().send(reAddMessage);
        }
    }

    public Location getCompassTarget() {
        return compassTarget;
    }

    public void setCompassTarget(Location loc) {
        compassTarget = loc;
        session.send(new SpawnPositionMessage(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
    }

    public Location getBedSpawnLocation() {
        return bedSpawn;
    }

    public void setBedSpawnLocation(Location bedSpawn) {
        setBedSpawnLocation(bedSpawn, false);
    }

    public void setBedSpawnLocation(Location location, boolean force) {
        this.bedSpawn = location;
    }

    public boolean isSleepingIgnored() {
        return sleepingIgnored;
    }

    public void setSleepingIgnored(boolean isSleeping) {
        sleepingIgnored = isSleeping;
    }

    @Override
    public void setGameMode(GameMode mode) {
        boolean changed = getGameMode() != mode;
        super.setGameMode(mode);
        if (changed) session.send(new StateChangeMessage(3, mode.getValue()));

        setAllowFlight(mode == GameMode.CREATIVE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Entity status

    public boolean isSneaking() {
        return (metadata.getByte(MetadataIndex.STATUS) & 0x02) != 0;
    }

    public void setSneaking(boolean sneak) {
        if (EventFactory.onPlayerToggleSneak(this, sneak).isCancelled()) {
            return;
        }

        if (sneak) {
            metadata.setBit(MetadataIndex.STATUS, 0x02);
        } else {
            metadata.clearBit(MetadataIndex.STATUS, 0x02);
        }

        updateMetadata();
    }

    public boolean isSprinting() {
        return metadata.getBit(MetadataIndex.STATUS, 0x08);
    }

    public void setSprinting(boolean sprinting) {
        // todo: event

        if (sprinting) {
            metadata.setBit(MetadataIndex.STATUS, 0x08);
        } else {
            metadata.clearBit(MetadataIndex.STATUS, 0x08);
        }

        updateMetadata();
    }

    public double getEyeHeight() {
        return getEyeHeight(false);
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        // Height of player's eyes above feet. Matches CraftBukkit.
        if (ignoreSneaking || !isSneaking()) {
            return 1.62;
        } else {
            return 1.54;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Player capabilities

    public boolean getAllowFlight() {
        return canFly;
    }

    public void setAllowFlight(boolean flight) {
        canFly = flight;
        if (!canFly) flying = false;
        sendAbilities();
    }

    public boolean isFlying() {
        return flying;
    }

    public void setFlying(boolean value) {
        flying = value && canFly;
        sendAbilities();
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public void setFlySpeed(float value) throws IllegalArgumentException {
        flySpeed = value;
        sendAbilities();
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }

    public void setWalkSpeed(float value) throws IllegalArgumentException {
        walkSpeed = value;
        sendAbilities();
    }

    private void sendAbilities() {
        boolean creative = getGameMode() == GameMode.CREATIVE;
        int flags = (creative ? 8 : 0) | (canFly ? 4 : 0) | (flying ? 2 : 0) | (creative ? 1 : 0);
        // division is conversion from Bukkit to MC units
        session.send(new PlayerAbilitiesMessage(flags, flySpeed / 2f, walkSpeed / 2f));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Experience and levelling
    // todo: most of the exp stuff is pretty broken

    public int getExperience() {
        return experience % ((getLevel() + 1) * 7);
    }

    public void setExperience(int exp) {
        setTotalExperience(experience - getExperience() + exp);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        experience = 0;
        for (this.level = 0; this.level < level; ++this.level) {
            experience += getExpToLevel();
        }
        sendExperience();
    }

    public int getTotalExperience() {
        return experience;
    }

    public void setTotalExperience(int exp) {
        int calcExperience = exp;
        this.experience = exp;
        level = 0;
        while ((calcExperience -= getExpToLevel()) > 0) ++level;
        sendExperience();
    }

    public void giveExp(int xp) {
        experience += xp;
        while (experience > (getLevel() + 1) * 7) {
            experience -= (getLevel() + 1) * 7;
            ++level;
        }
        sendExperience();
    }

    public float getExp() {
        return (float) experience / getExpToLevel();
    }

    public void setExp(float percentToLevel) {
        experience = (int) (percentToLevel * getExpToLevel());
    }

    @Override
    public int getExpToLevel() {
        return getExpToLevel(level);
    }

    private int getExpToLevel(int level) {
        if (level >= 30) {
            return 62 + (level - 30) * 7;
        } else if (level >= 15) {
            return 17 + (level - 15) * 3;
        } else {
            return 17;
        }
    }

    public void giveExpLevels(int amount) {

    }

    private void sendExperience() {
        session.send(new ExperienceMessage(getExp(), (byte) getLevel(), (short) getTotalExperience()));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Health and food handling

    @Override
    public void setHealth(double health) {
        super.setHealth(health);
        sendHealth();
    }

    public boolean isHealthScaled() {
        return healthScaled;
    }

    public void setHealthScaled(boolean scale) {
        healthScaled = scale;
        sendHealth();
    }

    public double getHealthScale() {
        return healthScale;
    }

    public void setHealthScale(double scale) throws IllegalArgumentException {
        healthScaled = true;
        healthScale = scale;
        sendHealth();
    }

    public int getFoodLevel() {
        return food;
    }

    public void setFoodLevel(int food) {
        this.food = Math.min(food, 20);
        sendHealth();
    }

    public float getExhaustion() {
        return exhaustion;
    }

    public void setExhaustion(float value) {
        exhaustion = value;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float value) {
        saturation = value;
        sendHealth();
    }

    private void sendHealth() {
        float finalHealth = (float) (getHealth() / getMaxHealth() * getHealthScale());
        session.send(new HealthMessage(finalHealth, getFoodLevel(), getSaturation()));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Actions

    /**
     * Teleport the player.
     * @param location The destination to teleport to.
     * @return Whether the teleport was a success.
     */
    @Override
    public boolean teleport(Location location) {
        return teleport(location, TeleportCause.UNKNOWN);
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        if (this.location != null && this.location.getWorld() != null) {
            PlayerTeleportEvent event = EventFactory.onPlayerTeleport(this, getLocation(), location, cause);
            if (event.isCancelled()) return false;
            location = event.getTo();
        }

        // account for floating point shenanigans in client physics
        double y = location.getY() + getEyeHeight() + 0.05;
        PositionRotationMessage message = new PositionRotationMessage(location.getX(), y, location.getZ(), location.getYaw(), location.getPitch(), true);

        if (location.getWorld() != world) {
            GlowWorld oldWorld = world;
            world.getEntityManager().deallocate(this);

            world = (GlowWorld) location.getWorld();
            world.getEntityManager().allocate(this);

            for (GlowChunk.Key key : knownChunks) {
                session.send(ChunkDataMessage.empty(key.getX(), key.getZ()));
            }
            knownChunks.clear();
            chunkLock.clear();
            chunkLock = world.newChunkLock(getName());

            String type = world.getWorldType().getName().toLowerCase();
            session.send(new RespawnMessage(world.getEnvironment().getId(), world.getDifficulty().getValue(), getGameMode().getValue(), type));
            streamBlocks(); // stream blocks

            setCompassTarget(world.getSpawnLocation()); // set our compass target
            this.session.send(message);
            this.location = location; // take us to spawn position
            sendWeather();
            reset();
            EventFactory.onPlayerChangedWorld(this, oldWorld);
        } else {
            this.session.send(message);
            this.location = location;
            reset();
        }

        return true;
    }

    public void sendMessage(String message) {
        sendRawMessage(message);
    }

    public void sendMessage(String[] messages) {
        for (String line : messages) {
            sendMessage(line);
        }
    }

    public void sendRawMessage(String message) {
        // todo: use chat components instead of plain text
        // textwrapper also does not preserve non-color formatting
        for (String line : TextWrapper.wrapText(message)) {
            session.send(new ChatMessage(line));
        }
    }

    public void kickPlayer(String message) {
        session.disconnect(message == null ? "" : message);
    }

    public boolean performCommand(String command) {
        return getServer().dispatchCommand(this, command);
    }

    public void chat(String text) {
        chat(text, false);
    }

    public void chat(String text, boolean async) {
        if (text.startsWith("/")) {
            server.getLogger().info(getName() + " issued command: " + text);
            try {
                PlayerCommandPreprocessEvent event = EventFactory.onPlayerCommand(this, text);
                if (event.isCancelled()) {
                    return;
                }
                getServer().dispatchCommand(event.getPlayer(), event.getMessage().substring(1));
            } catch (Exception ex) {
                sendMessage(ChatColor.RED + "An internal error occured while executing your command.");
                getServer().getLogger().log(Level.SEVERE, "Exception while executing command: " + text, ex);
            }
        } else {
            final String theText = text;
            final boolean isAsync = async;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    AsyncPlayerChatEvent event = EventFactory.onAsyncPlayerChat(isAsync, GlowPlayer.this, theText);
                    if (event.isCancelled()) {
                        return;
                    }

                    String message = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
                    getServer().getLogger().info(message);
                    for (Player recipient : event.getRecipients()) {
                        recipient.sendMessage(message);
                    }
                }
            };

            if (async)
                server.getScheduler().runTaskAsynchronously(null, task);
            else
                task.run();
        }
    }

    public void saveData() {
        saveData(true);
    }

    public void saveData(boolean async) {
        final GlowWorld dataWorld = (GlowWorld) server.getWorlds().get(0);

        if (async) {
            server.getScheduler().runTaskAsynchronously(null, new Runnable() {
                public void run() {
                    dataWorld.getMetadataService().writePlayerData(GlowPlayer.this);
                }
            });
        } else {
            dataWorld.getMetadataService().writePlayerData(this);
        }
    }

    public void loadData() {
        GlowWorld dataWorld = (GlowWorld) server.getWorlds().get(0);
        dataWorld.getMetadataService().readPlayerData(this);
    }

    @Deprecated
    public void setTexturePack(String url) {
        setResourcePack(url);
    }

    public void setResourcePack(String url) {
        session.send(new PluginMessage("MC|RPack", url));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Effect and data transmission

    public void playNote(Location loc, Instrument instrument, Note note) {
        playNote(loc, instrument.getType(), note.getId());
    }

    public void playNote(Location loc, byte instrument, byte note) {
        session.send(new BlockActionMessage(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), instrument, note, Material.NOTE_BLOCK.getId()));
    }

    public void playEffect(Location loc, Effect effect, int data) {
        int id = effect.getId();
        boolean ignoreDistance = id == 1013; // mob.wither.spawn, not in Bukkit yet
        session.send(new PlayEffectMessage(id, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), data, ignoreDistance));
    }

    public <T> void playEffect(Location loc, Effect effect, T data) {
        playEffect(loc, effect, GlowEffect.getDataValue(effect, data));
    }

    public void playSound(Location location, Sound sound, float volume, float pitch) {
        playSound(location, GlowSound.getName(sound), volume, pitch);
    }

    public void playSound(Location location, String sound, float volume, float pitch) {
        session.send(new PlaySoundMessage(sound, location.getBlockX(), location.getBlockY(), location.getBlockZ(), volume, pitch));
    }

    public void sendBlockChange(Location loc, Material material, byte data) {
        sendBlockChange(loc, material.getId(), data);
    }

    public void sendBlockChange(Location loc, int material, byte data) {
        sendBlockChange(new BlockChangeMessage(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), material, data));
    }

    public void sendBlockChange(BlockChangeMessage message) {
        // only send message if the chunk is within visible range
        GlowChunk.Key key = new GlowChunk.Key(message.getX() >> 4, message.getZ() >> 4);
        if (canSee(key)) {
            blockChanges.add(message);
        }
    }

    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendSignChange(Location location, String[] lines) throws IllegalArgumentException {
        Validate.notNull(location, "location cannot be null");
        Validate.notNull(lines, "lines cannot be null");
        Validate.isTrue(lines.length == 4, "lines.length must equal 4");

        afterBlockChanges.add(new UpdateSignMessage(location.getBlockX(), location.getBlockY(), location.getBlockZ(), lines));
    }

    public void sendMap(MapView map) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Achievements and statistics

    public boolean hasAchievement(Achievement achievement) {
        return stats.hasAchievement(achievement);
    }

    public void awardAchievement(Achievement achievement) {
        if (hasAchievement(achievement)) return;

        stats.setAchievement(achievement, true);
        sendAchievement(achievement, true);

        // todo: make an announcement if that's enabled
    }

    public void removeAchievement(Achievement achievement) {
        if (!hasAchievement(achievement)) return;

        stats.setAchievement(achievement, false);
        sendAchievement(achievement, false);
    }

    private void sendAchievement(Achievement achievement, boolean has) {
        Map<String, Integer> values = new HashMap<>();
        values.put(GlowAchievement.getName(achievement), has ? 1 : 0);
        session.send(new StatisticMessage(values));
    }

    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return stats.get(statistic);
    }

    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return stats.get(statistic, material);
    }

    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return stats.get(statistic, entityType);
    }

    public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
        stats.set(statistic, newValue);
    }

    public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
        stats.set(statistic, material, newValue);
    }

    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        stats.set(statistic, entityType, newValue);
    }

    public void incrementStatistic(Statistic statistic) {
        stats.add(statistic, 1);
    }

    public void incrementStatistic(Statistic statistic, int amount) {
        stats.add(statistic, amount);
    }

    public void incrementStatistic(Statistic statistic, Material material) {
        stats.add(statistic, material, 1);
    }

    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        stats.add(statistic, material, amount);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        stats.add(statistic, entityType, 1);
    }

    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
        stats.add(statistic, entityType, amount);
    }

    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        stats.add(statistic, -1);
    }

    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        stats.add(statistic, -amount);
    }

    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        stats.add(statistic, material, -1);
    }

    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        stats.add(statistic, material, -amount);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        stats.add(statistic, entityType, -1);
    }

    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        stats.add(statistic, entityType, -amount);
    }

    public void sendStats() {
        session.send(stats.toMessage());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Inventory

    public void updateInventory() {
        session.send(new SetWindowContentsMessage(invMonitor.getId(), invMonitor.getContents()));
    }

    public void sendItemChange(int slot, ItemStack item) {
        session.send(new SetWindowSlotMessage(invMonitor.getId(), slot, item));
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        super.setItemOnCursor(item);
        session.send(new SetWindowSlotMessage(-1, -1, item));
    }

    @Override
    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        if (!super.setWindowProperty(prop, value)) return false;
        session.send(new WindowPropertyMessage(invMonitor.getId(), prop.getId(), value));
        return true;
    }

    @Override
    public void openInventory(InventoryView view) {
        session.send(new CloseWindowMessage(invMonitor.getId()));

        super.openInventory(view);

        invMonitor = new InventoryMonitor(getOpenInventory());
        int viewId = invMonitor.getId();
        if (viewId != 0) {
            String title = view.getTitle();
            boolean useTitle = !view.getType().getDefaultTitle().equals(title);
            if (view.getTopInventory() instanceof PlayerInventory && !useTitle) {
                title = ((PlayerInventory) view.getTopInventory()).getHolder().getName();
                useTitle = true;
            }
            Message open = new OpenWindowMessage(viewId, invMonitor.getType(), title, view.getTopInventory().getSize(), useTitle);
            session.send(open);
        }

        updateInventory();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Player-specific time and weather

    public void setPlayerTime(long time, boolean relative) {
        timeOffset = (time % GlowWorld.DAY_LENGTH + GlowWorld.DAY_LENGTH) % GlowWorld.DAY_LENGTH;
        timeRelative = relative;
        sendTime();
    }

    public long getPlayerTime() {
        if (timeRelative) {
            // add timeOffset ticks to current time
            return (world.getTime() + timeOffset) % GlowWorld.DAY_LENGTH;
        } else {
            // return time offset
            return timeOffset;
        }
    }

    public long getPlayerTimeOffset() {
        return timeOffset;
    }

    public boolean isPlayerTimeRelative() {
        return timeRelative;
    }

    public void resetPlayerTime() {
        setPlayerTime(0, true);
    }

    public void sendTime() {
        long time = getPlayerTime();
        if (!timeRelative) {
            time = -time; // negative value indicates fixed time
        }
        session.send(new TimeMessage(world.getFullTime(), time));
    }

    public void setPlayerWeather(WeatherType type) {
        playerWeather = type;
        sendWeather();
    }

    public WeatherType getPlayerWeather() {
        return playerWeather;
    }

    public void resetPlayerWeather() {
        playerWeather = null;
        sendWeather();
    }

    public void sendWeather() {
        boolean stormy = playerWeather == null ? getWorld().hasStorm() : playerWeather == WeatherType.DOWNFALL;
        session.send(new StateChangeMessage(stormy ? 2 : 1, 0));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Player visibility

    public void hidePlayer(Player player) {

    }

    public void showPlayer(Player player) {

    }

    public boolean canSee(Player player) {
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Scoreboard

    public Scoreboard getScoreboard() {
        return null;
    }

    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {

    }

    ////////////////////////////////////////////////////////////////////////////
    // Conversable

    public boolean isConversing() {
        return false;
    }

    public void acceptConversationInput(String input) {

    }

    public boolean beginConversation(Conversation conversation) {
        return false;
    }

    public void abandonConversation(Conversation conversation) {

    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {

    }

    ////////////////////////////////////////////////////////////////////////////
    // Plugin messages

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(getServer().getMessenger(), source, channel, message);
        if (listeningChannels.contains(channel)) {
            // only send if player is listening for it
            session.send(new PluginMessage(channel, message));
        }
    }

    public Set<String> getListeningPluginChannels() {
        return Collections.unmodifiableSet(listeningChannels);
    }

    /**
     * Add a listening channel to this player.
     * @param channel The channel to add.
     */
    public void addChannel(String channel) {
        if (listeningChannels.add(channel)) {
            EventFactory.callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }

    /**
     * Remove a listening channel from this player.
     * @param channel The channel to remove.
     */
    public void removeChannel(String channel) {
        if (listeningChannels.remove(channel)) {
            EventFactory.callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }

    /**
     * Send the supported plugin channels to the client.
     */
    private void sendSupportedChannels() {
        Set<String> listening = server.getMessenger().getIncomingChannels();

        if (!listening.isEmpty()) {
            // send NUL-separated list of channels we support
            ByteBuf buf = Unpooled.buffer(16 * listening.size());
            for (String channel : listening) {
                buf.writeBytes(channel.getBytes(StandardCharsets.UTF_8));
                buf.writeByte(0);
            }
            session.send(new PluginMessage("REGISTER", buf.array()));
        }
    }
}
