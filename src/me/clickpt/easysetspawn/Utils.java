package me.clickpt.easysetspawn;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {
	
	public static String color(String arg0) {
		return ChatColor.translateAlternateColorCodes('&', arg0);
	}
	
	public static boolean verifyIfIsAPlayer(CommandSender sender) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ConfigUtil.getConsoleUseCommand());
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean hasPermission(Player player, String permission) {
		return player.hasPermission("easysetspawn." + permission) || player.hasPermission("easysetspawn.*");
	}
	
	public static boolean hasPermission(CommandSender sender, String permission) {
		return sender.hasPermission("easysetspawn." + permission) || sender.hasPermission("easysetspawn.*");
	}
	
}
