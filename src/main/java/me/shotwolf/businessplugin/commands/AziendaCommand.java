package me.shotwolf.businessplugin.commands;

import me.shotwolf.businessplugin.Main;
import me.shotwolf.businessplugin.configfile.ConfigFile;
import me.shotwolf.businessplugin.dipendente.Azienda;
import me.shotwolf.businessplugin.dipendente.Dipendente;
import me.shotwolf.businessplugin.dipendente.Direttore;
import me.shotwolf.businessplugin.dipendente.ViceDirettore;
import me.shotwolf.businessplugin.utils.UtilsChat;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class AziendaCommand implements CommandExecutor {
    private Main main;
    private ConfigFile cf;
    private UtilsChat utilsChat = new UtilsChat();

    public AziendaCommand(Main main) {
        this.main = main;
    }

    public void addPermission(User user, String permission) {
        user.data().add(Node.builder(permission).build());
        main.getApi().getUserManager().saveUser(user);
    }

    public void removePermission(User user, String permission) {
        user.data().remove(Node.builder(permission).build());
        main.getApi().getUserManager().saveUser(user);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            cf = new ConfigFile(main);
            Player p = (Player) sender;

            switch (args.length) { //controlla quanti arguments ci sono
                case 0:
                    utilsChat.sendMessage(p, cf.getInvalidCommand()); //da fare nel config
                    break;
                case 1:
                    switch (args[0]) { //controlla il primo args
                        case "list":
                            if (p.hasPermission("azienda.list")) {
                                utilsChat.sendMessage(p, cf.getAziendaList());
                                utilsChat.sendMessage(p, main.getPlayerManager().printAzienda(main));
                            } else {
                                utilsChat.sendMessage(p, cf.getNoPermission());
                            }
                            return true;
                        case "reload":
                            if (p.hasPermission("azienda.reload")) {
                                main.reloadConfig();
                                utilsChat.sendMessage(p, "Plugin reloaded"); //da fare nel config
                            } else {
                                utilsChat.sendMessage(p, cf.getNoPermission());
                            }
                            return true;
                        default:
                            utilsChat.sendMessage(p, cf.getInvalidCommand());
                            return false;
                    }
                case 3:
                    if (p.hasPermission("azienda.admin")) {
                        switch (args[0]) {//controlla il primo args
                            case "create":
                                Player target = Bukkit.getPlayerExact(args[2]);

                                if (target != null) {
                                    if (main.getPlayerManager().getDirettore(target.getUniqueId()) == null) {
                                        try {
                                            Azienda azienda = new Azienda(main, args[1], p);
                                            Direttore direttore = new Direttore(main, target.getUniqueId(), azienda, target);

                                            main.getPlayerManager().addAzienda(args[1], azienda);
                                            main.getPlayerManager().addDirettore(target.getUniqueId(), direttore);

                                            User user = main.getApi().getUserManager().getUser(target.getUniqueId());
                                            addPermission(user, "direttore." + args[1].toLowerCase());
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            utilsChat.sendMessage(p, cf.getSQLError());
                                        }
                                    } else {
                                        utilsChat.sendMessage(p, cf.getArleadyDirector());
                                    }
                                } else {
                                    utilsChat.sendMessage(p, "il player non esiste o non è online"); // config yml
                                }
                                return true;
                            case "delete":
                                String nome_azienda = args[1];
                                target = Bukkit.getPlayerExact(args[2]);

                                if (main.getPlayerManager().getAzienda(nome_azienda) != null) {
                                    if (target != null && main.getPlayerManager().getDirettore(target.getUniqueId()) != null && main.getPlayerManager().getDirettore(target.getUniqueId()).getAzienda().getName().equalsIgnoreCase(nome_azienda)) {
                                        main.getPlayerManager().removeDirettore(main, p.getUniqueId());
                                        main.getPlayerManager().removeAzienda(main, nome_azienda);

                                        User user = main.getApi().getUserManager().getUser(p.getUniqueId());
                                        removePermission(user, "direttore." + args[1].toLowerCase());

                                        utilsChat.sendMessage(p, cf.getBusinessDeleted());
                                    } else {
                                        utilsChat.sendMessage(p, "non è direttore dell'azienda"); // config yml
                                    }
                                } else {
                                    utilsChat.sendMessage(p, cf.getBusinessNotExist());
                                }
                                return true;
                            default:
                                utilsChat.sendMessage(p, cf.getInvalidCommand());
                                return false;
                        }
                    }

                    String nome_azienda = args[0];

                    if (p.hasPermission("direttore." + nome_azienda)) { //azienda (AZIENDA) add/remove (PLAYER)
                        if (main.getPlayerManager().getAzienda(nome_azienda) != null) {
                            if (Bukkit.getServer().getPlayer(args[2]) != null) {
                                User user;
                                UUID target = Bukkit.getServer().getPlayerExact(args[2]).getUniqueId();

                                switch (args[1].toLowerCase()) {
                                    case "promote": //azienda (AZIENDA) promote/degrade (PLAYER)
                                        if (main.getPlayerManager().getDipendente(target) != null) {
                                            if (main.getPlayerManager().getDipendente(target).getAzienda().getName().equalsIgnoreCase(nome_azienda)) {
                                                Azienda azienda = main.getPlayerManager().getAzienda(nome_azienda);
                                                user = main.getApi().getUserManager().getUser(target);

                                                addPermission(user, "vicedirettore." + nome_azienda.toLowerCase());
                                                removePermission(user, "dipendente." + nome_azienda.toLowerCase());
                                                main.getPlayerManager().removeDipendente(main, target);

                                                ViceDirettore viceDirettore = new ViceDirettore(main, target, azienda, Bukkit.getPlayer(target));
                                                main.getPlayerManager().addViceDirettore(target, viceDirettore);

                                                utilsChat.sendMessage(p, cf.getSubDirectorAdded());
                                            }
                                        }
                                        return true;
                                    case "degrade":
                                        if (main.getPlayerManager().getViceDirettore(target) != null) {
                                            if (main.getPlayerManager().getViceDirettore(target).getAzienda().getName().equalsIgnoreCase(nome_azienda)) {
                                                Azienda azienda = main.getPlayerManager().getAzienda(nome_azienda);
                                                user = main.getApi().getUserManager().getUser(target);

                                                main.getPlayerManager().removeViceDirettore(main, target);
                                                removePermission(user, "vicedirettore." + nome_azienda.toLowerCase());

                                                addPermission(user, "dipendente." + nome_azienda.toLowerCase());
                                                Dipendente dipendente = new Dipendente(main, target, azienda, p);
                                                main.getPlayerManager().addDipendente(target, dipendente);

                                                utilsChat.sendMessage(p, cf.getSubDirectorRemoved());
                                            }
                                        }
                                        return true;
                                    case "add":
                                        if (main.getPlayerManager().getDirettore(target) == null) {

                                            Azienda azienda = main.getPlayerManager().getAzienda(nome_azienda);

                                            user = main.getApi().getUserManager().getUser(target);
                                            addPermission(user, "dipendente." + nome_azienda.toLowerCase());

                                            Dipendente dipendente = new Dipendente(main, target, azienda, p);
                                            main.getPlayerManager().addDipendente(target, dipendente);
                                        } else {
                                            utilsChat.sendMessage(p, cf.getInvalidCommand());
                                        }
                                        return true;
                                    case "remove":
                                        target = Bukkit.getServer().getPlayerExact(args[2]).getUniqueId();
                                        if (main.getPlayerManager().getDipendente(target) != null) {
                                            if (main.getPlayerManager().getDipendente(target).getAzienda().getName().equalsIgnoreCase(nome_azienda)) {
                                                if (main.getPlayerManager().getAzienda(nome_azienda) != null) {
                                                    main.getPlayerManager().removeDipendente(main, target);

                                                    user = main.getApi().getUserManager().getUser(target);
                                                    removePermission(user, "dipendente." + nome_azienda.toLowerCase());

                                                    utilsChat.sendMessage(p, cf.getEmployerDeleted()); //employerdeleted
                                                } else {
                                                    utilsChat.sendMessage(p, cf.getBusinessNotExist());
                                                }
                                            } else {
                                                utilsChat.sendMessage(p, cf.getEmployerNotInThatBusiness()); //employernotinthatbusiness
                                            }
                                        } else {
                                            utilsChat.sendMessage(p, cf.getPlayerNotEmployer()); //playernotemployer
                                        }

                                        return true;
                                }
                            } else {
                                utilsChat.sendMessage(p, cf.getPlayerNotOnline()); //PlayerNotOnline
                                return false;
                            }
                        } else {
                            utilsChat.sendMessage(p, cf.getBusinessNotExist());
                            return false;
                        }
                    } else {
                        utilsChat.sendMessage(p, cf.getNoPermission());
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
