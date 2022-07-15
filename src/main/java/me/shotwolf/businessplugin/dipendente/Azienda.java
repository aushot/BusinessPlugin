package me.shotwolf.businessplugin.dipendente;

import me.shotwolf.businessplugin.Main;
import me.shotwolf.businessplugin.configfile.ConfigFile;
import me.shotwolf.businessplugin.utils.UtilsChat;
import me.shotwolf.businessplugin.utils.UtilsDbStatement;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Azienda {

    private Main main;
    private UtilsDbStatement utilsDbStatement;
    private UtilsChat utilsChat;
    private ConfigFile cf;

    private int p_iva;
    private String name;
    private float money;

    public Azienda(Main main, String nome_azienda, Player p) throws SQLException {
        cf = new ConfigFile(main);
        utilsDbStatement = new UtilsDbStatement(main);
        utilsChat = new UtilsChat();
        this.main = main;

        if(isInDB(nome_azienda)){
            utilsChat.sendMessage(p ,cf.getBusinessExist());
        } else {

            name = nome_azienda.toLowerCase();
            money = 0;
            PreparedStatement statement1 = utilsDbStatement.preparedStatement("INSERT INTO Azienda (nome_azienda, money) VALUES (?, ?)");
            statement1.setString(1, name);
            statement1.setFloat(2, money);
            statement1.executeUpdate();

            utilsChat.sendMessage(p ,cf.getBusinessCreated());
        }

        PreparedStatement statement2 = utilsDbStatement.preparedStatement("SELECT p_iva, nome_azienda, money FROM Azienda WHERE nome_azienda = ?;");
        statement2.setString(1, nome_azienda);
        ResultSet rs2 = statement2.executeQuery();
        if(rs2.next()) {
            name = rs2.getString("nome_azienda");
            money = rs2.getFloat("money");
            p_iva = rs2.getInt("p_iva");
        }
    }

    public Azienda(Main main, String nome_azienda) throws SQLException{
        utilsDbStatement = new UtilsDbStatement(main);
        utilsChat = new UtilsChat();

        this.main = main;

        PreparedStatement statement = utilsDbStatement.preparedStatement("SELECT p_iva, nome_azienda, money FROM Azienda WHERE nome_azienda = ?;");
        statement.setString(1, nome_azienda.toLowerCase());
        ResultSet rs = statement.executeQuery();
        if(rs.next()) {
            name = rs.getString("nome_azienda");
            money = rs.getFloat("money");
            p_iva = rs.getInt("p_iva");
        } else {
            name = nome_azienda;
            money = 0;

            PreparedStatement statement1 = utilsDbStatement.preparedStatement("INSERT INTO Azienda (nome_azienda, money) VALUES (?, ?)");
            statement1.setString(1, name);
            statement1.setFloat(2, money);
            statement1.executeUpdate();
        }
    }

    public void setName(String name) {
        utilsDbStatement = new UtilsDbStatement(main);

        this.name = name;
        try {
            PreparedStatement statement = utilsDbStatement.preparedStatement("UPDATE Azienda SET nome_azienda = '"+ name + "' WHERE nome_azienda = '" + name + "';");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMoney(float money) {
        utilsDbStatement = new UtilsDbStatement(main);

        this.money = money;

        try {
            PreparedStatement statement = utilsDbStatement.preparedStatement("UPDATE Azienda SET money = '"+ money + "' WHERE nome_azienda = '" + name + "';");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setP_iva(int p_iva) {
        this.p_iva = p_iva;
    }

    public String getName() {
        return name;
    }

    public float getMoney() {
        return money;
    }

    public int getP_iva() {
        return p_iva;
    }

    public boolean isInDB(String nome_azienda) throws SQLException{
        PreparedStatement statement = utilsDbStatement.preparedStatement("SELECT p_iva, nome_azienda, money FROM Azienda WHERE nome_azienda = ?;");
        statement.setString(1, nome_azienda);
        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
}
