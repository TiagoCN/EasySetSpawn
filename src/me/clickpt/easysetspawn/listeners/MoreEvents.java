package me.clickpt.easysetspawn.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.clickpt.easysetspawn.Main;
import me.clickpt.easysetspawn.Spawn;

public class MoreEvents implements Listener {
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if(Main.getConfiguration().getBoolean("teleport-to-spawn-on.respawn"))
			e.setRespawnLocation(Spawn.getLocation());
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player && e.getCause().equals(DamageCause.VOID) && Main.getConfiguration().getBoolean("teleport-to-spawn-on.void-fall")) {
			Spawn.teleport(((Player) e.getEntity()), false, null);
			
			e.setCancelled(true);
		}
	}
	
}
