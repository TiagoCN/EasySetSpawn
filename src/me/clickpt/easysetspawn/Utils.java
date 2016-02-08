package me.clickpt.easysetspawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {
	
	public static void teleportToSpawn(Player player) {
		try {
			World w = Bukkit.getServer().getWorld(Config.getConfig().getString("spawn.world"));
			double x = Config.getConfig().getDouble("spawn.x");
			double y = Config.getConfig().getDouble("spawn.y");
			double z = Config.getConfig().getDouble("spawn.z");
			float yaw = Config.getConfig().getInt("spawn.yaw");
			float pitch = Config.getConfig().getInt("spawn.pitch");
			
			player.teleport(new Location(w, x, y, z, yaw, pitch));
		} catch(Exception e) {
			Bukkit.getLogger().warning("[EasySetSpawn] Spawn not set.");
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.spawn-not-set")));
		}
	}
	
	public static void teleportToSpawn(Player player, boolean message) {
		try {
			World w = Bukkit.getServer().getWorld(Config.getConfig().getString("spawn.world"));
			double x = Config.getConfig().getDouble("spawn.x");
			double y = Config.getConfig().getDouble("spawn.y");
			double z = Config.getConfig().getDouble("spawn.z");
			float yaw = Config.getConfig().getInt("spawn.yaw");
			float pitch = Config.getConfig().getInt("spawn.pitch");
			
			player.teleport(new Location(w, x, y, z, yaw, pitch));
			
			if(message)
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.command-teleport")));
		} catch(Exception e) {
			Bukkit.getLogger().warning("[EasySetSpawn] Spawn not set.");
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.spawn-not-set")));
		}
	}
	
	public static void teleportToSpawn(Player player, CommandSender sender) {
		try {
			World w = Bukkit.getServer().getWorld(Config.getConfig().getString("spawn.world"));
			double x = Config.getConfig().getDouble("spawn.x");
			double y = Config.getConfig().getDouble("spawn.y");
			double z = Config.getConfig().getDouble("spawn.z");
			float yaw = Config.getConfig().getInt("spawn.yaw");
			float pitch = Config.getConfig().getInt("spawn.pitch");
			
			player.teleport(new Location(w, x, y, z, yaw, pitch));
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.command-teleport")));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.teleported-other-player")).replaceAll("%target%", player.getName()));
		} catch(Exception e) {
			Bukkit.getLogger().warning("[EasySetSpawn] Spawn not set.");
			
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.spawn-not-set")));
		}
	}
	
	public static boolean hasPermission(Player player, String permission) {
		return player.hasPermission("easysetspawn." + permission) || player.hasPermission("easysetspawn.*");
	}
	
	public static boolean hasPermission(CommandSender sender, String permission) {
		return sender.hasPermission("easysetspawn." + permission) || sender.hasPermission("easysetspawn.*");
	}
	
}
