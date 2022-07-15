package me.shotwolf.businessplugin.listener;

import me.shotwolf.businessplugin.Main;
import me.shotwolf.businessplugin.utils.UtilsChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEventListener implements Listener {
    Main main;
    public ChatEventListener(Main main){
        this.main = main;
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player from = e.getPlayer();

        UtilsChat utilsChat = new UtilsChat();
        if(main.getBusinesschat().containsKey(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
            for(Player p : Bukkit.getOnlinePlayers()){
                if(main.getPlayerManager().getDipendente(p.getUniqueId()) != null){
                    if (main.getPlayerManager().getDipendente(p.getUniqueId()).getAzienda() == main.getBusinesschat().get(from.getUniqueId())) {
                        utilsChat.sendMessage(p, from.getDisplayName() + ": &a" + e.getMessage());
                    }
                } else if (main.getPlayerManager().getDirettore(p.getUniqueId()) != null) {
                    if (main.getPlayerManager().getDirettore(p.getUniqueId()).getAzienda() == main.getBusinesschat().get(from.getUniqueId())) {
                        utilsChat.sendMessage(p, from.getDisplayName() + ": &a" + e.getMessage());
                    }
                } else if (main.getPlayerManager().getViceDirettore(p.getUniqueId()) != null) {
                    if (main.getPlayerManager().getViceDirettore(p.getUniqueId()).getAzienda() == main.getBusinesschat().get(from.getUniqueId())) {
                        utilsChat.sendMessage(p, from.getDisplayName() + ": &a" + e.getMessage());
                    }
                }
            }
        }
    }
}
