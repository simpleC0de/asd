package me.gorok.main;

import org.bukkit.plugin.java.JavaPlugin;

import me.gorok.commands.Commandss;
import me.gorok.listeners.Listenerr;

public class Main extends JavaPlugin
{
	public static GameState gs = GameState.LOBBY;
	
	public void onEnable()
	{
		
		regCmds();
		regEvents();
		loadConfig();
		System.out.println("Aktiviert:");
	}
	public void onDisable()
	{
		
	}
	public void loadConfig()
	{
		if(!getDataFolder().exists())
		{
			getConfig().options().header("Designed for Public use MIT License.");
			getConfig().set("SpawnPoint.Player", null);
			getConfig().set("Enable", false);
			getConfig().set("minPlayers", 4);
			getConfig().set("countdowntime", 30);
			saveConfig();
			
		}
	}
	public void regCmds()
	{
		new Commandss(this);
		getCommand("setspawn").setExecutor(new Commandss(this));
		getCommand("setstart").setExecutor(new Commandss(this));
	}
	public void regEvents()
	{
		new Listenerr(this);
		getServer().getPluginManager().registerEvents(new Listenerr(this), this);
	}
}
