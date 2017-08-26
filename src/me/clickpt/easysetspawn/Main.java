package me.clickpt.easysetspawn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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
		
		try {
			checkVersion();
		} catch (IOException e1) {
			getLogger().warning("Error checking updates!");
		}
		
		registerCommands();
		registerListeners();

		if(getConfig().getBoolean("metrics")) {
			new MetricsLite(this);
		}

		getLogger().info(getDescription().getName() + " enabled!");
	}

	public void onDisable() {
		getLogger().info(getDescription().getName() + " disabled!");
	}

	// -------------------------------------
	
	private void registerCommands() {
		getCommand("setspawn").setExecutor(new SetSpawnCMD());
		getCommand("spawn").setExecutor(new SpawnCMD());
		getCommand("easyss").setExecutor(new EasySSCMD());
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
			URL url = new URL("https://raw.githubusercontent.com/TiagoCN/EasySetSpawn/master/version.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String str;

			if((str = br.readLine()) != null)
				if(!str.equalsIgnoreCase(getPluginVersion()) && !str.contains("<") && str.contains("."))
					new_version = true;

			br.close();
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
