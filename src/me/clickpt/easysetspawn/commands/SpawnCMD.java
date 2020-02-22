package me.clickpt.easysetspawn.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.clickpt.easysetspawn.Main;
import me.clickpt.easysetspawn.Spawn;
import me.clickpt.easysetspawn.Utils;
import me.clickpt.easysetspawn.config.ConfigUtil;

public class SpawnCMD implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			if(Utils.verifyIfIsAPlayer(sender)) return true;
			
			final Player p = (Player) sender;
			
			if(Main.getConfiguration().getBoolean("spawn-command.need-permission")) {
				if(Utils.hasPermission(p, "spawn")) {
					Spawn.spawn(p);
				}
				else {
					sender.sendMessage(ConfigUtil.getNoPermission());
				}
			}
			else {
				Spawn.spawn(p);
			}
			
			return true;
		}
		
		if(Utils.hasPermission(sender, "teleportothers")) {
			Player target = Bukkit.getServer().getPlayer(args[0]);
			
			if(target == null) {
				sender.sendMessage(ConfigUtil.getPlayerNotFound());
				return true;
			}
			
			Spawn.teleport(target, true, sender);
		}
		else {
			sender.sendMessage(ConfigUtil.getNoPermission());
		}
		
		return true;
	}
	
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1 && Utils.hasPermission(sender, "teleportothers")) {
			return null; // Bukkit will list online players.
		}
		return Collections.emptyList();
	}

}
