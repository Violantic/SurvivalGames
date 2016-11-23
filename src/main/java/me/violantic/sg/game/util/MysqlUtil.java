package me.violantic.sg.game.util;

import me.violantic.sg.SurvivalGames;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public void update(String name, String uuid, int newWins, int newGames,int newKills, int newDeaths, int newChests, int newPoints) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("UPDATE s_games VALUES ((SELECT ID WHERE uuid='"
                            + uuid + "'), "
                            + name + ", "
                            + uuid + ", ((SELECT wins WHERE uuid='" + uuid + "')+"
                            + newWins + "), ((SELECT games WHERE uuid='" + uuid + "')+"
                            + newGames + "), ((SELECT kills WHERE uuid='" + uuid + "')+"
                            + newKills + ", ((SELECT deaths WHERE uuid='" + uuid + "')+"
                            + newDeaths + ", ((SELECT chests_opened WHERE uuid='" + uuid + "')+"
                            + newChests + ", ((SELECT points WHERE uuid='" + uuid + "')+"
                            + newPoints + ")");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
