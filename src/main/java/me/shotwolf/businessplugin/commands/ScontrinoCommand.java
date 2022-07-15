package me.shotwolf.businessplugin.commands;

import me.shotwolf.businessplugin.Main;
import me.shotwolf.businessplugin.configfile.ConfigFile;
import me.shotwolf.businessplugin.utils.UtilsChat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ScontrinoCommand implements CommandExecutor {
    Main main;
    public ScontrinoCommand(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UtilsChat utilsChat = new UtilsChat();
        ConfigFile cf = new ConfigFile(main);

        if(sender instanceof Player) {
            Player from = (Player) sender;
            if (args.length == 4) {
                if(from.hasPermission("dipendente." + args[2].toLowerCase()) || from.hasPermission("direttore." + args[2].toLowerCase())) {
                    if (Bukkit.getPlayerExact(args[0]) != null) {
                        Player p = Bukkit.getPlayerExact(args[0]);
                        double importo = Double.parseDouble(args[1]); //scontrino (PLAYER) (Importo) (NomeAzienda) (descrizione)
                        String nome_azienda = args[2];
                        String descrizione = args[3];

                        if (main.getPlayerManager().getAzienda(nome_azienda) != null) {

                            Economy economy = Main.getEconomy();
                            EconomyResponse er = economy.withdrawPlayer(from, importo);
                            if (er.transactionSuccess()) {
                                ItemStack paper = new ItemStack(Material.PAPER);
                                ItemMeta papermeta = paper.getItemMeta();
                                papermeta.setDisplayName(utilsChat.coloredMessage(cf.getReceiptName())); //receiptname
                                List<String> paperlore = new ArrayList<>();
                                paperlore.add(utilsChat.coloredMessage(cf.getImportLineReceipt().replace("$p", String.valueOf(importo)))); //importlinereceipt
                                paperlore.add(utilsChat.coloredMessage(cf.getDescriptionLineReceipt().replace("$d", descrizione))); //descriptionlinereceipt
                                papermeta.setLore(paperlore);
                                paper.setItemMeta(papermeta);

                                p.getInventory().addItem(paper);
                                main.getPlayerManager().getAzienda(nome_azienda).setMoney((float) (main.getPlayerManager().getAzienda(nome_azienda).getMoney() + importo));
                            } else {
                                utilsChat.sendMessage(from, cf.getNoMoneyLeft());
                            }
                        } else {
                            utilsChat.sendMessage(from, cf.getBusinessNotExist());
                        }
                    } else {
                        utilsChat.sendMessage(from, cf.getPlayerNotOnline());
                    }
                } else {
                    utilsChat.sendMessage(from, cf.getNoPermission());
                }
            } else {
                utilsChat.sendMessage(from, cf.getInvalidCommand());
            }
        }
        return true;
    }
}
