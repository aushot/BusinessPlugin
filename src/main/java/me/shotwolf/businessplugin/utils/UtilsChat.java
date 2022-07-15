package me.shotwolf.businessplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UtilsChat {
    public void sendMessage(Player p, String s){
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
    }
    public String coloredMessage(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
