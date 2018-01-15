package nl.Steffion.BlockHunt.Helpers;
/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

import junit.framework.Assert;
import nl.Steffion.BlockHunt.BlockHunt;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@PrepareForTest(BlockHunt.class)
public class TestInstanceCreator {
    private BlockHunt plugin;
    private Server mockServer;
    private CommandSender commandSender;
    public Map<String, Player> players = new HashMap<String, Player>();

    public static final File pluginDirectory = new File("bin/test/server/plugins/BlockHunt");
    public static final File serverDirectory = new File("bin/test/server");
    public static final File worldsDirectory = new File("bin/test/server");

    public boolean setUp() {
        try {
            pluginDirectory.delete();
            serverDirectory.delete();
            Assert.assertFalse(pluginDirectory.exists());
            pluginDirectory.mkdirs();
            Assert.assertTrue(pluginDirectory.exists());
            MockGateway.MOCK_STANDARD_METHODS = false;
            PluginDescriptionFile pdf = new PluginDescriptionFile("Blockhunt", "0.2.1-SNAPSHOT",
                    "nl.Steffion.BlockHunt.BlockHunt");
            //PluginDescriptionFile pdf = PowerMockito.spy(base);
            //when(pdf.getAuthors()).thenReturn(new ArrayList<String>());
            //plugin = PowerMockito.spy(new BlockHunt());

            //

            // core = PowerMockito.spy(new MultiverseCore());

            // Let's let all MV files go to bin/test
            // Let's let all MV files go to bin/test

            // Return a fake PDF file.


            // Add Core to the list of loaded plugins

            // Mock the Plugin Manager
            //when(mockPluginManager.getPermission(anyString())).thenReturn(null);

            // Make some fake folders to fool the fake MV into thinking these worlds exist
            File worldNormalFile = new File(serverDirectory, "world");
            Util.log("Creating world-folder: " + worldNormalFile.getAbsolutePath());
            worldNormalFile.mkdirs();
            MockWorldFactory.makeNewMockWorld("world", Environment.NORMAL, WorldType.NORMAL);
            File worldNetherFile = new File(serverDirectory, "world_nether");
            Util.log("Creating world-folder: " + worldNetherFile.getAbsolutePath());
            worldNetherFile.mkdirs();
            MockWorldFactory.makeNewMockWorld("world_nether", Environment.NETHER, WorldType.NORMAL);
            File worldSkylandsFile = new File(serverDirectory, "world_the_end");
            Util.log("Creating world-folder: " + worldSkylandsFile.getAbsolutePath());
            worldSkylandsFile.mkdirs();
            MockWorldFactory.makeNewMockWorld("world_the_end", Environment.THE_END, WorldType.NORMAL);
            File world2File = new File(serverDirectory, "world2");
            Util.log("Creating world-folder: " + world2File.getAbsolutePath());
            world2File.mkdirs();
            MockWorldFactory.makeNewMockWorld("world2", Environment.NORMAL, WorldType.NORMAL);

            // Initialize the Mock server.
            mockServer = mock(Server.class);
            plugin = mock(BlockHunt.class);
            plugin.onEnable();
            //when(plugin.isEnabled()).thenReturn(true);
            //when(plugin.getDescription()).thenReturn(pdf);
            when(plugin.getDataFolder()).thenReturn(pluginDirectory);
            JavaPlugin[] plugins = new JavaPlugin[]{plugin};
            PluginManager mockPluginManager = PowerMockito.mock(PluginManager.class);
            when(mockPluginManager.getPlugins()).thenReturn(plugins);
            when(mockServer.getName()).thenReturn("TestBukkit");
            Logger.getLogger("Minecraft").setParent(Util.logger);
            when(mockServer.getPluginManager()).thenReturn(mockPluginManager);
            when(mockPluginManager.getPlugin("Blockhunt")).thenReturn(plugin);
            when(mockPluginManager.getPermission(anyString())).thenReturn(null);
            when(mockServer.getLogger()).thenReturn(Util.logger);
            when(mockServer.getWorldContainer()).thenReturn(worldsDirectory);
            when(plugin.getServer()).thenReturn(mockServer);

            Answer<Player> playerAnswer = new Answer<Player>() {
                public Player answer(InvocationOnMock invocation) throws Throwable {
                    String arg;
                    try {
                        arg = (String) invocation.getArguments()[0];
                    } catch (Exception e) {
                        return null;
                    }
                    Player player = players.get(arg);
                    if (player == null) {
                        player = new MockPlayer(arg, mockServer);
                        players.put(arg, player);
                    }
                    return player;
                }
            };
            when(mockServer.getPlayer(anyString())).thenAnswer(playerAnswer);
            when(mockServer.getOfflinePlayer(anyString())).thenAnswer(playerAnswer);
            when(mockServer.getOfflinePlayers()).thenAnswer(new Answer<OfflinePlayer[]>() {
                public OfflinePlayer[] answer(InvocationOnMock invocation) throws Throwable {
                    return players.values().toArray(new Player[players.values().size()]);
                }
            });
            when(mockServer.getOnlinePlayers()).thenAnswer(new Answer<Collection<Player>>() {
                public Collection<Player> answer(InvocationOnMock invocation) throws Throwable {
                    return players.values();
                }
            });

            // Give the server some worlds
            when(mockServer.getWorld(anyString())).thenAnswer(new Answer<World>() {
                public World answer(InvocationOnMock invocation) throws Throwable {
                    String arg;
                    try {
                        arg = (String) invocation.getArguments()[0];
                    } catch (Exception e) {
                        return null;
                    }
                    return MockWorldFactory.getWorld(arg);
                }
            });

            when(mockServer.getWorld(any(UUID.class))).thenAnswer(new Answer<World>() {
                @Override
                public World answer(InvocationOnMock invocation) throws Throwable {
                    UUID arg;
                    try {
                        arg = (UUID) invocation.getArguments()[0];
                    } catch (Exception e) {
                        return null;
                    }
                    return MockWorldFactory.getWorld(arg);
                }
            });

            when(mockServer.getWorlds()).thenAnswer(new Answer<List<World>>() {
                public List<World> answer(InvocationOnMock invocation) throws Throwable {
                    return MockWorldFactory.getWorlds();
                }
            });



            when(mockServer.createWorld(Matchers.isA(WorldCreator.class))).thenAnswer(
                    new Answer<World>() {
                        public World answer(InvocationOnMock invocation) throws Throwable {
                            WorldCreator arg;
                            try {
                                arg = (WorldCreator) invocation.getArguments()[0];
                            } catch (Exception e) {
                                return null;
                            }
                            // Add special case for creating null worlds.
                            // Not sure I like doing it this way, but this is a special case
                            if (arg.name().equalsIgnoreCase("nullworld")) {
                                return MockWorldFactory.makeNewNullMockWorld(arg.name(), arg.environment(), arg.type());
                            }
                            return MockWorldFactory.makeNewMockWorld(arg.name(), arg.environment(), arg.type());
                        }
                    });

            when(mockServer.unloadWorld(anyString(), anyBoolean())).thenReturn(true);

            // add mock scheduler
            BukkitScheduler mockScheduler = mock(BukkitScheduler.class);
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class), anyLong())).
                    thenAnswer(new Answer<Integer>() {
                        public Integer answer(InvocationOnMock invocation) throws Throwable {
                            Runnable arg;
                            try {
                                arg = (Runnable) invocation.getArguments()[1];
                            } catch (Exception e) {
                                return null;
                            }
                            arg.run();
                            return null;
                        }
                    });
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class))).
                    thenAnswer(new Answer<Integer>() {
                        public Integer answer(InvocationOnMock invocation) throws Throwable {
                            Runnable arg;
                            try {
                                arg = (Runnable) invocation.getArguments()[1];
                            } catch (Exception e) {
                                return null;
                            }
                            arg.run();
                            return null;
                        }
                    });
            when(mockServer.getScheduler()).thenReturn(mockScheduler);


            // Set Core


            // Set server
            Field serverfield = JavaPlugin.class.getDeclaredField("server");
            serverfield.setAccessible(true);
            serverfield.set(plugin, mockServer);

            // Set worldManager


            // Set playerListener


            // Set entityListener


            // Set weatherListener


            // Init our command sender
            final Logger commandSenderLogger = Logger.getLogger("CommandSender");
            commandSenderLogger.setParent(Util.logger);
            commandSender = mock(CommandSender.class);
            doAnswer(new Answer<Void>() {
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    commandSenderLogger.info(ChatColor.stripColor((String) invocation.getArguments()[0]));
                    return null;
                }
            }).when(commandSender).sendMessage(anyString());
            when(commandSender.getServer()).thenReturn(mockServer);
            when(commandSender.getName()).thenReturn("MockCommandSender");
            when(commandSender.isPermissionSet(anyString())).thenReturn(true);
            when(commandSender.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
            when(commandSender.hasPermission(anyString())).thenReturn(true);
            when(commandSender.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
            when(commandSender.addAttachment(plugin)).thenReturn(null);
            when(commandSender.isOp()).thenReturn(true);

            Bukkit.setServer(mockServer);

            plugin.onLoad();

            plugin.onEnable();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean tearDown() {
        /*
        List<MultiverseWorld> worlds = new ArrayList<MultiverseWorld>(core.getMVWorldManager()
                .getMVWorlds());
        for (MultiverseWorld world : worlds) {
            core.getMVWorldManager().deleteWorld(world.getName());
        }
        */

        Server maybeNullServer = getServer();
        PluginManager maybeNullPluginManager = maybeNullServer.getPluginManager();
        Plugin plugin = maybeNullPluginManager.getPlugin("Multiverse-Inventories");
        //Plugin plugin = getServer().getPluginManager().getPlugin("Multiverse-Inventories");

        MockWorldFactory.clearWorlds();


        try {
            Field serverField = Bukkit.class.getDeclaredField("server");
            serverField.setAccessible(true);
            serverField.set(Class.forName("org.bukkit.Bukkit"), null);
        } catch (Exception e) {
            Util.log(Level.SEVERE,
                    "Error while trying to unregister the server from Bukkit. Has Bukkit changed?");
            e.printStackTrace();
            Assert.fail(e.getMessage());
            return false;
        }

        return true;
    }

    public BlockHunt getPlugin() {
        return this.plugin;
    }

    public Server getServer() {
        return this.mockServer;
    }

    public CommandSender getCommandSender() {
        return commandSender;
    }
}