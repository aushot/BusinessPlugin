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

    public BusinessChat (Main main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigFile cf = new ConfigFile(main);
        UtilsChat utilsChat = new UtilsChat();

        if(sender instanceof Player) {
            Player p = (Player) sender;
            UUID uuid = p.getUniqueId();

            if (main.getPlayerManager().getDipendente(uuid) != null) {
                String nome_azienda = main.getPlayerManager().getDipendente(uuid).getAzienda().getName().toLowerCase();
                Azienda azienda = main.getPlayerManager().getDipendente(p.getUniqueId()).getAzienda();

                if (p.hasPermission("direttore." + nome_azienda) || p.hasPermission("dipendente." + nome_azienda)) {
                    if (!main.getBusinesschat().containsKey(uuid)) {
                        main.getBusinesschat().put(uuid, azienda);
                    } else {
                        main.getBusinesschat().remove(uuid);
                    }
                } else {
                    utilsChat.sendMessage(p, cf.getNoPermission());
                }
            } else {
                utilsChat.sendMessage(p, cf.getNotEmployer()); //NotEmployer
            }
        }

        return false;
    }
}
