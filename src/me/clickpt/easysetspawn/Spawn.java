package me.clickpt.easysetspawn;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.clickpt.easysetspawn.listeners.BlockCombat;

public class Spawn {
	
	public static HashMap<Player, Delay> delay = new HashMap<>();
	
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
	
	public static void teleport(Player p, boolean message, CommandSender sender) {
		Location l = getLocation();
		
		if(l == null) {
			Main.getInstance().getLogger().warning("Spawn not set.");
			
			p.sendMessage(Utils.color(Main.getConfiguration().getString("messages.spawn-not-set")));
		} else {
			if(!l.getChunk().isLoaded())
				l.getChunk().load();
			
			p.teleport(l);
			
			if((sender != null) || (message && Main.getConfiguration().getBoolean("spawn-command.message-enabled"))) {
				p.sendMessage(Utils.color(Main.getConfiguration().getString("spawn-command.message")));
				
				if(sender != null)
					if(p.getName() != sender.getName())
						sender.sendMessage(Utils.color(Main.getConfiguration().getString("messages.teleported-other-player")).replaceAll("%target%", p.getName()));
			}
		}
	}
	
	public static void spawn(final Player p) {
		if(BlockCombat.containsKey(p) && (!Utils.hasPermission(p, "bypasscmdblock"))) {
			p.sendMessage(Utils.color(Main.getConfiguration().getString("disable-spawn-command-in-pvp.warning-message")));
		}
		else {
			if(Utils.hasPermission(p, "bypassdelay") || Main.getConfiguration().getInt("teleport-delay-in-seconds") <= 0) {
				Spawn.teleport(p, true, null);
			}
			else {
				if(delay.containsKey(p))
					if(delay.get(p).getTask().isSync())
						delay.get(p).getTask().cancel();
				
				Location l = p.getLocation();
				
				delay.put(p, new Delay(new BukkitRunnable() {
					
					@Override
					public void run() {
						if(delay.get(p).getI() >= Main.getConfiguration().getInt("teleport-delay-in-seconds")) {
							delay.get(p).setI(0);
							
							Spawn.teleport(p, true, null);
							
							cancel();
						}
						else {
							delay.get(p).setI(delay.get(p).getI() + 1);
							
							if(delay.get(p).getStartX() != (int) p.getLocation().getX() || 
									delay.get(p).getStartY() != (int) p.getLocation().getY() || 
									delay.get(p).getStartZ() != (int) p.getLocation().getZ()) {
								delay.get(p).setI(0);
								
								cancel();
								
								p.sendMessage(Utils.color(Main.getConfiguration().getString("messages.player-move")));
							}
						}
					}
					
				}.runTaskTimer(Main.getInstance(), 20L, 20L), (int) l.getX(), (int) l.getY(), (int) l.getZ()));
				
				p.sendMessage(Utils.color(Main.getConfiguration().getString("messages.teleport-delay")).replaceAll("%seconds%", Main.getConfiguration().getInt("teleport-delay-in-seconds") + ""));
			}
		}
	}
	
	public static void removeDelay(Player p) {
		if(delay.containsKey(p)) {
			if(delay.get(p).getTask().isSync())
				delay.get(p).getTask().cancel();
			
			delay.remove(p);
		}
	}
	
}
