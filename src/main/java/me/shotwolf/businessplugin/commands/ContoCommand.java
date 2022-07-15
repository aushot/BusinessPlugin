package me.shotwolf.businessplugin.commands;

import me.shotwolf.businessplugin.Main;
import me.shotwolf.businessplugin.configfile.ConfigFile;
import me.shotwolf.businessplugin.utils.UtilsChat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ContoCommand implements CommandExecutor {

    private Main main;
    private ConfigFile cf;
    private UtilsChat utilsChat = new UtilsChat();


    public ContoCommand(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        cf = new ConfigFile(main);

        if(sender instanceof Player){
            Player p = (Player) sender;
            String nome_azienda;
            double soldi;

            switch (args.length){
                case 2:
                    switch (args[0].toLowerCase()){
                        case "mostra":
                            nome_azienda = args[1];
                            if(main.getPlayerManager().getAzienda(nome_azienda) != null) {
                                if (p.hasPermission("direttore." + nome_azienda)) {

                                    utilsChat.sendMessage(p, cf.getShowBusinessMoney()
                                            .replace("$b", main.getPlayerManager().getAzienda(nome_azienda).getName())
                                            .replace("$m", Float.toString(main.getPlayerManager().getAzienda(nome_azienda).getMoney())));
                                } else {
                                    utilsChat.sendMessage(p, cf.getNoPermission());
                                }
                            } else {
                                utilsChat.sendMessage(p, cf.getBusinessNotExist());
                            }
                            return true;
                    }
                case 3:
                    switch (args[0].toLowerCase()){
                        case "deposita":
                            nome_azienda = args[1];
                            soldi = Double.parseDouble(args[2]);

                            if(main.getPlayerManager().getAzienda(nome_azienda) != null) {
                                if(p.hasPermission("direttore." + nome_azienda)){
                                    Economy economy = Main.getEconomy();
                                    EconomyResponse er = economy.withdrawPlayer(p, soldi);
                                    if(er.transactionSuccess()){
                                        main.getPlayerManager().getAzienda(nome_azienda).setMoney((float) (main.getPlayerManager().getAzienda(nome_azienda).getMoney() + soldi));
                                    } else {
                                        utilsChat.sendMessage(p, cf.getNoMoneyLeft());
                                    }
                                } else {
                                    utilsChat.sendMessage(p, cf.getNoPermission());
                                }
                            }
                            return true;
                        case "preleva":
                            nome_azienda = args[1];
                            soldi = Double.parseDouble(args[2]);

                            if (main.getPlayerManager().getAzienda(nome_azienda) != null) {
                                if (p.hasPermission("direttore." + nome_azienda)) {
                                    if(soldi<=main.getPlayerManager().getAzienda(nome_azienda).getMoney()) {
                                        main.getPlayerManager().getAzienda(nome_azienda).setMoney((float) (main.getPlayerManager().getAzienda(nome_azienda).getMoney() - soldi));
                                        Economy economy = Main.getEconomy();
                                        economy.depositPlayer(p, soldi);
                                    } else {
                                        utilsChat.sendMessage(p, cf.getNoMoneyLeftBusiness());
                                    }
                                } else {
                                    utilsChat.sendMessage(p, cf.getNoPermission());
                                }
                            }
                            return true;
                        default:
                            utilsChat.sendMessage(p, cf.getInvalidCommand());
                            return false;
                    }
                default:
                    utilsChat.sendMessage(p, cf.getInvalidCommand());
                    return false;
            }
        }
        return true;
    }
}
