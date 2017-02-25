package me.clickpt.easysetspawn;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
	
	private Plugin pl;
	
	public Config(Plugin pl) {
		this.pl = pl;
	}
	
	public void createConfig() {
    	pl.getConfig().options().copyDefaults(true);
    	pl.saveDefaultConfig();
	}
	
	public void checkForUpdate() {
		File file1 = new File(pl.getDataFolder(), "config.yml");
		YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
		
		if(pl.getDataFolder().exists() && file1.exists()) {
			if(config1.getInt("config-version") < Main.getConfigVersion()) {
				File file2 = new File(pl.getDataFolder(), "oldconfig.yml");
				
				file1.renameTo(file2);
				try {
					file2.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				file1.delete();
				
				pl.getLogger().warning("OLD CONFIG REMOVED!");
				pl.getLogger().warning("NEW CONFIG GENERATED! PLEASE CONSIDER MODIFYING THE CONFIG.");
			}
		}
	}
	
	public void convertOldConfig() {
		File file = new File(pl.getDataFolder(), "oldconfig.yml");
		
		if(pl.getDataFolder().exists() && file.exists() && (pl.getConfig().getString("spawn.world") == null)) {
			YamlConfiguration oldconfig = YamlConfiguration.loadConfiguration(file);
			
			if(oldconfig.getString("spawn.world") != null) {
				pl.getConfig().set("spawn.world", oldconfig.getString("spawn.world"));
				pl.getConfig().set("spawn.x", oldconfig.getDouble("spawn.x"));
				pl.getConfig().set("spawn.y", oldconfig.getDouble("spawn.y"));
				pl.getConfig().set("spawn.z", oldconfig.getDouble("spawn.z"));
				pl.getConfig().set("spawn.yaw", (float) oldconfig.getDouble("spawn.yaw"));
				pl.getConfig().set("spawn.pitch", (float) oldconfig.getDouble("spawn.pitch"));
				
				pl.saveConfig();
			}
		}
	}
	
	public static void testConfig() {
		int seconds = Main.getConfiguration().getInt("teleport-delay-in-seconds");
		if(seconds >= 60 || seconds <= 0)
			Main.getConfiguration().set("teleport-delay-in-seconds", 0);
		
		int gamemode = Main.getConfiguration().getInt("options.set-gamemode-on-join.gamemode");
		if(gamemode != 0 && gamemode != 1 && gamemode != 2 && gamemode != 3)
			Main.getConfiguration().set("options.set-gamemode-on-join.gamemode", 0);
		
		Main.getInstance().saveConfig();
	}
	
}
