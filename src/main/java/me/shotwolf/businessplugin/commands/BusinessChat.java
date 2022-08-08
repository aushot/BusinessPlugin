package me.shotwolf.businessplugin.commands;

import me.shotwolf.businessplugin.Main;
import me.shotwolf.businessplugin.configfile.ConfigFile;
import me.shotwolf.businessplugin.dipendente.Azienda;
import me.shotwolf.businessplugin.utils.UtilsChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BusinessChat implements CommandExecutor {
    Main main;

    public BusinessChat(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigFile cf = new ConfigFile(main);
        UtilsChat utilsChat = new UtilsChat();

        if (sender instanceof Player) {
            Player p = (Player) sender;
            UUID uuid = p.getUniqueId();

            String nome_azienda = null;
            Azienda azienda = null;

            if (main.getPlayerManager().getDipendente(uuid) != null) {
                nome_azienda = main.getPlayerManager().getDipendente(uuid).getAzienda().getName().toLowerCase();
                azienda = main.getPlayerManager().getDipendente(p.getUniqueId()).getAzienda();
            } else if (main.getPlayerManager().getViceDirettore(uuid) != null) {
                nome_azienda = main.getPlayerManager().getViceDirettore(uuid).getAzienda().getName().toLowerCase();
                azienda = main.getPlayerManager().getViceDirettore(p.getUniqueId()).getAzienda();
            } else if (main.getPlayerManager().getDirettore(uuid) != null) {
                nome_azienda = main.getPlayerManager().getDirettore(uuid).getAzienda().getName().toLowerCase();
                azienda = main.getPlayerManager().getDirettore(p.getUniqueId()).getAzienda();
            } else {
                utilsChat.sendMessage(p, cf.getNotEmployer()); //NotEmployer
            }

            if (p.hasPermission("direttore." + nome_azienda)
                    || p.hasPermission("vicedirettore." + nome_azienda)
                    || p.hasPermission("dipendente." + nome_azienda)) {

                if (!main.getBusinesschat().containsKey(uuid)) {
                    main.getBusinesschat().put(uuid, azienda);
                    utilsChat.sendMessage(p, cf.getChatbusinessActivated());
                } else {
                    main.getBusinesschat().remove(uuid);
                    utilsChat.sendMessage(p, cf.getChatbusinessDeactivated());
                }
            } else {
                utilsChat.sendMessage(p, cf.getNoPermission());
            }
        }

        return false;
    }
}
