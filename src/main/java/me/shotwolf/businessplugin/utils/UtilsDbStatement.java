package me.shotwolf.businessplugin.utils;

import me.shotwolf.businessplugin.Main;
import me.shotwolf.businessplugin.dipendente.Azienda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilsDbStatement {
    private Main main;

    public UtilsDbStatement(Main main){
        this.main = main;
    }

    public PreparedStatement preparedStatement(String SQLcode) throws SQLException {
        return main.getDatabase().getConnection().prepareStatement(SQLcode);
    }

}
