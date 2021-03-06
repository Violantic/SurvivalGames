package com.mineswine.sg;

import com.mineswine.sg.game.Game;
import com.mineswine.sg.game.GameState;
import com.mineswine.sg.game.command.CommandManager;
import com.mineswine.sg.game.command.SGCommand;
import com.mineswine.sg.game.command.custom.ForceEndCommand;
import com.mineswine.sg.game.command.custom.ForceStartCommand;
import com.mineswine.sg.game.command.custom.HistoryCommand;
import com.mineswine.sg.game.command.custom.StatsCommand;
import com.mineswine.sg.game.history.HistoryLogger;
import com.mineswine.sg.game.lang.Messages;
import com.mineswine.sg.game.listener.GameListener;
import com.mineswine.sg.game.listener.PlayerListener;
import com.mineswine.sg.game.loot.CrateGenerator;
import com.mineswine.sg.game.map.Map;
import com.mineswine.sg.game.map.MapVoter;
import com.mineswine.sg.game.util.CosmeticUtil;
import com.mineswine.sg.game.util.LocationUtil;
import com.mineswine.sg.game.util.MysqlUtil;
import com.mineswine.sg.handler.GameHandler;
import com.mineswine.sg.handler.ScoreboardHandler;
import com.mineswine.sg.handler.VoteHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

    private CommandManager commands;

    private GameState state;
    private GameHandler handler;

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
    private boolean locationGenerationInvoked = false;

    private Location corner1;
    private Location corner2;

    private MysqlUtil mysql;
    private CosmeticUtil cosmetics;

    private HistoryLogger history;

    private boolean enabled = true;

    public int second;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(getConfig().contains("lobby"));
        saveConfig();

        setState(new GameState("waiting"));
        getState().setCanOpen(true);
        handler = new GameHandler();

        history = new HistoryLogger();

        new Map("Lobby", new String[]{"Ethan"}, null);

        gameMapVoter = new MapVoter("Game Map", new HashMap<String, Integer>() {
            {
                put("Test", 0);
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

        //getServer().getScheduler().runTaskTimer(this, lagUtil, 100l, 1l);
        getServer().getScheduler().runTaskTimer(this, getHandler(), 0l, 20l);
        getServer().getScheduler().runTaskTimer(this, scoreboardHandler, 0l, 20l);
        getServer().getScheduler().runTaskTimer(this, voteHandler, 0l, 20 * 10l);
        //getServer().getScheduler().runTaskTimerAsynchronously(this, new CosmeticHandler(this), 0l, 1);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new GameListener(this), this);

        mysql = new MysqlUtil("localhost", 3306, "mcc", "root", "HCFTESTSERVER1");
        cosmetics = new CosmeticUtil(mysql);

        commands = new CommandManager(this);
        commands.register(new StatsCommand(this));
        commands.register(new ForceStartCommand(this));
        commands.register(new ForceEndCommand(this));
        commands.register(new HistoryCommand(this));

        for(SGCommand commandz : commands.getCommandCache()) {
            String name = commandz.getName();
            getCommand(name).setExecutor(new CommandExecutor() {
                @Override
                public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                    if(!(commandSender instanceof Player)) {
                        return false;
                    }

                    if(command.getName().equalsIgnoreCase(name)) {
                        if(!commandSender.hasPermission(commandz.getNode())) {
                            commandSender.sendMessage(getPrefix() + Messages.EN_NO_PERMS);
                            return false;
                        }
                        commandz.execute(commandSender, strings);
                        return true;
                    }

                    return false;
                }
            });
        }
    }

    @Override
    public void onDisable() {

    }

    public static SurvivalGames getInstance() {
        return instance;
    }

    public HistoryLogger getHistory() {
        return history;
    }

    public void setHistory(HistoryLogger history) {
        this.history = history;
    }

    public CommandManager getCommands() {
        return commands;
    }

    public void setCommands(CommandManager commands) {
        this.commands = commands;
    }

    public MysqlUtil getMysql() {
        return mysql;
    }

    public void setMysql(MysqlUtil mysql) {
        this.mysql = mysql;
    }

    public CosmeticUtil getCosmetics() {
        return cosmetics;
    }

    public void setCosmetics(CosmeticUtil cosmetics) {
        this.cosmetics = cosmetics;
    }

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    public CrateGenerator getCrateGenerator() {
        return crateGenerator;
    }

    public boolean isLocationGenerationInvoked() {
        return locationGenerationInvoked;
    }

    public void startLocationGenerator() {
        this.locationGenerationInvoked = true;
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

    public Location getCorner1() {
        return corner1;
    }

    public void setCorner1(Location corner1) {
        this.corner1 = corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public void setCorner2(Location corner2) {
        this.corner2 = corner2;
    }

    public void initiateGameMap() {
        gameMap = new Map(gameMapVoter.getWinner(), new String[]{"Mineswine Build Team"}, null);
        corner1 = LocationUtil.getLocation(gameMap.getName(), getConfig().getString("corner1"));
        corner2 = LocationUtil.getLocation(gameMap.getName(), getConfig().getString("corner2"));
        this.crateGenerator = new CrateGenerator(this);
    }

    public Map getMap() {
        return gameMap;
    }

    public Map getGameMap() {
        return gameMap;
    }

    public void setupLocations() {
        List<Location> locs = LocationUtil.getCircle(LocationUtil.getLocation(getGameMap().getName(), getConfig().getString("center")), 15, 30);
        for (Location location : locs) {
            try {
                getStartingLocations().add(location);
                location.subtract(0, 1, 0).getBlock().setType(Material.COAL_BLOCK);
            } catch (Exception e) {
                getLogger().log(Level.CONFIG, location.toString() + " could not be parsed to a location!");
            }
        }
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
        return ChatColor.RED + "" + ChatColor.BOLD + "Borawski's " + ChatColor.YELLOW + "" + ChatColor.BOLD + "SG " + ChatColor.RESET + ChatColor.GRAY  + "";
    }

}
