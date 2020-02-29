package me.clickpt.easysetspawn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.clickpt.easysetspawn.commands.*;
import me.clickpt.easysetspawn.config.Config;
import me.clickpt.easysetspawn.listeners.*;

public class Main extends JavaPlugin {

	private static Main instance;
	
	private static boolean new_version = false;
	
	private static String config_version = "3.1";
	
	public void onEnable() {
		instance = this;
		
		Config c = new Config(this);
		c.checkVersion();
		c.createConfig();
		c.convertOldConfig();
		Config.testConfig();
		
		registerCommands();
		registerListeners();
		
		Location lspawn = Spawn.getLocation();
		if(lspawn != null) {
			lspawn.getWorld().setSpawnLocation((int) lspawn.getX(), (int) lspawn.getY(), (int) lspawn.getZ());
		}
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
			
			@Override
			public void run() {
				try {
					checkVersion();
				} catch (IOException e1) {
					getLogger().warning("Error checking updates!");
				}
			}
			
		}, 1);
		
		if(getConfig().getBoolean("metrics")) {
			new MetricsLite(this);
		}

		getLogger().info("Enabled!");
	}

	public void onDisable() {
		getLogger().info("Disabled!");
	}

	// -------------------------------------
	
	private void registerCommands() {
		SetSpawnCMD setSpawnCmd = new SetSpawnCMD();
		SpawnCMD spawnCmd = new SpawnCMD();
		EasySSCMD easyssCmd = new EasySSCMD();
		getCommand("setspawn").setExecutor(setSpawnCmd);
		getCommand("spawn").setExecutor(spawnCmd);
		getCommand("easyss").setExecutor(easyssCmd);
		getCommand("setspawn").setTabCompleter(setSpawnCmd);
		getCommand("spawn").setTabCompleter(spawnCmd);
		getCommand("easyss").setTabCompleter(easyssCmd);
	}
	
	private void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new PlayerQuit(), this);
		pm.registerEvents(new BlockCombat(), this);
		pm.registerEvents(new MoreEvents(), this);
	}
	
	// -------------------------------------

	public void checkVersion() throws IOException {
		if(getConfig().getBoolean("check-version.enabled")) {
			getLogger().info("Checking for updates...");
			
			URL url = new URL("https://raw.githubusercontent.com/TiagoCN/EasySetSpawn/master/version.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String str;

			if((str = br.readLine()) != null)
				if(str != null && str.matches(".*[123456789.-].*") && str.length() <= 16)
					if(!str.equalsIgnoreCase(getPluginVersion()))
							new_version = true;

			br.close();
			
			if(new_version) {
				getLogger().info("Update available!");
			}
		}
	}
	
	// -------------------------------------
	
	public static FileConfiguration getConfiguration() {
		return getInstance().getConfig();
	}

	// -------------------------------------

	public static Main getInstance() {
		return instance;
	}

	// -------------------------------------

	public static String getPluginVersion() {
		return instance.getDescription().getVersion();
	}

	public static boolean hasNewVersion() {
		return new_version;
	}
	
	public static String getConfigVersion() {
		return config_version;
	}

}
