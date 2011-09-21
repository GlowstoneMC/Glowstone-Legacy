package net.glowstone;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.command.*;
import net.glowstone.io.StorageQueue;
import net.glowstone.io.mcregion.McRegionWorldStorageProvider;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Recipe;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.util.config.Configuration;

import net.glowstone.command.*;
import net.glowstone.net.MinecraftPipelineFactory;
import net.glowstone.net.Session;
import net.glowstone.net.SessionRegistry;
import net.glowstone.scheduler.GlowScheduler;
import net.glowstone.util.PlayerListFile;
import net.glowstone.util.bans.BanManager;
import net.glowstone.util.bans.FlatFileBanManager;
import net.glowstone.inventory.CraftingManager;
import net.glowstone.map.GlowMapView;

import org.bukkit.util.permissions.DefaultPermissions;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * The core class of the Glowstone server.
 * @author Graham Edgecombe
 */
public final class GlowServer implements Server {

    /**
     * The logger for this class.
     */
    public static final Logger logger = Logger.getLogger("Minecraft");

    /**
     * The directory configurations are stored in
     */
    private static final File configDir = new File("config");
            
    /**
     * The configuration the server uses.
     */
    private static final Configuration config = new Configuration(new File(configDir, "glowstone.yml"));

    /**
     * The protocol version supported by the server
     */
    public static final int PROTOCOL_VERSION = 17;


    public static final StorageQueue storeQueue = new StorageQueue();

    /**
     * Creates a new server on TCP port 25565 and starts listening for
     * connections.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        try {
            if (!configDir.exists() || !configDir.isDirectory())
                configDir.mkdirs();
            config.load();

            storeQueue.start();
            
            int port = config.getInt("server.port", 25565);
            GlowServer server = new GlowServer();
            server.start();
            server.bind(new InetSocketAddress(port));
            logger.info("Ready for connections.");
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Error during server startup.", t);
        }
    }

    /**
     * The {@link ServerBootstrap} used to initialize Netty.
     */
    private final ServerBootstrap bootstrap = new ServerBootstrap();

    /**
     * A group containing all of the channels.
     */
    private final ChannelGroup group = new DefaultChannelGroup();

    /**
     * The network executor service - Netty dispatches events to this thread
     * pool.
     */
    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * A list of all the active {@link Session}s.
     */
    private final SessionRegistry sessions = new SessionRegistry();
    
    /**
     * The console manager of this server.
     */
    private final ConsoleManager consoleManager = new ConsoleManager(this, config.getString("server.terminal-mode", "jline"));
    
    /**
     * The services manager of this server.
     */
    private final SimpleServicesManager servicesManager = new SimpleServicesManager();

    /**
     * The command map of this server.
     */
    private final GlowCommandMap commandMap = new GlowCommandMap(this);
    
    /**
     * The command map for commands built-in to Glowstone.
     */
    private final GlowCommandMap builtinCommandMap = new GlowCommandMap(this);

    /**
     * The plugin manager of this server.
     */
    private final SimplePluginManager pluginManager = new SimplePluginManager(this, commandMap);
    
    /**
     * The crafting manager for this server.
     */
    private final CraftingManager craftingManager = new CraftingManager();
    
    /**
     * The list of OPs on the server.
     */
    private final PlayerListFile opsList = new PlayerListFile(new File(configDir, "ops.txt"));
    
    /**
     * The list of players whitelisted on the server.
     */
    private final PlayerListFile whitelist = new PlayerListFile(new File(configDir, "whitelist.txt"));

    /**
     * The server's ban manager.
     */
    private BanManager banManager = new FlatFileBanManager(this);

    /**
     * The world this server is managing.
     */
    private final ArrayList<GlowWorld> worlds = new ArrayList<GlowWorld>();

    /**
     * The task scheduler used by this server.
     */
    private final GlowScheduler scheduler = new GlowScheduler(this);

    /**
     * The server's default game mode
     */
    private GameMode defaultGameMode = GameMode.SURVIVAL;

    /**
     * The server's message of the day
     */
    private String motd;

    /**
     * Creates a new server.
     */
    public GlowServer() {
        init();
    }

    /**
     * Initializes the channel and pipeline factories.
     */
    private void init() {
        Bukkit.setServer(this);
        
        ChannelFactory factory = new NioServerSocketChannelFactory(executor, executor);
        bootstrap.setFactory(factory);

        ChannelPipelineFactory pipelineFactory = new MinecraftPipelineFactory(this);
        bootstrap.setPipelineFactory(pipelineFactory);
        
        // If the configuration is empty, attempt to migrate non-Glowstone configs
        if (config.getKeys().size() <= 1) {
            System.out.println("Generating default configuration config/glowstone.yml...");

            // bukkit.yml
            File bukkitYml = new File("bukkit.yml");
            if (bukkitYml.exists()) {
                Configuration bukkit = new Configuration(bukkitYml);
                bukkit.load();
                String moved = "", separator = "";

                if (bukkit.getNode("database") != null) {
                    config.setProperty("database", bukkit.getNode("database").getAll());
                    moved += separator + "database settings";
                    separator = ", ";
                }

                if (bukkit.getProperty("settings.spawn-radius") != null) {
                    config.setProperty("server.spawn-radius", bukkit.getInt("settings.spawn-radius", 16));
                    moved += separator + "spawn radius";
                    separator = ", ";
                }

                if (bukkit.getString("settings.update-folder") != null) {
                    config.setProperty("server.folders.update", bukkit.getString("settings.update-folder"));
                    moved += separator + "update folder";
                    separator = ", ";
                }

                if (bukkit.getNode("worlds") != null) {
                    config.setProperty("worlds", bukkit.getNode("worlds").getAll());
                    moved += separator + "world generators";
                    separator = ", ";
                }

                // TODO: move aliases when those are implemented

                if (moved.length() > 0) {
                    System.out.println("Copied " + moved + " from bukkit.yml");
                }
            }

            // server.properties
            File serverProps = new File("server.properties");
            if (serverProps.exists()) {
                try {
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(serverProps));
                    String moved = "", separator = "";

                    if (properties.containsKey("level-name")) {
                        String world = properties.getProperty("level-name", "world");
                        config.setProperty("server.world-name", world);
                        moved += separator + "world name";
                        separator = ", ";
                    }

                    if (properties.containsKey("online-mode")) {
                        String value = properties.getProperty("online-mode", "true");
                        boolean bool = value.equalsIgnoreCase("on") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true");
                        config.setProperty("server.online-mode", bool);
                        moved += separator + "online mode";
                        separator = ", ";
                    }

                    if (properties.containsKey("server-port")) {
                        String value = properties.getProperty("server-port", "25565");
                        try {
                            int port = Integer.parseInt(value);
                            config.setProperty("server.port", port);
                            moved += separator + "port";
                            separator = ", ";
                        }
                        catch (NumberFormatException ex) {}
                    }

                    if (properties.containsKey("max-players")) {
                        String value = properties.getProperty("max-players", "20");
                        try {
                            int players = Integer.parseInt(value);
                            config.setProperty("server.max-players", players);
                            moved += separator + "max players";
                            separator = ", ";
                        }
                        catch (NumberFormatException ex) {}
                    }

                    if (properties.containsKey("motd")) {
                        String motd = properties.getProperty("motd", "Glowstone server");
                        config.setProperty("server.motd", motd);
                        moved += separator + "MOTD";
                        separator = ", ";
                    }

                    if (properties.containsKey("gamemode")) {
                        String value = properties.getProperty("gamemode", "0");
                        try {
                            int mode = Integer.parseInt(value);
                            GameMode gMode = GameMode.getByValue(mode);
                            if (gMode == null) gMode = GameMode.SURVIVAL;
                            config.setProperty("server.def-game-mode", gMode.name());
                            moved += separator + "default game mode";
                            separator = ", ";
                        } catch (NumberFormatException ex) {}
                    }

                    // TODO: move nether, view distance, monsters, etc when implemented

                    if (moved.length() > 0) {
                        System.out.println("Copied " + moved + " from server.properties");
                    }
                }
                catch (IOException ex) {}
            }
        }

        // Ensure default values are set on everything
        // Server config
        config.getInt("server.port", 25565);
        config.getString("server.world-name", "world");
        config.getInt("server.max-players", 50);
        config.getInt("server.spawn-radius", 16);
        config.getBoolean("server.online-mode", true);
        config.getString("server.log-file", "logs/log-%D.txt");
        config.getString("server.terminal-mode", "jline");
        config.getBoolean("server.whitelist", false);
        config.getBoolean("server.allow-nether", true);
        config.getBoolean("server.allow-flight", false);
        config.getInt("server.view-distance", GlowChunk.VISIBLE_RADIUS);
        motd = config.getString("server.motd", "Glowstone server");
        setDefaultGameMode(config.getString("server.def-game-mode", GameMode.SURVIVAL.name()));

        // Server folders config
        config.getString("server.folders.plugins", "plugins");
        config.getString("server.folders.update", "update");

        // Database config
        config.getString("database.driver", "org.sqlite.JDBC");
        config.getString("database.url", "jdbc:sqlite:{DIR}{NAME}.db");
        config.getString("database.username", "glowstone");
        config.getString("database.password", "nether");
        config.getString("database.isolation", "SERIALIZABLE");

        config.save();
    }

    /**
     * Binds this server to the specified address.
     * @param address The addresss.
     */
    public void bind(SocketAddress address) {
        logger.log(Level.INFO, "Binding to address: {0}...", address);
        group.add(bootstrap.bind(address));
    }

    /**
     * Starts this server.
     */
    public void start() {
        // Config should have already loaded by this point, but to be safe...
        config.load();
        consoleManager.setupConsole();
        
        // Load player lists
        opsList.load();
        whitelist.load();
        banManager.load();

        // Start loading plugins
        loadPlugins();

        // Begin registering permissions
        DefaultPermissions.registerCorePermissions();

        // Register these first so they're usable while the worlds are loading
        GlowCommandMap.initGlowPermissions(this);
        builtinCommandMap.register(new MeCommand(this));
        builtinCommandMap.register(new ColorCommand(this));
        builtinCommandMap.register(new KickCommand(this));
        builtinCommandMap.register(new ListCommand(this));
        builtinCommandMap.register(new TimeCommand(this));
        builtinCommandMap.register(new WhitelistCommand(this));
        builtinCommandMap.register(new BanCommand(this));
        builtinCommandMap.register(new GameModeCommand(this));
        builtinCommandMap.register(new OpCommand(this));
        builtinCommandMap.register(new DeopCommand(this));
        builtinCommandMap.register(new StopCommand(this));
        builtinCommandMap.register(new SaveCommand(this));
        builtinCommandMap.register(new SayCommand(this));
        builtinCommandMap.register(new HelpCommand(this, builtinCommandMap.getKnownCommands()));

        enablePlugins(PluginLoadOrder.STARTUP);
        
        // Create worlds
        createWorld(config.getString("server.world-name", "world"), Environment.NORMAL);
        if (getAllowNether()) {
            createWorld(config.getString("server.world-name", "world") + "_nether", Environment.NETHER);
        }
        
        // Finish loading plugins
        enablePlugins(PluginLoadOrder.POSTWORLD);
        consoleManager.refreshCommands();
    }
    
    /**
     * Stops this server.
     */
    public void shutdown() {
        logger.info("The server is shutting down...");
        
        // Stop scheduler and disable plugins
        scheduler.stop();
        pluginManager.clearPlugins();
        
        // Save worlds
        for (World world : getWorlds()) {
            world.save();
        }
        
        // Kick (and save) all players
        for (Player player : getOnlinePlayers()) {
            player.kickPlayer("Server shutting down.");
        }
        
        // Gracefully stop Netty
        group.close();
        bootstrap.getFactory().releaseExternalResources();
        
        // And finally kill the console
        consoleManager.stop();

        storeQueue.end();
    }
    
    /**
     * Loads all plugins, calling onLoad, &c.
     */
    private void loadPlugins() {
        // clear the map
        commandMap.clearCommands();
            
        File folder = new File(config.getString("server.folders.plugins", "plugins"));
        folder.mkdirs();
        
        // clear plugins and prepare to load
        pluginManager.clearPlugins();
        pluginManager.registerInterface(JavaPluginLoader.class);
        Plugin[] plugins = pluginManager.loadPlugins(folder);

        // call onLoad methods
        for (Plugin plugin : plugins) {
            try {
                plugin.onLoad();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error loading {0}: {1}", new Object[]{plugin.getDescription().getName(), ex.getMessage()});
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Enable all plugins of the given load order type.
     * @param type The type of plugin to enable.
     */
    public void enablePlugins(PluginLoadOrder type) {
        Plugin[] plugins = pluginManager.getPlugins();
        for (Plugin plugin : plugins) {
            if (!plugin.isEnabled() && plugin.getDescription().getLoad() == type) {
                List<Command> pluginCommands = PluginCommandYamlParser.parse(plugin);

                if (!pluginCommands.isEmpty()) {
                    commandMap.registerAll(plugin.getDescription().getName(), pluginCommands);
                }
                
                List<Permission> perms = plugin.getDescription().getPermissions();
                for (Permission perm : perms) {
                    try {
                        pluginManager.addPermission(perm);
                    } catch (IllegalArgumentException ex) {
                        getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + perm.getName() + "' but it's already registered", ex);
                    }
                }

                try {
                    pluginManager.enablePlugin(plugin);
                } catch (Throwable ex) {
                    logger.log(Level.SEVERE, "Error loading {0}", plugin.getDescription().getFullName());
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Reloads the server, refreshing settings and plugin information
     */
    public void reload() {
        try {
            // Reload relevant configuration
            config.load();
            opsList.load();
            whitelist.load();
            
            // Reset crafting
            craftingManager.resetRecipes();
            
            // Load plugins
            loadPlugins();
            DefaultPermissions.registerCorePermissions();
            GlowCommandMap.initGlowPermissions(this);
            builtinCommandMap.registerAllPermissions();
            enablePlugins(PluginLoadOrder.STARTUP);
            enablePlugins(PluginLoadOrder.POSTWORLD);
            consoleManager.refreshCommands();

            storeQueue.reset();
            
            // TODO: Register aliases
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, "Uncaught error while reloading: {0}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Gets the channel group.
     * @return The {@link ChannelGroup}.
     */
    public ChannelGroup getChannelGroup() {
        return group;
    }

    /**
     * Gets the session registry.
     * @return The {@link SessionRegistry}.
     */
    public SessionRegistry getSessionRegistry() {
        return sessions;
    }
    
    /**
     * Returns the list of OPs on this server.
     */
    public PlayerListFile getOpsList() {
        return opsList;
    }
    
    /**
     * Returns the list of OPs on this server.
     */
    public PlayerListFile getWhitelist() {
        return whitelist;
    }

    /**
     * Returns the folder where configuration files are stored
     */
    public File getConfigDir() {
        return configDir;
    }

    /**
     * Returns the currently used ban manager for the server
     */
    public BanManager getBanManager() {
        return banManager;
    }

    public void setBanManager(BanManager manager) {
        this.banManager = manager;
        manager.load();
        logger.log(Level.INFO, "Using {0} for ban management", manager.getClass().getName());
    }

    /**
     * Gets the world by the given name.
     * @param name The name of the world to look up.
     * @return The {@link GlowWorld} this server manages.
     */
    public GlowWorld getWorld(String name) {
        for (GlowWorld world : worlds) {
            if (world.getName().equalsIgnoreCase(name))
                return world;
        }
        return null;
    }

    /**
     * Gets the world from the given Unique ID
     *
     * @param uid Unique ID of the world to retrieve.
     * @return World with the given Unique ID, or null if none exists.
     */
    public GlowWorld getWorld(UUID uid) {
        for (GlowWorld world : worlds) {
            if (uid.equals(world.getUID()))
                return world;
        }
        return null;
    }
    
    /**
     * Gets the list of worlds currently loaded.
     * @return An ArrayList containing all loaded worlds.
     */
    public List<World> getWorlds() {
        return new ArrayList<World>(worlds);
    }
    
    /**
     * Gets a list of available commands from the command mapc.
     * @return A list of all commands at the time.
     */
    protected String[] getAllCommands() {
        HashSet<String> knownCommands = new HashSet<String>(builtinCommandMap.getKnownCommandNames());
        knownCommands.addAll(commandMap.getKnownCommandNames());
        return knownCommands.toArray(new String[] {});
    }

    /**
     * Gets the name of this server implementation
     *
     * @return "Glowstone"
     */
    public String getName() {
        return "Glowstone";
    }

    /**
     * Gets the version string of this server implementation.
     *
     * @return version of this server implementation
     */
    public String getVersion() {
        return getClass().getPackage().getImplementationVersion();
    }
    
    /**
     * Gets a list of all currently logged in players
     *
     * @return An array of Players that are currently online
     */
    public Player[] getOnlinePlayers() {
        ArrayList<Player> result = new ArrayList<Player>();
        for (World world : getWorlds()) {
            for (Player player : world.getPlayers())
                result.add(player);
        }
        return result.toArray(new Player[] {});
    }
    
    /**
     * Get the maximum amount of players which can login to this server
     *
     * @return The amount of players this server allows
     */
    public int getMaxPlayers() {
        return config.getInt("server.max-players", 50);
    }

    /**
     * Gets the port the server listens on.
     * @return The port number the server is listening on.
     */
    public int getPort() {
        return config.getInt("server.port", 25565);
    }

    /**
     * Get the IP that this server is bound to or empty string if not specified
     *
     * @return The IP string that this server is bound to, otherwise empty string
     */
    public String getIp() {
        return "";
    }
    
    /**
     * Get the name of this server
     *
     * @return The name of this server
     */
    public String getServerName() {
        return "Glowstone Server";
    }
    
    /**
     * Get an ID of this server. The ID is a simple generally alphanumeric
     * ID that can be used for uniquely identifying this server.
     *
     * @return The ID of this server
     */
    public String getServerId() {
        return Integer.toHexString(getServerName().hashCode());
    }

    /**
     * Broadcast a message to all players.
     *
     * @param message the message
     * @return the number of players
     */
    public int broadcastMessage(String message) {
        return broadcast(message, BROADCAST_CHANNEL_USERS);
    }
    
    /**
     * Gets the name of the update folder. The update folder is used to safely update
     * plugins at the right moment on a plugin load.
     *
     * @return The name of the update folder
     */
    public String getUpdateFolder() {
        return config.getString("server.folders.update", "update");
    }
    
    /**
     * Gets a player object by the given username
     *
     * This method may not return objects for offline players
     *
     * @param name Name to look up
     * @return Player if it was found, otherwise null
     */
    public Player getPlayer(String name) {
        for (Player player : getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name))
                return player;
        }
        return null;
    }

    public Player getPlayerExact(String name) {
        for (Player player : getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name))
                return player;
        }
        return null;
    }

    /**
     * Attempts to match any players with the given name, and returns a list
     * of all possibly matches
     *
     * This list is not sorted in any particular order. If an exact match is found,
     * the returned list will only contain a single result.
     *
     * @param name Name to match
     * @return List of all possible players
     */
    public List<Player> matchPlayer(String name) {
        ArrayList<Player> result = new ArrayList<Player>();
        for (Player player : getOnlinePlayers()) {
            if (player.getName().startsWith(name)) {
                result.add(player);
            }
        }
        return result;
    }

    /**
     * Gets the PluginManager for interfacing with plugins
     *
     * @return PluginManager for this GlowServer instance
     */
    public SimplePluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * Gets the Scheduler for managing scheduled events
     *
     * @return Scheduler for this GlowServer instance
     */
    public GlowScheduler getScheduler() {
        return scheduler;
    }

    /**
     * Gets a services manager
     *
     * @return Services manager
     */
    public SimpleServicesManager getServicesManager() {
        return servicesManager;
    }
    
    /**
     * Gets the default ChunkGenerator for the given environment.
     * @return The ChunkGenerator.
     */
    private ChunkGenerator getGenerator(String name, Environment environment) {
        if (config.getString("worlds." + name + ".generator") != null) {
            String[] args = config.getString("worlds." + name + ".generator").split(":", 2);
            if (getPluginManager().getPlugin(args[0]) == null) {
                logger.log(Level.WARNING, "Plugin {0} specified for world {1} does not exist, using default.", new Object[]{args[0], name});
            } else {
                return getPluginManager().getPlugin(args[0]).getDefaultWorldGenerator(name, args.length == 2 ? args[1] : "");
            }
        }
        
        if (environment == Environment.NETHER) {
            return new net.glowstone.generator.UndergroundGenerator();
        } else if (environment == Environment.SKYLANDS) {
            return new net.glowstone.generator.CakeTownGenerator();
        } else {
            return new net.glowstone.generator.SurfaceGenerator();
        }
    }

    /**
     * Creates or loads a world with the given name.
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(name)
     *
     * @param name Name of the world to load
     * @param environment Environment type of the world
     * @return Newly created or loaded World
     */
    public GlowWorld createWorld(String name, Environment environment) {
        return createWorld(name, environment, new Random().nextLong(), getGenerator(name, environment));
    }

    /**
     * Creates or loads a world with the given name.
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(name)
     *
     * @param name Name of the world to load
     * @param environment Environment type of the world
     * @param seed Seed value to create the world with
     * @return Newly created or loaded World
     */
    public GlowWorld createWorld(String name, Environment environment, long seed) {
        return createWorld(name, environment, seed, getGenerator(name, environment));
    }

    /**
     * Creates or loads a world with the given name.
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(name)
     *
     * @param name Name of the world to load
     * @param environment Environment type of the world
     * @param generator ChunkGenerator to use in the construction of the new world
     * @return Newly created or loaded World
     */
    public GlowWorld createWorld(String name, Environment environment, ChunkGenerator generator) {
        return createWorld(name, environment, new Random().nextLong(), generator);
    }

    /**
     * Creates or loads a world with the given name.
     * If the world is already loaded, it will just return the equivalent of
     * getWorld(name)
     *
     * @param name Name of the world to load
     * @param environment Environment type of the world
     * @param seed Seed value to create the world with
     * @param generator ChunkGenerator to use in the construction of the new world
     * @return Newly created or loaded World
     */
    public GlowWorld createWorld(String name, Environment environment, long seed, ChunkGenerator generator) {
        if (getWorld(name) != null) return getWorld(name);
        GlowWorld world = new GlowWorld(this, name, environment, seed, new McRegionWorldStorageProvider(new File(name)), generator);
        if (world != null) worlds.add(world);
        return world;
    }

    /**
     * Unloads a world with the given name.
     *
     * @param name Name of the world to unload
     * @param save Whether to save the chunks before unloading.
     * @return Whether the action was Successful
     */
    public boolean unloadWorld(String name, boolean save) {
        if (getWorld(name) == null) return false;
        return unloadWorld(getWorld(name), save);
    }

    /**
     * Unloads the given world.
     *
     * @param world The world to unload
     * @param save Whether to save the chunks before unloading.
     * @return Whether the action was Successful
     */
    public boolean unloadWorld(World world, boolean save) {
        if (save) {
            world.save();
        }
        if (!(world instanceof GlowWorld)) {
            return false;
        }
        if (worlds.contains((GlowWorld) world)) {
            worlds.remove((GlowWorld) world);
            return true;
        }
        return false;
    }
    
    /**
     * Returns the primary logger associated with this server instance
     *
     * @return Logger associated with this server
     */
    public Logger getLogger() {
        return logger;
    }
    
    /**
     * Gets a {@link PluginCommand} with the given name or alias
     *
     * @param name Name of the command to retrieve
     * @return PluginCommand if found, otherwise null
     */
    public PluginCommand getPluginCommand(String name) {
        Command command = commandMap.getCommand(name);
        if (command instanceof PluginCommand) {
            return (PluginCommand) command;
        } else {
            return null;
        }
    }

    /**
     * Writes loaded players to disk
     */
    public void savePlayers() {
        for (Player player : getOnlinePlayers())
            player.saveData();
    }

    /**
     * Dispatches a command on the server, and executes it if found.
     *
     * @param commandLine command + arguments. Example: "test abc 123"
     * @return targetFound returns false if no target is found.
     * @throws CommandException Thrown when the executor for the given command fails with an unhandled exception
     */
    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        try {
            if (commandMap.dispatch(sender, commandLine)) {
                return true;
            }
            
            if (builtinCommandMap.dispatch(sender, commandLine)) {
                return true;
            }
            
            return false;
        }
        catch (CommandException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new CommandException("Unhandled exception executing command", ex);
        }
    }

    /**
     * Populates a given {@link com.avaje.ebean.config.ServerConfig} with values attributes to this server
     *
     * @param dbConfig ServerConfig to populate
     */
    public void configureDbConfig(com.avaje.ebean.config.ServerConfig dbConfig) {
        com.avaje.ebean.config.DataSourceConfig ds = new com.avaje.ebean.config.DataSourceConfig();
        ds.setDriver(config.getString("database.driver", "org.sqlite.JDBC"));
        ds.setUrl(config.getString("database.url", "jdbc:sqlite:{DIR}{NAME}.db"));
        ds.setUsername(config.getString("database.username", "glow"));
        ds.setPassword(config.getString("database.password", "stone"));
        ds.setIsolationLevel(com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation.getLevel(config.getString("database.isolation", "SERIALIZABLE")));

        if (ds.getDriver().contains("sqlite")) {
            dbConfig.setDatabasePlatform(new com.avaje.ebean.config.dbplatform.SQLitePlatform());
            dbConfig.getDatabasePlatform().getDbDdlSyntax().setIdentity("");
        }

        dbConfig.setDataSourceConfig(ds);
    }
    
    /**
     * Return the crafting manager.
     * @return The server's crafting manager.
     */
    public CraftingManager getCraftingManager() {
        return craftingManager;
    }

    /**
     * Adds a recipe to the crafting manager.
     * @param recipe The recipe to add.
     * @return True to indicate that the recipe was added.
     */
    public boolean addRecipe(Recipe recipe) {
        return craftingManager.addRecipe(recipe);
    }

    public Map<String, String[]> getCommandAliases() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getSpawnRadius() {
        return config.getInt("server.spawn-radius", 16);
    }

    public void setSpawnRadius(int value) {
        config.setProperty("server.spawn-radius", value);
    }

    public boolean getOnlineMode() {
        return config.getBoolean("server.online-mode", true);
    }

    public boolean getAllowNether() {
        return config.getBoolean("server.allow-nether", true);
    }

    public boolean hasWhitelist() {
        return config.getBoolean("server.whitelist", false);
    }

    public void setWhitelist(boolean enabled) {
        config.setProperty("server.whitelist", enabled);
    }

    public Set<OfflinePlayer> getWhitelistedPlayers() {
        Set<OfflinePlayer> players = new HashSet<OfflinePlayer>();
        for (String name : whitelist.getContents()) {
            players.add(getOfflinePlayer(name));
        }
        return players;
     }

    public void reloadWhitelist() {
        whitelist.load();
    }

    public boolean getAllowFlight() {
        return config.getBoolean("server.allow-flight", false);
    }

    public int broadcast(String message, String permission) {
        int count = 0;
        for (Permissible permissible : getPluginManager().getPermissionSubscriptions(permission)) {
            if (permissible instanceof CommandSender) {
                ((CommandSender) permissible).sendMessage(message);
            }
        }
        return count;
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        OfflinePlayer player = getPlayer(name);
        if (player == null) {
            player = new GlowOfflinePlayer(this, name);
        }
        return player;
    }

    public Set<String> getIPBans() {
        return banManager.getIpBans();
    }

    public void banIP(String address) {
       banManager.setIpBanned(address, true);
    }

    public void unbanIP(String address) {
        banManager.setIpBanned(address, false);
    }

    public Set<OfflinePlayer> getBannedPlayers() {
        Set<OfflinePlayer> bannedPlayers = new HashSet<OfflinePlayer>();
        for (String name : banManager.getBans()) {
            bannedPlayers.add(getOfflinePlayer(name));
        }
        return bannedPlayers;
    }

    public GameMode getDefaultGameMode() {
        return defaultGameMode;
    }

    public void setDefaultGameMode(GameMode mode) {
        GameMode oldMode = defaultGameMode;
        defaultGameMode = mode;
        for (Player player : getOnlinePlayers()) {
            if (player.getGameMode() == oldMode) {
                player.setGameMode(mode);
            }
        }
        config.setProperty("server.def-game-mode", mode.name());
    }

    public GameMode setDefaultGameMode(String mode) {
        GameMode gameMode;
        try {
            gameMode = GameMode.valueOf(mode);
        } catch (Throwable t) {
            logger.severe("Unknown game mode specified. Defaulting to survival");
            gameMode = GameMode.SURVIVAL;
        }
        setDefaultGameMode(gameMode);
        return getDefaultGameMode();
    }

    public int getViewDistance() {
        return config.getInt("server.view-distance", GlowChunk.VISIBLE_RADIUS);
    }
    
    public String getLogFile() {
        return config.getString("server.log-file", "logs/log-%D.txt");
    }

    public GlowMapView getMap(short id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public GlowMapView createMap(World world) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getMOTD() {
        return motd;
    }

    public void setMOTD(String motd) {
        setMOTD(motd, true);
    }

    public void setMOTD(String motd, boolean permanent) {
        this.motd = motd;
        if (permanent) config.setProperty("server.motd", motd);
    }

    public StorageQueue getStorageQueue() {
        return storeQueue;
    }
     
}
