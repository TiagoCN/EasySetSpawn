package me.clickpt.easysetspawn;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
	
	private static Plugin pl;
	
	private static File file;
	private static YamlConfiguration config;
	
	public static void loadConfig(Plugin plugin) {
		pl = plugin;
		
		file = new File(pl.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(file);
		
		if(!pl.getDataFolder().exists())
			pl.getDataFolder().mkdirs();
		
		create();
		
		if(!config.getString("config-version").equalsIgnoreCase(Main.getConfigVersion())) {
			pl.getLogger().warning("CONFIG.YML IS OUT OF DATE!");
			pl.getLogger().warning("USE AN OLD CONFIGURATION CAN GENERATE SEVERAL BUGS.");
			pl.getLogger().warning("PLEASE BACKUP CONFIG AND REMOVE IT TO GENERATE NEW.");
		}
		
		int seconds = config.getInt("teleport-delay-in-seconds");
		
		if(seconds >= 60 || seconds <= 0)
			config.set("teleport-delay-in-seconds", 0);
		
		int gamemode = config.getInt("options.set-gamemode-on-join.gamemode");
		
		if(gamemode != 0 && gamemode != 1 && gamemode != 2 && gamemode != 3)
			config.set("options.set-gamemode-on-join.gamemode", 0);
		
		saveConfigFile();
	}
	
	public static File getFile() {
		return file;
	}
	
	public static YamlConfiguration getConfig() {
		return config;
	}
	
	public static void reloadConfig() {
		loadConfig(pl);
	}
	
	public static void saveConfigFile() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void create() {
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			config.set("teleport-to-spawn-on.first-join", true);
			config.set("teleport-to-spawn-on.join", true);
			config.set("teleport-to-spawn-on.respawn", true);
			config.set("teleport-to-spawn-on.void-fall", false);
			config.set("teleport-delay-in-seconds", 0);
			config.set("check-version.enabled", true);
			config.set("check-version.warning-message", "&6[EasySetSpawn] &7New version available on the official page.");
			config.set("metrics", true);
			config.set("disable-spawn-command-in-pvp.enabled", false);
			config.set("disable-spawn-command-in-pvp.warning-message", "&cYou can not go to spawn when you are in PvP.");
			config.set("options.set-gamemode-on-join.enabled", false);
			config.set("options.set-gamemode-on-join.gamemode", 0);
			config.set("options.set-fly-on-join.enabled", false);
			config.set("options.set-fly-on-join.fly", false);
			config.set("options.set-max-health-on-join", false);
			config.set("options.set-max-food-level-on-join", false);
			config.set("spawn-command.message-enabled", true);
			config.set("spawn-command.message", "&6[EasySetSpawn] &7Teleported to spawn.");
			config.set("spawn-command.need-permission", false);
			config.set("messages.no-permission", "&cYou do not have permission to use this command.");
			config.set("messages.teleported-other-player", "&6[EasySetSpawn] &7%target% teleported to spawn.");
			config.set("messages.spawn-not-set", "&6[EasySetSpawn] &cSpawn has not yet been set.");
			config.set("messages.player-not-found", "&6[EasySetSpawn] &cPlayer not found.");
			config.set("messages.spawn-successfully-set", "&6[EasySetSpawn] &7Spawn successfully set.");
			config.set("messages.config-reloaded", "&6[EasySetSpawn] &7Config reloaded.");
			config.set("messages.console-use-command", "&cOnly players can run this command.");
			config.set("messages.teleport-delay", "&6Teleportation will commence in %seconds% seconds. Do not move.");
			config.set("messages.player-move", "&cTeleportation canceled due to movement.");
			config.set("broadcast.player-join.enabled", false);
			config.set("broadcast.player-join.message", "&6%player% joined the server!");
			config.set("broadcast.player-join.hide", false);
			config.set("broadcast.player-quit.enabled", false);
			config.set("broadcast.player-quit.message", "&6%player% left the server!");
			config.set("broadcast.player-quit.hide", false);
			config.set("broadcast.first-join.enabled", false);
			config.set("broadcast.first-join.message", "&6Welcome, %player%!");
			config.set("join-message.enabled", false);
			config.set("join-message.text", Arrays.asList("&6Welcome %player%!"));
			config.set("first-join-message.enabled", false);
			config.set("first-join-message.text", Arrays.asList("&6Welcome %player%!"));
			config.set("spawn.world", null);
			config.set("spawn.x", null);
			config.set("spawn.y", null);
			config.set("spawn.z", null);
			config.set("spawn.yaw", null);
			config.set("spawn.pitch", null);
			config.set("config-version", Main.getConfigVersion());
			
			saveConfigFile();
		}
	}
	
}
