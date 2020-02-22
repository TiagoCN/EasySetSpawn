package me.clickpt.easysetspawn.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.clickpt.easysetspawn.Main;
import me.clickpt.easysetspawn.Spawn;
import me.clickpt.easysetspawn.Utils;
import me.clickpt.easysetspawn.config.ConfigUtil;

public class SetSpawnCMD implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(Utils.verifyIfIsAPlayer(sender)) return true;
		
		Player p = (Player) sender;
		
		if(Utils.hasPermission(p, "setspawn")) {
			Location l = p.getLocation();
			
			Spawn.setLocation(l);
			
			p.getWorld().setSpawnLocation((int) l.getX(), (int) l.getY(), (int) l.getZ());
			
			p.sendMessage(Utils.color(Main.getConfiguration().getString("messages.spawn-successfully-set")));
		}
		else {
			p.sendMessage(ConfigUtil.getNoPermission());
		}
		
		return true;
	}
	
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Collections.emptyList();
	}

}
