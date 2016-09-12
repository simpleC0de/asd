package me.gorok.commands;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import API.TitleManager;
import me.gorok.main.Main;



public class Commandss implements CommandExecutor {

	
	private Main plugin;
	public Commandss(Main plugin)
	{
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			if(cmd.getLabel().equalsIgnoreCase("setspawn"))
			{
				int x = p.getLocation().getBlockX();
				int y = p.getLocation().getBlockY();
				int z = p.getLocation().getBlockZ();
				plugin.getConfig().set("SpawnPoint.PlayerX", x);
				plugin.getConfig().set("SpawnPoint.PlayerY", y);
				plugin.getConfig().set("SpawnPoint.PlayerZ", z);
				plugin.saveConfig();
				p.sendMessage(ChatColor.WHITE + "[" +  ChatColor.RED + "TNTRUN" + ChatColor.WHITE + "]" + ChatColor.GRAY + " Spawn gesetzt.");
			}
		}
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			if(cmd.getLabel().equalsIgnoreCase("setstart"))
			{
				int x1 = p.getLocation().getBlockX();
				int y1 = p.getLocation().getBlockY();
				int z1 = p.getLocation().getBlockZ();
				
				plugin.getConfig().set("StartPoint.PlayerX", x1);
				plugin.getConfig().set("StartPoint.PlayerY", y1);
				plugin.getConfig().set("StartPoint.PlayerZ", z1);
				plugin.saveConfig();
				p.sendMessage(ChatColor.WHITE + "[" +  ChatColor.RED + "TNTRUN" + ChatColor.WHITE + "]" + ChatColor.GRAY + " Startpunkt gesetzt.");
				TitleManager.sendTitle(p, ChatColor.RED + "Spawn set.", 25, 50, 25);
			}
		}
		
		return true;
	}

}
