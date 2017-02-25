package me.clickpt.easysetspawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn {
	
	public static void setLocation(Location l) {
		Main.getConfiguration().set("spawn.world", l.getWorld().getName());
		Main.getConfiguration().set("spawn.x", l.getX());
		Main.getConfiguration().set("spawn.y", l.getY());
		Main.getConfiguration().set("spawn.z", l.getZ());
		Main.getConfiguration().set("spawn.yaw", (float) l.getYaw());
		Main.getConfiguration().set("spawn.pitch", (float) l.getPitch());
		
		Main.getInstance().saveConfig();
	}
	
	public static Location getLocation() {
		String wname = Main.getConfiguration().getString("spawn.world");
		
		if(wname == null || wname.equalsIgnoreCase("")) {
			return null;
		} else {
			World w = Bukkit.getServer().getWorld(wname);
			double x = Main.getConfiguration().getDouble("spawn.x");
			double y = Main.getConfiguration().getDouble("spawn.y");
			double z = Main.getConfiguration().getDouble("spawn.z");
			float yaw = Main.getConfiguration().getInt("spawn.yaw");
			float pitch = Main.getConfiguration().getInt("spawn.pitch");
			
			return new Location(w, x, y, z, yaw, pitch);
		}
	}
	
	public static void teleport(Player p) {
		Location l = getLocation();
		
		if(l == null) {
			a(p);
		} else {
			p.teleport(l);
		}
	}
	
	public static void teleport(Player p, boolean message) {
		Location l = getLocation();
		
		if(l == null) {
			a(p);
		} else {
			p.teleport(l);
			
			if(message && Main.getConfiguration().getBoolean("spawn-command.message-enabled"))
				p.sendMessage(Utils.color(Main.getConfiguration().getString("spawn-command.message")));
		}
	}
	
	public static void teleport(Player p, CommandSender sender) {
		Location l = getLocation();
		
		if(l == null) {
			a(p);
		} else {
			p.teleport(l);
			
			p.sendMessage(Utils.color(Main.getConfiguration().getString("spawn-command.message")));
			if(p.getName() != sender.getName())
				sender.sendMessage(Utils.color(Main.getConfiguration().getString("messages.teleported-other-player")).replaceAll("%target%", p.getName()));
		}
	}
	
	private static void a(Player p) {
		Main.getInstance().getLogger().warning("Spawn not set.");
		
		p.sendMessage(Utils.color(Main.getConfiguration().getString("messages.spawn-not-set")));
	}
	
}
