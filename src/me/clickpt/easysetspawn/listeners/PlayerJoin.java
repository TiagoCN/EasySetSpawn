package me.clickpt.easysetspawn.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.clickpt.easysetspawn.Main;
import me.clickpt.easysetspawn.Spawn;
import me.clickpt.easysetspawn.Utils;

public class PlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(Main.hasNewVersion() && p.isOp())
			p.sendMessage(Utils.color(Main.getConfiguration().getString("check-version.warning-message")));
		
		if(Main.getConfiguration().getBoolean("broadcast.player-join.enabled")) {
			e.setJoinMessage(null);
			
			if(!Main.getConfiguration().getBoolean("broadcast.player-join.hide")) {
				Bukkit.broadcastMessage(Utils.color(Main.getConfiguration().getString("broadcast.player-join.message").replaceAll("%player%", p.getDisplayName())));
			}
		}
		
		if(Main.getConfiguration().getBoolean("join-message.enabled")) {
			for(String message : Main.getConfiguration().getStringList("join-message.text")) {
				p.sendMessage(Utils.color(message.replaceAll("%player%", p.getName())));
			}
		}
		
		if(p.hasPlayedBefore()) {
			if(Main.getConfiguration().getBoolean("teleport-to-spawn-on.join")) {
				Spawn.teleport(p);
			}
		}
		else {
			if(Main.getConfiguration().getBoolean("teleport-to-spawn-on.first-join"))
				Spawn.teleport(p);
			
			if(Main.getConfiguration().getBoolean("broadcast.first-join.enabled"))
				Bukkit.broadcastMessage(Utils.color(Main.getConfiguration().getString("broadcast.first-join.message").replaceAll("%player%", p.getDisplayName())));
			
			if(Main.getConfiguration().getBoolean("first-join-message.enabled")) {
				for(String message : Main.getConfiguration().getStringList("first-join-message.text")) {
					p.sendMessage(Utils.color(message.replaceAll("%player%", p.getName())));
				}
			}
		}
		
		int gm = Main.getConfiguration().getInt("options.set-gamemode-on-join.gamemode");
		
		if(Main.getConfiguration().getBoolean("options.set-gamemode-on-join.enabled")) {
			if(gm == 0) {
				p.setGameMode(GameMode.SURVIVAL);
			}
			else if(gm == 1) {
				p.setGameMode(GameMode.CREATIVE);
			}
			else if(gm == 2) {
				p.setGameMode(GameMode.ADVENTURE);
			}
			else if(gm == 3) {
				p.setGameMode(GameMode.SPECTATOR);
			}
		}
		
		if(Main.getConfiguration().getBoolean("options.set-fly-on-join.enabled") && gm != 3)
			p.setAllowFlight(Main.getConfiguration().getBoolean("options.set-fly-on-join.fly"));
		
		if(Main.getConfiguration().getBoolean("options.set-max-health-on-join"))
			p.setHealth(20.0);
		
		if(Main.getConfiguration().getBoolean("options.set-max-food-level-on-join"))
			p.setFoodLevel(20);
	}
	
}
