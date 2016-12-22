package me.violantic.sg.game.util;

import me.violantic.sg.SurvivalGames;
import me.violantic.sg.game.cosmetic.Cosmetic;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Ethan on 11/12/2016.
 */
public class MysqlUtil {

    private Connection connection;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public MysqlUtil(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        openConnection();
        createTables();
    }

    private void openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        } catch (SQLException e) {
            SurvivalGames.getInstance().getLogger().log(Level.SEVERE, "Connection to MySQL failed.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        if (connection == null) {
            openConnection();
            return connection;
        }

        try {
            if (connection.isClosed()) {
                openConnection();
                return connection;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void createTables() {
        String query =
                "CREATE TABLE IF NOT EXISTS sg_cos (" +
                "id INT(11) AUTO INCREMENT, PRIMARY KEY(id), uuid VARCHAR(60), cos_id INT(11)" +
                ");";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cosmetic> getCosmetics(String uuid) {
        List<Cosmetic> list = new ArrayList<Cosmetic>();
        String query = "SELECT * FROM sg_cos WHERE uuid='" + uuid + "'";
        PreparedStatement statment = null;
        try {
            statment = getConnection().prepareStatement(query);
            ResultSet set = statment.executeQuery();
            while(set.next()) {
                Cosmetic c = Cosmetic.valueOf(set.getInt("cos_id"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean hasCosmetic(String uuid, Cosmetic c) {
        return (getCosmetics(uuid).contains(c));
    }

    public void giveCosmeticIfNotExists(String uuid, Cosmetic cosmetic) {
        if(hasCosmetic(uuid, cosmetic)) return;
        String query = "INSERT INTO sg_cos VALUES(NULL, '" + uuid + "', '" + cosmetic.getId() + "')";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.executeUpdate();
            // TODO: run async ^^^
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getStats(String name, String uuid) {
        final String query = "SELECT * FROM s_games WHERE uuid='" + uuid + "';";
        try {
            final PreparedStatement statement = getConnection().prepareStatement(query);
            final ResultSet set = statement.executeQuery();
            if(!set.next()) {
                String add = "INSERT INTO s_games VALUES(NULL, '" + name + "', '" + uuid + "', 0, 0, 0, 0, 0, 0);";
                final PreparedStatement addStatement = getConnection().prepareStatement(add);
                new BukkitRunnable() {
                    public void run() {
                        try {
                            addStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }.runTaskAsynchronously(SurvivalGames.getInstance());
            }

            return set;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getStat(String uuid, String stat) {
        try {
            while(getStats(Bukkit.getPlayer(UUID.fromString(uuid)).getName(), uuid).next()) {
                return getStats(Bukkit.getPlayer(UUID.fromString(uuid)).getName(), uuid).getInt(stat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void setStat(String uuid, String stat, int i) {
        try {
            final String q = "SELECT * FROM s_games WHERE uuid='" + uuid + "';";
            final PreparedStatement statement = getConnection().prepareStatement(q);
            final ResultSet set = statement.executeQuery();
            while(set.next()) {
                int s = set.getInt(stat);
                i+=s;
            }

            String query = null;
            if(getStat(uuid, stat) == 0) {
                query = "UPDATE s_games SET " + stat + "=" + i + " WHERE uuid='" + uuid + "'";
            } else if(getStat(uuid, stat) > 0) {
                int old = getStat(uuid, stat);
                query = "UPDATE s_games SET " + stat + "=" + old + i + " WHERE uuid='" + uuid + "'";
            }
            PreparedStatement statement1 = getConnection().prepareStatement(query);
            statement1.executeUpdate();
            System.out.println(getStat(uuid, stat) + " is the new stat for " + stat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendStats(Player player) {
        try {
            final String query = "SELECT * FROM s_games WHERE uuid='" + player.getUniqueId() + "';";
            final PreparedStatement statement = getConnection().prepareStatement(query);
            final ResultSet set = statement.executeQuery();
            while (set.next()) {
                System.out.println("Querying");
                if (set.getString("username") == null) {
                    System.out.println("user does not exist");
                    String add = "INSERT INTO s_games VALUES(NULL, '" + player.getName() + "', '" + player.getUniqueId().toString() + "', 0, 0, 0, 0, 0, 0);";
                    final PreparedStatement addStatement = getConnection().prepareStatement(add);
                    new BukkitRunnable() {
                        public void run() {
                            try {
                                addStatement.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }.runTaskAsynchronously(SurvivalGames.getInstance());

                    player.sendMessage(SurvivalGames.getInstance().getPrefix() + "You were never in our databases... adding now!");
                }

                System.out.println("Retrieving");

                player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
                player.sendMessage("");
                ChatUtil.sendCenteredMessage(player, SurvivalGames.getInstance().getPrefix());
                player.sendMessage("");
                ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Rating: " + ChatColor.LIGHT_PURPLE + set.getInt("points"));
                ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Kills: " + ChatColor.LIGHT_PURPLE + set.getInt("kills"));
                ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Deaths: " + ChatColor.LIGHT_PURPLE + set.getInt("deaths"));
                ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Chests Opened: " + ChatColor.LIGHT_PURPLE + set.getInt("chests_opened"));
                ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Games Played: " + ChatColor.LIGHT_PURPLE + set.getInt("games"));
                ChatUtil.sendCenteredMessage(player, ChatColor.YELLOW + "Games Won: " + ChatColor.LIGHT_PURPLE + set.getInt("wins"));

                player.sendMessage("");
                player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
