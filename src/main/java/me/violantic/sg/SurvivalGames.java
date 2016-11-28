package me.violantic.sg;

import me.violantic.sg.game.Game;
import me.violantic.sg.game.GameState;
import me.violantic.sg.game.Map;
import me.violantic.sg.game.MapVoter;
import me.violantic.sg.game.listener.GameListener;
import me.violantic.sg.game.listener.PlayerListener;
import me.violantic.sg.game.util.*;
import me.violantic.sg.handler.GameHandler;
import me.violantic.sg.handler.ScoreboardHandler;
import me.violantic.sg.handler.VoteHandler;
import me.violantic.sg.handler.WaitingGlassAnimationHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Ethan on 11/3/2016.
 */
public class SurvivalGames extends JavaPlugin implements Game {

    private static SurvivalGames instance;

    private LagUtil lagUtil;

    private GameState state;
    private GameHandler handler;

    private Map lobby;
    private Map gameMap;
    private List<Location> startingLocations;
    private List<UUID> verifiedPlayers;

    private MapVoter gameMapVoter;
    private MapVoter deathMatchMapVoter;
    private VoteHandler voteHandler;
    private VoteHandler deathMatchVoteHandler;

    private ScoreboardHandler scoreboardHandler;
    private java.util.Map<String, String> scoreboardValues;

    private CrateGenerator crateGenerator;

    private WaitingGlassAnimationHandler waitingGlassAnimationHandler;

    private MysqlUtil mysql;

    private boolean enabled = true;

    public int second;

    @Override
    public void onEnable() {
        instance = this;

        lagUtil = new LagUtil();

        getConfig().options().copyDefaults(getConfig().contains("lobby"));
        saveConfig();

        //lobby = new Map("lobby", new String[]{"Mineswine Build Team"}, null);

        setState(new GameState("waiting"));
        getState().setCanOpen(true);
        handler = new GameHandler();

        gameMapVoter = new MapVoter("Game Map", new HashMap<String, Integer>() {
            {
                put("Drybone_Valley", 0);
                put("Futuristic_City", 0);
                put("Moon", 0);
                put("Teweran", 0);
                put("Turbulence", 0);
                put("Winter_Mountain", 0);
            }
        });

        deathMatchMapVoter = new MapVoter("Death Match Map", new HashMap<String, Integer>() {
            {
                // TODO - Add random map names
            }
        });

        voteHandler = new VoteHandler(gameMapVoter.getName(), gameMapVoter);
        deathMatchVoteHandler = new VoteHandler(deathMatchMapVoter.getName(), deathMatchMapVoter);

        startingLocations = new ArrayList<Location>();

        verifiedPlayers = new ArrayList<UUID>();
        scoreboardValues = new HashMap<String, String>();
        scoreboardValues.put("state", getState().getName());
        scoreboardValues.put("alive", getVerifiedPlayers().size() + "");

        scoreboardHandler = new ScoreboardHandler("SG");

        crateGenerator = new CrateGenerator(this);

        getServer().getScheduler().runTaskTimer(this, lagUtil, 100l, 1l);
        getServer().getScheduler().runTaskTimer(this, getHandler(), 0l, 20l);
        getServer().getScheduler().runTaskTimer(this, scoreboardHandler, 0l, 20l);
        getServer().getScheduler().runTaskTimer(this, voteHandler, 0l, 20 * 5l);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new GameListener(this), this);

        mysql = new MysqlUtil("149.56.96.176", 3306, "survivalgames", "root", "ts8VdmKN2uTNYaAw");

        getCommand("forcestart").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if (command.getName().equalsIgnoreCase("forcestart")) {
                    if (getState().getName().equalsIgnoreCase("progress")) {
                        if (handler.getSecond() < 585) {
                            commandSender.sendMessage(getPrefix() + ChatColor.RED + "Game is already started!");
                        } else {
                            handler.setSecond(590);
                            commandSender.sendMessage(getPrefix() + "Starting game!");
                        }
                        return false;
                    } else if (getState().getName().equalsIgnoreCase("waiting")) {
                        commandSender.sendMessage(getPrefix() + "Game is now choosing map, and will start!");
                        handler.setSecond(16);
                    } else if (getState().getName().equalsIgnoreCase("started")) {
                        commandSender.sendMessage(getPrefix() + ChatColor.RED + "Game is already processing!");
                        return false;
                    }
                    return true;
                }
                return false;
            }
        });

        getCommand("forceend").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if (command.getName().equalsIgnoreCase("forceend")) {
                    if (getState().getName().equalsIgnoreCase("progress")) {
                        handler.setSecond(3);
                        commandSender.sendMessage(getPrefix() + "Ending game!");
                    } else if (getState().getName().equalsIgnoreCase("waiting")) {
                        commandSender.sendMessage(getPrefix() + ChatColor.RED + "Game hasn't started yet!");
                    } else if (getState().getName().equalsIgnoreCase("started")) {
                        commandSender.sendMessage(getPrefix() + ChatColor.RED + "Game is still processing!");
                    }
                    return true;
                }
                return false;
            }
        });

        getCommand("stats").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if (command.getName().equalsIgnoreCase("stats")) {
                    System.out.println("STATS!");
                    if (!(commandSender instanceof Player)) {
                        return false;
                    }

                    Player player = (Player) commandSender;

                    try {
                        final String query = "SELECT * FROM s_games WHERE uuid='" + player.getUniqueId() + "';";
                        final PreparedStatement statement = getMysql().getConnection().prepareStatement(query);
                        final ResultSet set = statement.executeQuery();
                        while (set.next()) {
                            System.out.println("Querying");
                            if (set.getString("username") == null) {
                                System.out.println("user does not exist");
                                String add = "INSERT INTO s_games VALUES(NULL, '" + player.getName() + "', '" + player.getUniqueId().toString() + "', 0, 0, 0, 0, 0, 0);";
                                final PreparedStatement addStatement = getMysql().getConnection().prepareStatement(add);
                                new BukkitRunnable() {
                                    public void run() {
                                        try {
                                            addStatement.executeUpdate();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.runTaskAsynchronously(SurvivalGames.getInstance());

                                player.sendMessage(getPrefix() + "You were never in our databases... adding now!");
                                return false;
                            }

                            System.out.println("Retrieving");

                            player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------");
                            player.sendMessage("");
                            ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                            player.sendMessage("");
                            ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Rating: " + ChatColor.LIGHT_PURPLE + set.getInt("points"));
                            ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Kills: " + ChatColor.LIGHT_PURPLE + set.getInt("kills"));
                            ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Deaths: " + ChatColor.LIGHT_PURPLE + set.getInt("deaths"));
                            ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Chests Opened: " + ChatColor.LIGHT_PURPLE + set.getInt("chests_opened"));
                            ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Games Played: " + ChatColor.LIGHT_PURPLE + set.getInt("games"));
                            player.sendMessage("");
                            player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onDisable() {

    }

    public static SurvivalGames getInstance() {
        return instance;
    }

    public LagUtil getLagUtil() {
        return lagUtil;
    }

    public MysqlUtil getMysql() {
        return mysql;
    }

    public void setMysql(MysqlUtil mysql) {
        this.mysql = mysql;
    }

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    public CrateGenerator getCrateGenerator() {
        return crateGenerator;
    }

    public boolean enabled() {
        return enabled;
    }

    public void setEnable(boolean enabled) {
        this.enabled = enabled;
    }

    public List<UUID> getVerifiedPlayers() {
        return verifiedPlayers;
    }

    /**
     * Return the last user in the verifiex player cache
     *
     * @return
     */
    public UUID getWinner() {
        return getVerifiedPlayers().get(0);
    }

    public List<Location> getStartingLocations() {
        return startingLocations;
    }

    public int minimumPlayers() {
        return 1;
    }

    public int maximumPlayers() {
        return 24;
    }

    public VoteHandler getVoteHandler() {
        return voteHandler;
    }

    public MapVoter getGameMapVoter() {
        return gameMapVoter;
    }

    public MapVoter getDeathMatchMapVoter() {
        return deathMatchMapVoter;
    }

    public VoteHandler getDeathMatchVoteHandler() {
        return deathMatchVoteHandler;
    }

    public java.util.Map<String, String> getScoreboardValues() {
        return scoreboardValues;
    }

    public void initiateGameMap() {
        gameMap = new Map(gameMapVoter.getWinner(), new String[]{"Mineswine Build Team"}, null);
    }

    public Map getMap() {
        return gameMap;
    }

    public Map getGameMap() {
        return gameMap;
    }

    public void setupLocations() {
        for (Location location : LocationUtil.getCircle(LocationUtil.getLocation(getGameMap().getWorld().getName(), getConfig().getString("center")), 21, 24)) {
            try {
                getStartingLocations().add(location);
            } catch (Exception e) {
                getLogger().log(Level.CONFIG, location.toString() + " could not be parsed to a location!");
            }
        }

        this.waitingGlassAnimationHandler = new WaitingGlassAnimationHandler(getGameMap().getWorld(), getStartingLocations());
    }

    public String[] getDescriptions() {
        return new String[]{"Survive your longest", "Attempt to eliminate everybody"};
    }

    public Location getLobby() {
        return LocationUtil.getLocation("lobby", getConfig().getString("lobby"));
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameHandler getHandler() {
        return handler;
    }

    public void initiateVoteHandler() {
        getServer().getScheduler().runTaskTimer(this, voteHandler, 0l, 20l);
    }

    public String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "MINESWINE" + ChatColor.GRAY + "] [" + ChatColor.GREEN + "Survival" + ChatColor.DARK_GREEN + "Games" + ChatColor.GRAY + "] ";
    }

    public String ERROR_GAME_IN_PROGRESS() {
        return getPrefix() + ChatColor.RED + "You could not join the current game, it is already in play!";
    }

    public String GAME_LOBBY_JOIN_SUCCESS() {
        return getPrefix() + "You have joined the lobby! Currently waiting for " + (minimumPlayers() - Bukkit.getOnlinePlayers().size()) + " players...";
    }

}
