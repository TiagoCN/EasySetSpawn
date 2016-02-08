package me.clickpt.easysetspawn;

import org.bukkit.ChatColor;

public class ConfigUtils {
	
	public static String no_permission;
	
	public static String player_not_found;
	
	public static String console_use_command;
	
	public static void load() {
		no_permission = ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.no-permission"));
		
		player_not_found = ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.player-not-found"));
		
		console_use_command = ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.console-use-command"));
	}
	
}
