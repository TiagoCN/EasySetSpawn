package me.clickpt.easysetspawn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

public class Main extends JavaPlugin {

	private static Main instance;

	private final static String config_version = "2.1";
	private static boolean new_version = false;

	public void onEnable() {
		instance = this;

		Config.loadConfig(this);
		try {
			checkVersion();
		} catch (IOException e1) {
			getLogger().warning("Error checking for updates!");
		}

		getCommand("setspawn").setExecutor(new Commands());
		getCommand("spawn").setExecutor(new Commands());
		getCommand("easyss").setExecutor(new Commands());

		getServer().getPluginManager().registerEvents(new Events(), this);

		if (Config.getConfig().getBoolean("metrics")) {
			try {
				MetricsLite metrics = new MetricsLite(this);
				metrics.start();
			} catch (IOException e) {
				getLogger().warning("Error loading MetricsLite!");
			} finally {
				getLogger().info("MetricsLite enabled!");
			}
		}

		getLogger().info("--------------------------");
		getLogger().info("");
		getLogger().info(getDescription().getName() + " enabled!");
		getLogger().info("");
		getLogger().info("--------------------------");
	}

	public void onDisable() {
		getLogger().info("--------------------------");
		getLogger().info("");
		getLogger().info(getDescription().getName() + " disabled!");
		getLogger().info("");
		getLogger().info("--------------------------");
	}

	// -------------------------------------

	protected void checkVersion() throws IOException {
		if (Config.getConfig().getBoolean("check-version.enabled")) {
			URL url = new URL("https://raw.githubusercontent.com/ClickPT/EasySetSpawn/master/version.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String str;

			if((str = br.readLine()) != null)
				if(!str.equalsIgnoreCase(getPluginVersion()) && !str.contains("<") && str.contains("."))
					new_version = true;

			br.close();
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
