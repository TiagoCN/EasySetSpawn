package me.clickpt.easysetspawn.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clickpt.easysetspawn.Main;
import me.clickpt.easysetspawn.Spawn;
import me.clickpt.easysetspawn.Utils;
import me.clickpt.easysetspawn.config.ConfigUtil;

public class SetSpawnCMD implements CommandExecutor {

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
		
		return false;
	}

}
