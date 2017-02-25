package me.clickpt.easysetspawn.delay;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.clickpt.easysetspawn.Main;
import me.clickpt.easysetspawn.Spawn;
import me.clickpt.easysetspawn.Utils;

import me.clickpt.easysetspawn.listeners.BlockCombat;

public class SpawnDelay {
	
	public static HashMap<Player, Delay> delay = new HashMap<>();	
	
	public static void spawn(final Player p) {
		if(BlockCombat.containsKey(p) && (!Utils.hasPermission(p, "bypasscmdblock"))) {
			p.sendMessage(Utils.color(Main.getConfiguration().getString("disable-spawn-command-in-pvp.warning-message")));
		}
		else {
			if(Utils.hasPermission(p, "bypassdelay") || Main.getConfiguration().getInt("teleport-delay-in-seconds") <= 0) {
				Spawn.teleport(p, true);
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
							
							Spawn.teleport(p, true);
							
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
	
	public static void remove(Player p) {
		if(delay.containsKey(p)) {
			if(delay.get(p).getTask().isSync())
				delay.get(p).getTask().cancel();
			
			delay.remove(p);
		}
	}
	
}
