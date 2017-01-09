package me.clickpt.easysetspawn;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Events implements Listener {
	
	protected static HashMap<Player, BukkitTask> pvp = new HashMap<>();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(Main.hasNewVersion() && p.isOp())
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("check-version.warning-message")));
		
		if(Config.getConfig().getBoolean("broadcast.player-join.enabled")) {
			if(Config.getConfig().getBoolean("broadcast.player-join.hide")) {
				e.setJoinMessage(null);
			}
			else {
				e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("broadcast.player-join.message").replaceAll("%player%", p.getName())));
			}
		}
		
		if(Config.getConfig().getBoolean("join-message.enabled")) {
			for(String message : Config.getConfig().getStringList("join-message.text")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replaceAll("%player%", p.getName())));
			}
		}
		
		if(p.hasPlayedBefore()) {
			if(Config.getConfig().getBoolean("teleport-to-spawn-on.join")) {
				Utils.teleportToSpawn(p);
			}
		}
		else {
			if(Config.getConfig().getBoolean("teleport-to-spawn-on.first-join"))
				Utils.teleportToSpawn(p);
			
			if(Config.getConfig().getBoolean("broadcast.first-join.enabled"))
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("broadcast.first-join.message").replaceAll("%player%", p.getName())));
			
			if(Config.getConfig().getBoolean("first-join-message.enabled")) {
				for(String message : Config.getConfig().getStringList("first-join-message.text")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replaceAll("%player%", p.getName())));
				}
			}
		}
		
		int gm = Config.getConfig().getInt("options.set-gamemode-on-join.gamemode");
		
		if(Config.getConfig().getBoolean("options.set-gamemode-on-join.enabled")) {
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
		
		if(Config.getConfig().getBoolean("options.set-fly-on-join.enabled") && gm != 3)
			p.setAllowFlight(Config.getConfig().getBoolean("options.set-fly-on-join.fly"));
		
		if(Config.getConfig().getBoolean("options.set-max-health-on-join"))
			p.setHealth(20.0);
		
		if(Config.getConfig().getBoolean("options.set-max-food-level-on-join"))
			p.setFoodLevel(20);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		if(Config.getConfig().getBoolean("options.set-fly-on-join.fly"))
			p.setAllowFlight(false);
		
		if(Config.getConfig().getBoolean("broadcast.player-quit.enabled")) {
			if(Config.getConfig().getBoolean("broadcast.player-quit.hide")) {
				e.setQuitMessage(null);
			}
			else {
				e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("broadcast.player-quit.message").replaceAll("%player%", e.getPlayer().getName())));
			}
		}
		
		if(pvp.containsKey(p)) {
			if(pvp.get(p).isSync())
				pvp.get(p).cancel();
		
			pvp.remove(p);
		}
		
		if(Commands.delay.containsKey(p)) {
			if(Commands.delay.get(p).getTask().isSync())
				Commands.delay.get(p).getTask().cancel();
			
			Commands.delay.remove(p);
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if(Config.getConfig().getBoolean("teleport-to-spawn-on.respawn"))
			e.setRespawnLocation(Utils.getSpawnLocation());
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(!((e.getEntity() instanceof Player) && Config.getConfig().getBoolean("disable-spawn-command-in-pvp.enabled")))
			return;
		
		final Player p = (Player) e.getEntity();
		
		if(e.getDamager() instanceof Player) {
			pvp(p, (Player) e.getDamager());
		}
		else if((e.getDamager() instanceof Arrow) && (((Arrow) e.getDamager()).getShooter() instanceof Player)) {
			pvp(p, (Player) ((Arrow) e.getDamager()).getShooter());
		}
	}
	
	@EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player && e.getCause().equals(DamageCause.VOID) && Config.getConfig().getBoolean("teleport-to-spawn-on.void-fall")) {
			Utils.teleportToSpawn((Player) e.getEntity());
			e.setCancelled(true);
		}
    }
	
	private void pvp(final Player p, final Player d) {
		if(pvp.containsKey(p))
			if(pvp.get(p).isSync())
				pvp.get(p).cancel();
		
		pvp.put(p, new BukkitRunnable() {
			
			@Override
			public void run() {
				if(pvp.containsKey(p))
					pvp.remove(p);
			}
			
		}.runTaskLater(Main.getInstance(), 8*20L));
		
		if(pvp.containsKey(d))
			if(pvp.get(d).isSync())
				pvp.get(d).cancel();
		
		pvp.put(d, new BukkitRunnable() {
			
			@Override
			public void run() {
				if(pvp.containsKey(d))
					pvp.remove(d);
			}
			
		}.runTaskLater(Main.getInstance(), 8*20L));
	}
	
}
