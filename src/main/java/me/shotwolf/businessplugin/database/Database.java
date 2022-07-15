package me.shotwolf.businessplugin.database;

import me.shotwolf.businessplugin.Main;
import me.shotwolf.businessplugin.configfile.ConfigFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    ConfigFile cf;

    private Connection connection;

    public void connect(Main main) throws SQLException {
        cf = new ConfigFile(main);

        connection = DriverManager.getConnection(
                "jdbc:mysql://" + cf.getHost() + ":" + cf.getPort() + "/" + cf.getDatabase() + "?useSSL=" + cf.getUseSSL(), cf.getUsername(), cf.getPassword());
    }

    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect(){
        if(isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
