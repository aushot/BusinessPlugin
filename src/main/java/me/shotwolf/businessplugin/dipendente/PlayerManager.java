package me.shotwolf.businessplugin.dipendente;

import me.shotwolf.businessplugin.Main;
import me.shotwolf.businessplugin.configfile.ConfigFile;
import me.shotwolf.businessplugin.utils.UtilsDbStatement;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private ConfigFile cf;

    private HashMap<UUID, Dipendente> dipendente = new HashMap<>();
    private HashMap<UUID, Direttore> direttore = new HashMap<>();
    private HashMap<UUID, ViceDirettore> vicedirettore = new HashMap<>();
    private HashMap<String, Azienda> azienda = new HashMap<>();

    public Dipendente getDipendente(UUID uuid){
        return dipendente.get(uuid);
    }
    public Direttore getDirettore(UUID uuid){
        return direttore.get(uuid);
    }
    public ViceDirettore getViceDirettore(UUID uuid) {return vicedirettore.get(uuid); }
    public Azienda getAzienda(String nome_azienda){
        return azienda.get(nome_azienda.toLowerCase());
    }

    public String printAzienda(Main main){
        cf = new ConfigFile(main);
        String print = "";

        for (String name : azienda.keySet()) {
            String nome_azienda = name;


            String p_iva = String.format("%010d", (azienda.get(name).getP_iva()));


            print = print + (cf.getAziendaListLayout()
                    .replace("$v", p_iva)
                    .replace("$n", nome_azienda) + "\n" + ChatColor.RESET);
        }
        return print;
    }

    public void addAzienda(String name, Azienda az){
        azienda.put(name.toLowerCase(), az);
    }
    public void addDipendente(UUID uuid, Dipendente dp){ dipendente.put(uuid, dp);}
    public void addDirettore(UUID uuid, Direttore dir){ direttore.put(uuid, dir);}
    public void addViceDirettore(UUID uuid, ViceDirettore vicdir) { vicedirettore.put(uuid, vicdir); }
    public void removeDipendente(Main main, UUID uuid){
        UtilsDbStatement utilsDbStatement = new UtilsDbStatement(main);

        dipendente.remove(uuid);
        main.getBusinesschat().remove(uuid);
        try {
            PreparedStatement statement = utilsDbStatement.preparedStatement("DELETE FROM Dipendenti WHERE uuid = ?");
            statement.setString(1, String.valueOf(uuid));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeDirettore(Main main, UUID uuid){
        UtilsDbStatement utilsDbStatement = new UtilsDbStatement(main);

        direttore.remove(uuid);
        try {
            PreparedStatement statement = utilsDbStatement.preparedStatement("DELETE FROM Direttori WHERE uuid = ?");
            statement.setString(1, String.valueOf(uuid));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void removeViceDirettore(Main main, UUID uuid){
        UtilsDbStatement utilsDbStatement = new UtilsDbStatement(main);

        vicedirettore.remove(uuid);
        try {
            PreparedStatement statement = utilsDbStatement.preparedStatement("DELETE FROM Vicedirettori WHERE uuid = ?");
            statement.setString(1, String.valueOf(uuid));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeAzienda(Main main, String name){
        UtilsDbStatement utilsDbStatement = new UtilsDbStatement(main);

        try {
            PreparedStatement statement = utilsDbStatement.preparedStatement("SELECT uuid FROM Dipendenti WHERE p_iva_azienda = ?");
            statement.setInt(1, main.getPlayerManager().getAzienda(name).getP_iva());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String uuid = rs.getString("uuid");
                removeDipendente(main, UUID.fromString(uuid));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            PreparedStatement statement = utilsDbStatement.preparedStatement("DELETE FROM Dipendenti WHERE p_iva_azienda = ?");
            statement.setInt(1, main.getPlayerManager().getAzienda(name).getP_iva());
            statement.executeUpdate();

            PreparedStatement statement1 = utilsDbStatement.preparedStatement("ALTER TABLE `Azienda` AUTO_INCREMENT = 0;");
            statement1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        main.loadDipendenti();
        main.loadDirettori();
        main.loadViceDirettori();

        azienda.remove(name);
            try {
                PreparedStatement statement = utilsDbStatement.preparedStatement("DELETE FROM Azienda WHERE nome_azienda = ?");
                statement.setString(1, name);
                statement.executeUpdate();

                PreparedStatement statement1 = utilsDbStatement.preparedStatement("ALTER TABLE `Azienda` AUTO_INCREMENT = 0;");
                statement1.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
