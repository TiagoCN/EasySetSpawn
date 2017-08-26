package me.clickpt.easysetspawn.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clickpt.easysetspawn.Main;
import me.clickpt.easysetspawn.Utils;
import me.clickpt.easysetspawn.config.Config;
import me.clickpt.easysetspawn.config.ConfigUtil;

public class EasySSCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
			if(Utils.hasPermission(sender, "help")) {
				sender.sendMessage("§7===== §bEasySetSpawn §7=====");
				sender.sendMessage("§b/spawn §7- Teleport to spawn.");
				sender.sendMessage("§b/setspawn §7- Set spawn.");
				sender.sendMessage("§b/" + cmd.getName() + " or /" + cmd.getName() + " help §7- Commands list.");
				sender.sendMessage("§b/" + cmd.getName() + " info §7- Plugin info.");
				sender.sendMessage("§b/" + cmd.getName() + " setdelay [seconds] §7- Set spawn delay. 0 = no delay");
				sender.sendMessage("§b/" + cmd.getName() + " reload §7- Reload config.");
			}
			else {
				sender.sendMessage(ConfigUtil.getNoPermission());
			}
		}
		else if(args[0].equalsIgnoreCase("info")) {
			if(Utils.hasPermission(sender, "info")) {
				sender.sendMessage("§bEasySetSpawn§7 version §b" + Main.getPluginVersion() + " §7created by §bClickPT§7.");
				sender.sendMessage("§8Bukkit Page: §7https://dev.bukkit.org/projects/easysetspawn");
				sender.sendMessage("§8Spigot Page: §7https://www.spigotmc.org/resources/easysetspawn.9961/");
			}
			else {
				sender.sendMessage(ConfigUtil.getNoPermission());
			}
		}
		else if(args[0].equalsIgnoreCase("setdelay")) {
			if(Utils.hasPermission(sender, "setdelay")) {
				if(args.length == 1) {
					sender.sendMessage("§7Currently teleport delay: §b" + Main.getConfiguration().getInt("teleport-delay-in-seconds") + " seconds§7.");
				}
				else {
					try {
						Main.getConfiguration().set("teleport-delay-in-seconds", Integer.parseInt(args[1]));
						Main.getInstance().saveConfig();
						
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
				Main.getInstance().reloadConfig();
				Config.testConfig();
				
				if(sender instanceof Player)
					Main.getInstance().getLogger().info("Config reloaded.");
				
				sender.sendMessage(Utils.color(Main.getConfiguration().getString("messages.config-reloaded")));
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
		
		return false;
	}

}
