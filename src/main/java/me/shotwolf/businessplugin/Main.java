package me.shotwolf.businessplugin;

import me.shotwolf.businessplugin.commands.*;
import me.shotwolf.businessplugin.dipendente.Direttore;
import me.shotwolf.businessplugin.listener.ChatEventListener;
import me.shotwolf.businessplugin.database.Database;
import me.shotwolf.businessplugin.dipendente.Azienda;
import me.shotwolf.businessplugin.dipendente.Dipendente;
import me.shotwolf.businessplugin.dipendente.PlayerManager;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private Database database;
    private PlayerManager playerManager;
    private static Economy econ = null;
    private FileConfiguration config;
    private LuckPerms api;
    private HashMap<UUID, Azienda> businesschat = new HashMap<>();
    private HashMap<Dipendente, Integer> contratto = new HashMap<>();

    public HashMap<UUID, Azienda> getBusinesschat() {
        return businesschat;
    }

    public HashMap<Dipendente, Integer> getContratto() {
        return contratto;
    }

    @Override
    public void onEnable() {
        if (!setupEconomy()) { // check if VAULT plugin is in the server
            System.out.println("VAULT required");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            this.api = provider.getProvider();
        }

        loadDatabase();

        playerManager = new PlayerManager(); //inizialize Player Manager Object

        initiateConfig();

        loadCommands();

        loadBusiness();
        loadDipendenti();
        loadDirettori();

        Bukkit.getPluginManager().registerEvents(new ChatEventListener(this), this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {

            for(Player p : Bukkit.getOnlinePlayers()){
                if(contratto.containsKey(getPlayerManager().getDipendente(p.getUniqueId()))) {

                    p.sendMessage("Hai ricevuto la tua paga di " + contratto.get(getPlayerManager().getDipendente(p.getUniqueId())));
                }
            }
        }, 20*1, 20*1);
    }
    @Override
    public void onDisable() {
        database.disconnect();
    }

    public LuckPerms getApi() {
        return api;
    }

    public Database getDatabase() {
        return database;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public void initiateConfig(){
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }

    public void loadCommands(){
        this.getCommand("conto").setExecutor(new ContoCommand(this));
        this.getCommand("azienda").setExecutor(new AziendaCommand(this));
        this.getCommand("chat").setExecutor(new BusinessChat(this));
        this.getCommand("scontrino").setExecutor(new ScontrinoCommand(this));
        this.getCommand("contratto").setExecutor(new ContrattoCommand(this));
    }

    public void loadBusiness(){
        try {
            PreparedStatement statement = getDatabase().getConnection().prepareStatement("SELECT p_iva, nome_azienda, money FROM Azienda;"); //load business on Player Manager (HashMap)
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int p_iva = rs.getInt("p_iva");
                String name = rs.getString("nome_azienda");
                float money = rs.getFloat("money");

                Azienda azienda = new Azienda(this, name);
                azienda.setMoney(money);
                azienda.setP_iva(p_iva);

                playerManager.addAzienda(name, azienda);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void loadDipendenti(){
        try {
            PreparedStatement statement = getDatabase().getConnection().prepareStatement("SELECT uuid, nome_azienda FROM Dipendenti, Azienda WHERE p_iva_azienda=p_iva;"); //load dipendenti on Player Manager (HashMap)
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                String nome_azienda = rs.getString("nome_azienda");

                Dipendente dipendente = new Dipendente(this, UUID.fromString(uuid));
                dipendente.setAzienda(getPlayerManager().getAzienda(nome_azienda));
                dipendente.setUuid(UUID.fromString(uuid));

                playerManager.addDipendente(UUID.fromString(uuid), dipendente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDirettori(){
        try {
            PreparedStatement statement = getDatabase().getConnection().prepareStatement("SELECT uuid, nome_azienda FROM Direttori, Azienda WHERE p_iva_azienda=p_iva;"); //load direttori on Player Manager (HashMap)
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                String nome_azienda = rs.getString("nome_azienda");

                Direttore direttore = new Direttore(this, UUID.fromString(uuid));
                direttore.setAzienda(getPlayerManager().getAzienda(nome_azienda));
                direttore.setUuid(UUID.fromString(uuid));

                playerManager.addDirettore(UUID.fromString(uuid), direttore);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDatabase(){
        database = new Database();
        try {
            database.connect(this); //connection to the database
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
