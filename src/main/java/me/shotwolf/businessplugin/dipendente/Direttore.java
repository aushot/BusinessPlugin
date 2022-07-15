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

public class Direttore {
    private UUID uuid;
    private Azienda azienda;

    private UtilsDbStatement utilsDbStatement;
    private UtilsChat utilsChat;
    private Main main;
    private ConfigFile cf;

    public Direttore(Main main, UUID uuid) {
        utilsDbStatement = new UtilsDbStatement(main);
        utilsChat = new UtilsChat();

        this.main = main;

        try {
            PreparedStatement statement = utilsDbStatement.preparedStatement("SELECT uuid, p_iva_azienda, nome_azienda FROM Direttori, Azienda WHERE p_iva_azienda = p_iva AND uuid = ?;");
            statement.setString(1, String.valueOf(uuid));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                this.uuid = UUID.fromString(rs.getString("uuid"));
                this.azienda = main.getPlayerManager().getAzienda(rs.getString("nome_azienda"));
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }
    public Direttore(Main main, UUID uuid, Azienda azienda, Player p) {
        cf = new ConfigFile(main);
        utilsDbStatement = new UtilsDbStatement(main);
        utilsChat = new UtilsChat();
        this.main = main;

            try {
                PreparedStatement statement1 = utilsDbStatement.preparedStatement("INSERT INTO Direttori (uuid, p_iva_azienda) VALUES (?, ?)");
                statement1.setString(1, String.valueOf(uuid));
                statement1.setInt(2, azienda.getP_iva());
                statement1.executeUpdate();

                utilsChat.sendMessage(p ,"Sei diventato direttore di " + azienda.getName()); //direttoreadd
            } catch (SQLException e){
                e.printStackTrace();
            }

        try {
            PreparedStatement statement2 = utilsDbStatement.preparedStatement("SELECT uuid, p_iva_azienda FROM Direttori WHERE uuid = ?;");
            statement2.setString(1, String.valueOf(uuid));
            ResultSet rs2 = statement2.executeQuery();
            if (rs2.next()) {
                this.uuid = UUID.fromString(rs2.getString("uuid"));
                this.azienda = azienda;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public Azienda getAzienda() {
        return azienda;
    }


    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isInDB(String uuid) {
        try {

            PreparedStatement statement = utilsDbStatement.preparedStatement("SELECT uuid, p_iva_azienda FROM Direttori WHERE uuid = ?;");
            statement.setString(1, uuid);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
