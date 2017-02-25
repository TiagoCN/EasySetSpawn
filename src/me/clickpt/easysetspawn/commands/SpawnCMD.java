package me.clickpt.easysetspawn.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clickpt.easysetspawn.ConfigUtil;
import me.clickpt.easysetspawn.Main;
import me.clickpt.easysetspawn.Spawn;
import me.clickpt.easysetspawn.Utils;

import me.clickpt.easysetspawn.delay.SpawnDelay;

public class SpawnCMD implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			if(Utils.verifyIfIsAPlayer(sender)) return true;
			
			final Player p = (Player) sender;
			
			if(Main.getConfiguration().getBoolean("spawn-command.need-permission")) {
				if(Utils.hasPermission(p, "spawn")) {
					SpawnDelay.spawn(p);
				}
				else {
					sender.sendMessage(ConfigUtil.getNoPermission());
				}
			}
			else {
				SpawnDelay.spawn(p);
			}
			
			return true;
		}
		
		if(Utils.hasPermission(sender, "teleportothers")) {
			Player target = Bukkit.getServer().getPlayer(args[0]);
			
			if(target == null) {
				sender.sendMessage(ConfigUtil.getPlayerNotFound());
				return true;
			}
			
			Spawn.teleport(target, sender);
		}
		else {
			sender.sendMessage(ConfigUtil.getNoPermission());
		}
		
		return false;
	}

}
