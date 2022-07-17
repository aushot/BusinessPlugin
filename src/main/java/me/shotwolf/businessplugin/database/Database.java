package me.shotwolf.businessplugin.database;

import me.shotwolf.businessplugin.Main;
import me.shotwolf.businessplugin.configfile.ConfigFile;
import me.shotwolf.businessplugin.utils.UtilsDbStatement;

import java.sql.*;

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

    public void createTable(Main main){
        UtilsDbStatement utilsDbStatement = new UtilsDbStatement(main);
        try {
                PreparedStatement statement1 = utilsDbStatement.preparedStatement("CREATE TABLE IF NOT EXISTS `Azienda` (" +
                        "  `p_iva` int(10) PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                        "  `nome_azienda` varchar(20) UNIQUE NOT NULL," +
                        "  `money` float NOT NULL" +
                        ");");
                statement1.executeUpdate();

                PreparedStatement statement2 = utilsDbStatement.preparedStatement("CREATE TABLE IF NOT EXISTS `Dipendenti` " +
                        "(`uuid` varchar(36) PRIMARY KEY NOT NULL," +
                        "`p_iva_azienda` int(10) NOT NULL," +
                        "FOREIGN KEY (p_iva_azienda) REFERENCES Azienda(p_iva)" +
                        ");");
                statement2.executeUpdate();

                PreparedStatement statement3 = utilsDbStatement.preparedStatement("CREATE TABLE IF NOT EXISTS `Direttori` " +
                        "(`uuid` varchar(36) PRIMARY KEY NOT NULL," +
                        "`p_iva_azienda` int(10) NOT NULL," +
                        "FOREIGN KEY (p_iva_azienda) REFERENCES Azienda(p_iva)" +
                        ");");
                statement3.executeUpdate();

                PreparedStatement statement4 = utilsDbStatement.preparedStatement("CREATE TABLE IF NOT EXISTS `Vicedirettori` " +
                        "(`uuid` varchar(36) PRIMARY KEY NOT NULL," +
                        "`p_iva_azienda` int(10) NOT NULL," +
                        "FOREIGN KEY (p_iva_azienda) REFERENCES Azienda(p_iva)" +
                        ");");
                statement4.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
