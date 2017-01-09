package me.clickpt.easysetspawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {
	
	public static Location getSpawnLocation() {
		String wname = Config.getConfig().getString("spawn.world");
		
		if(wname == null || wname.equalsIgnoreCase("")) {
			return null;
		} else {
			World w = Bukkit.getServer().getWorld(wname);
			double x = Config.getConfig().getDouble("spawn.x");
			double y = Config.getConfig().getDouble("spawn.y");
			double z = Config.getConfig().getDouble("spawn.z");
			float yaw = Config.getConfig().getInt("spawn.yaw");
			float pitch = Config.getConfig().getInt("spawn.pitch");
			
			return new Location(w, x, y, z, yaw, pitch);
		}
	}
	
	public static void teleportToSpawn(Player player) {
		Location l = getSpawnLocation();
		
		if(l == null) {
			Main.getInstance().getLogger().warning("Spawn not set.");
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.spawn-not-set")));
		} else {
			player.teleport(l);
		}
	}
	
	public static void teleportToSpawn(Player player, boolean message) {
		Location l = getSpawnLocation();
		
		if(l == null) {
			Main.getInstance().getLogger().warning("Spawn not set.");
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.spawn-not-set")));
		} else {
			player.teleport(l);
			
			if(message && Config.getConfig().getBoolean("spawn-command.message-enabled"))
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("spawn-command.message")));
		}
	}
	
	public static void teleportToSpawn(Player player, CommandSender sender) {
		Location l = getSpawnLocation();
		
		if(l == null) {
			Main.getInstance().getLogger().warning("Spawn not set.");
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.spawn-not-set")));
		} else {
			player.teleport(l);
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("spawn-command.message")));
			if(player.getName() != sender.getName())
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.teleported-other-player")).replaceAll("%target%", player.getName()));
		}
	}
	
	public static boolean hasPermission(Player player, String permission) {
		return player.hasPermission("easysetspawn." + permission) || player.hasPermission("easysetspawn.*");
	}
	
	public static boolean hasPermission(CommandSender sender, String permission) {
		return sender.hasPermission("easysetspawn." + permission) || sender.hasPermission("easysetspawn.*");
	}
	
}
