package me.clickpt.easysetspawn.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.clickpt.easysetspawn.Main;
import me.clickpt.easysetspawn.Utils;

import me.clickpt.easysetspawn.delay.SpawnDelay;

public class PlayerQuit implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		if(Main.getConfiguration().getBoolean("options.set-fly-on-join.fly"))
			p.setAllowFlight(false);
		
		if(Main.getConfiguration().getBoolean("broadcast.player-quit.enabled")) {
			e.setQuitMessage(null);
			
			if(!Main.getConfiguration().getBoolean("broadcast.player-quit.hide")) {
				Bukkit.broadcastMessage(Utils.color(Main.getConfiguration().getString("broadcast.player-quit.message").replaceAll("%player%", e.getPlayer().getDisplayName())));
			}
		}
		
		BlockCombat.remove(p);
		SpawnDelay.remove(p);
	}
	
}
