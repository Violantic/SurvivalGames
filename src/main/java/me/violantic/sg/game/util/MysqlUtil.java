package me.violantic.sg.game.util;

import me.violantic.sg.SurvivalGames;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
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
            String query = null;
            if(getStat(uuid, stat) == 0) {
                query = "UPDATE s_games SET " + stat + "=" + i + " WHERE uuid='" + uuid + "'";
            } else if(getStat(uuid, stat) > 0) {
                int old = getStat(uuid, stat);
                query = "UPDATE s_games SET " + stat + "=" + old + i + " WHERE uuid='" + uuid + "'";
            }
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.executeUpdate();
            System.out.println(getStat(uuid, stat) + " is the new stat for " + stat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String ay = "UPDATE s_games SET wins={wins},games={games},kills={kills},deaths={deaths},chests={chests},points={points} WHERE uuid={uuid}";
    public void update(String name, String uuid, int newWins, int newGames, int newKills, int newDeaths, int newChests, int newPoints) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement(ay
                            .replace("{name}", name)
                            .replace("{uuid}", uuid)
                            .replace("{wins}", getStat(uuid, "wins") + newWins + "")
                            .replace("{games}", getStat(uuid, "games") + newGames + "")
                            .replace("{kills}", getStat(uuid, "kills") + newKills + "")
                            .replace("{deaths}", getStat(uuid, "deaths") + newDeaths + "")
                            .replace("{chests}", getStat(uuid, "chests") + newChests + "")
                            .replace("{points}", getStat(uuid, "points") + newPoints + ""));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
