package me.shotwolf.businessplugin.commands;

import me.shotwolf.businessplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ContrattoCommand implements CommandExecutor {
    Main main;
    public ContrattoCommand (Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length == 2){
                Player target = Bukkit.getPlayerExact(args[0]);
                int soldi = Integer.parseInt(args[1]);

                if(main.getPlayerManager().getDipendente(target.getUniqueId()).getAzienda() != null){
                    if(main.getPlayerManager().getDirettore(p.getUniqueId()).getAzienda() != null) {
                        if (main.getPlayerManager().getDipendente(target.getUniqueId()).getAzienda() == main.getPlayerManager().getDirettore(p.getUniqueId()).getAzienda()) {
                            main.getContratto().put(main.getPlayerManager().getDipendente(target.getUniqueId()), soldi);
                            target.sendMessage("Hai fatto un contratto di 200 euro a settimana");
                        }
                    }
                }
            }
        }


        return false;
    }
}
