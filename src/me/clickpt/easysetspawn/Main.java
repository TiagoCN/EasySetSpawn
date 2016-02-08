package me.clickpt.easysetspawn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private static String config_version;
	private static boolean new_version;

	public void onEnable() {
		instance = this;
		
		config_version = "2.0";
		new_version = false;
		
		Config.loadConfig(this);
		checkVersion();
		ConfigUtils.load();
		
		getCommand("setspawn").setExecutor(new Commands());
		getCommand("spawn").setExecutor(new Commands());
		getCommand("ess").setExecutor(new Commands());

		Bukkit.getPluginManager().registerEvents(new Events(), this);

		getLogger().info("--------------------------");
		getLogger().info("");
		getLogger().info("EasySetSpawn enabled!");
		getLogger().info("");
		getLogger().info("--------------------------");
	}

	public void onDisable() {
		getLogger().info("--------------------------");
		getLogger().info("");
		getLogger().info("EasySetSpawn disabled!");
		getLogger().info("");
		getLogger().info("--------------------------");
	}

	// -------------------------------------
	
	protected void checkVersion() {
		if(Config.getConfig().getBoolean("check-version.enabled")) {
			try {
				URL url = new URL("http://clickpt.esy.es/dev/essversion.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
				String str;
				String s = null;

				while ((str = br.readLine()) != null)
					s = str;

				if (!s.equalsIgnoreCase(getPluginVersion()))
					new_version = true;
				
				if(s.equalsIgnoreCase("</html>"))
					new_version = false;

				br.close();
			} catch (MalformedURLException e1) {
				new_version = false;
			} catch (IOException e1) {
				new_version = false;
			}
		} else {
			new_version = false;
		}
	}
	
	// -------------------------------------

	protected static Main getInstance() {
		return instance;
	}
	
	// -------------------------------------
	
	public static String getPluginVersion() {
		return instance.getDescription().getVersion();
	}
	
	public static String getConfigVersion() {
		return config_version;
	}
	
	public static boolean hasNewVersion() {
		return new_version;
	}

}
