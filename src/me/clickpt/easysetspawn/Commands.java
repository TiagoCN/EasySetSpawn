package me.clickpt.easysetspawn;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Commands implements CommandExecutor {
	
	protected static HashMap<Player, SPlayer> delay = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("setspawn")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(ConfigUtil.getConsoleUseCommand());
				return true;
			}
			
			Player p = (Player) sender;
			
			if(Utils.hasPermission(p, "setspawn")) {
				Location l = p.getLocation();
				
				Config.getConfig().set("spawn.world", l.getWorld().getName());
				Config.getConfig().set("spawn.x", l.getX());
				Config.getConfig().set("spawn.y", l.getY());
				Config.getConfig().set("spawn.z", l.getZ());
				Config.getConfig().set("spawn.yaw", (float) l.getYaw());
				Config.getConfig().set("spawn.pitch", (float) l.getPitch());
				
				Config.saveConfigFile();
				
				p.getWorld().setSpawnLocation((int) l.getX(), (int) l.getY(), (int) l.getZ());
				
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.spawn-successfully-set")));
			}
			else {
				p.sendMessage(ConfigUtil.getNoPermission());
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("spawn")) {
			if(args.length == 0) {
				if(!(sender instanceof Player)) {
					sender.sendMessage(ConfigUtil.getConsoleUseCommand());
					return true;
				}
				
				final Player p = (Player) sender;
				
				if(Config.getConfig().getBoolean("spawn-command.need-permission")) {
					if(Utils.hasPermission(p, "spawn")) {
						spawnCommand(p);
					}
					else {
						sender.sendMessage(ConfigUtil.getNoPermission());
					}
				}
				else {
					spawnCommand(p);
				}
				
				return true;
			}
			
			if(Utils.hasPermission(sender, "teleportothers")) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				
				if(target == null) {
					sender.sendMessage(ConfigUtil.getPlayerNotFound());
					return true;
				}
				
				Utils.teleportToSpawn(target, sender);
			}
			else {
				sender.sendMessage(ConfigUtil.getNoPermission());
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("easyss") || cmd.getName().equalsIgnoreCase("ess")) {
			if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
				if(Utils.hasPermission(sender, "help")) {
					sender.sendMessage("§7===== §bEasySetSpawn §7=====");
					sender.sendMessage("§b/spawn §7- Teleport to spawn.");
					sender.sendMessage("§b/setspawn §7- Set spawn.");
					sender.sendMessage("§b/" + cmd.getName() + " or /" + cmd.getName() + " help §7- Commands list.");
					sender.sendMessage("§b/" + cmd.getName() + " info §7- Plugin info.");
					sender.sendMessage("§b/" + cmd.getName() + " setdelay §7- Set spawn delay. 0 = no delay");
					sender.sendMessage("§b/" + cmd.getName() + " reload §7- Reload config.");
				}
				else {
					sender.sendMessage(ConfigUtil.getNoPermission());
				}
			}
			else if(args[0].equalsIgnoreCase("info")) {
				if(Utils.hasPermission(sender, "info")) {
					sender.sendMessage("§bEasySetSpawn§7 version §b" + Main.getPluginVersion() + " §7created by §bClickPT§7.");
				}
				else {
					sender.sendMessage(ConfigUtil.getNoPermission());
				}
			}
			else if(args[0].equalsIgnoreCase("setdelay")) {
				if(Utils.hasPermission(sender, "setdelay")) {
					if(args.length == 1) {
						sender.sendMessage("§7Currently teleport delay: §b" + Config.getConfig().getInt("teleport-delay-in-seconds") + " seconds§7.");
					}
					else {
						try {
							Config.getConfig().set("teleport-delay-in-seconds", Integer.parseInt(args[1]));
							Config.saveConfigFile();
							
							sender.sendMessage("§7Teleport delay changed to: §b" + Integer.parseInt(args[1]) + " seconds§7.");
						} catch(Exception e) {
							sender.sendMessage("§cYou can only use numbers.");
						}
					}
				}
				else {
					sender.sendMessage(ConfigUtil.getNoPermission());
				}
			}
			else if(args[0].equalsIgnoreCase("reload")) {
				if(Utils.hasPermission(sender, "reload")) {
					Config.reloadConfig();
					
					if(sender instanceof Player)
						Bukkit.getLogger().info("[EasySetSpawn] Config reloaded.");
					
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.config-reloaded")));
				}
				else {
					sender.sendMessage(ConfigUtil.getNoPermission());
				}
			}
			else {
				if(Utils.hasPermission(sender, "help")) {
					sender.sendMessage("§cArgument not found. For help use: /" + cmd.getName() + " help");
				}
				else {
					sender.sendMessage(ConfigUtil.getNoPermission());
				}
			}
		}
		
		return false;
	}
	
	private void spawnCommand(final Player p) {
		if(Events.pvp.containsKey(p) && (!Utils.hasPermission(p, "bypasscmdblock"))) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("disable-spawn-command-in-pvp.warning-message")));
		}
		else {
			if(Utils.hasPermission(p, "bypassdelay") || Config.getConfig().getInt("teleport-delay-in-seconds") <= 0) {
				Utils.teleportToSpawn(p, true);
			}
			else {
				if(delay.containsKey(p))
					if(delay.get(p).getTask().isSync())
						delay.get(p).getTask().cancel();
				
				Location l = p.getLocation();
				
				delay.put(p, new SPlayer(new BukkitRunnable() {
					
					@Override
					public void run() {
						if(delay.get(p).getI() >= Config.getConfig().getInt("teleport-delay-in-seconds")) {
							delay.get(p).setI(0);
							Utils.teleportToSpawn(p, true);
							cancel();
						}
						else {
							delay.get(p).setI(delay.get(p).getI() + 1);
							
							if(delay.get(p).getStartX() != (int) p.getLocation().getX() || 
									delay.get(p).getStartY() != (int) p.getLocation().getY() || 
									delay.get(p).getStartZ() != (int) p.getLocation().getZ()) {
								delay.get(p).setI(0);
								cancel();
								
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.player-move")));
							}
						}
					}
					
				}.runTaskTimer(Main.getInstance(), 20L, 20L), (int) l.getX(), (int) l.getY(), (int) l.getZ()));
				
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("messages.teleport-delay")).replaceAll("%seconds%", Config.getConfig().getInt("teleport-delay-in-seconds") + ""));
			}
		}
	}
	
}
