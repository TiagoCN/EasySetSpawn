package me.clickpt.easysetspawn;

import org.bukkit.ChatColor;

public class ConfigUtil {
	
	public static String getNoPermission() {
		return ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.no-permission"));
	}
	
	public static String getPlayerNotFound() {
		return ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.player-not-found"));
	}
	
	public static String getConsoleUseCommand() {
		return ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.console-use-command"));
	}
	
}
