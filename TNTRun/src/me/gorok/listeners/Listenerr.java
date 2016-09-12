package me.gorok.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.gorok.main.GameState;
import me.gorok.main.Main;

public class Listenerr implements Listener
{
	
	private List<Player> alive = new ArrayList<>();
	private Main plugin;
	public Listenerr(Main plugin)
	{
		this.plugin = plugin;
	}
	@SuppressWarnings("static-access")
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		
		if(plugin.getConfig().getBoolean("Enable") == false)
		{
			return;
		}
		else
		{
			Player p = e.getPlayer();
			alive.add(p);
			int x = plugin.getConfig().getInt("SpawnPoint.PlayerX");
			int y = plugin.getConfig().getInt("SpawnPoint.PlayerY");
			int z =	plugin.getConfig().getInt("SpawnPoint.PlayerZ");
			Location loc = new Location(p.getWorld(), x, y, z);
			e.setJoinMessage(ChatColor.AQUA + p.getDisplayName() + ChatColor.GRAY + " joined the game.");
			new BukkitRunnable() {
				
				@Override
				public void run() {
					p.teleport(loc);
					
				}
			}.runTaskLater(plugin, 10);
			p.sendMessage("Test " + loc);
			
			int online = Bukkit.getOnlinePlayers().size();
			if(online >= plugin.getConfig().getInt("minPlayers"))
			{
				plugin.gs = GameState.STARTING;		
			}
			if(plugin.gs == GameState.STARTING)
			{
				new BukkitRunnable() {
					
					int i = plugin.getConfig().getInt("countdowntime");
					
					@Override
					public void run() {
						i--;
						p.sendMessage(ChatColor.RED + "Startet in " + ChatColor.GRAY + i + " Sekunden.");
						if(i == 1)
						{
							plugin.gs = GameState.INGAME;
							for(Player all : Bukkit.getOnlinePlayers())
							{
								int x = plugin.getConfig().getInt("StartPoint.PlayerX");
								int y = plugin.getConfig().getInt("StartPoint.PlayerY");
								int z = plugin.getConfig().getInt("StartPoint.PlayerZ");
								Bukkit.broadcastMessage(ChatColor.WHITE + "[" +  ChatColor.RED + "TNTRUN" + ChatColor.WHITE + "]" + ChatColor.GRAY + " TELEPORTIEREN!!");
								Location tp = new Location(p.getWorld(), x, y, z);
								all.teleport(tp);
							}
							cancel();
						}
						
					}
				}.runTaskTimer(plugin, 20, 20);
			}
		}
		
		
		
	}
	
	@EventHandler
	public void onPlayerMove ( PlayerMoveEvent e)
	{
		if(e.getPlayer() instanceof Player)
		{
			
			Player p = e.getPlayer();
			if(p.getGameMode() == GameMode.SPECTATOR)
			{
				return;
			}
			else
			{
				
			}
			if(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.TNT )
			{
				Location location = p.getLocation();
				Block tnt =  p.getLocation().getBlock().getRelative(BlockFace.DOWN);
				new BukkitRunnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tnt.setType(Material.AIR);
						@SuppressWarnings("unused")
						TNTPrimed tntprimed = tnt.getWorld().spawn(location, TNTPrimed.class);
					}
				}.runTaskLater(plugin, 2);		
			}
			
			if(p.getLocation().getBlockY() < 50)
			{
				p.setHealth(0.0);
			}
			if(alive.size() == 1)
			{
				//Player name = alive.get(0);
				new BukkitRunnable() {
					
					@Override
					public void run() {
						//Bukkit.broadcastMessage(ChatColor.RED + name.getDisplayName() + " hat das Spiel gewonnen!");
						
					}
				}.runTaskTimer(plugin, 0, 100);
				
				
			}
		}
	}
	
	@SuppressWarnings("static-access")
	@EventHandler
	public void onLogin (PlayerLoginEvent e)
	{
		if(plugin.gs == GameState.INGAME)
		{
			e.disallow(e.getResult().KICK_OTHER, ChatColor.RED + "Das Spiel hat schon gestartet.");
		}
		if(plugin.gs == GameState.LOBBY)
		{
			e.allow();
		}
		if(plugin.gs == GameState.STARTING)
		{
			e.allow();
		}
		if(plugin.gs == GameState.ENDING)
		{
			e.disallow(e.getResult().KICK_OTHER,  ChatColor.DARK_RED + "Der Server start gerade neu!");
		}
	}
	
	@EventHandler
	public void EntityExplode(ExplosionPrimeEvent e)
	{
		if(e.getEntityType() == EntityType.PRIMED_TNT)
		{
			e.getEntity().remove();
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDeath (PlayerDeathEvent e)
	{
		Player p = (Player) e.getEntity();
		e.setDeathMessage(ChatColor.AQUA + p.getDisplayName() + ChatColor.RED + " ist runtergefallen!");
		p.setDisplayName(ChatColor.RED + "*DEAD*");
		alive.remove(p);
		if(p instanceof Player)
		{
			new BukkitRunnable() {
				
				@Override
				public void run() {
					e.getEntity().setGameMode(GameMode.SPECTATOR);
					
					
					
				}
			}.runTaskLater(plugin, 20);
			
		}
		if(alive.size() == 1)
		{
			Player name = alive.get(0);
			new BukkitRunnable() {
				
				@Override
				public void run() {
					Bukkit.broadcastMessage(ChatColor.RED + name.getDisplayName() + " hat das Spiel gewonnen" +  ChatColor.GRAY);
					
				}
			};
		}
		if(alive.size() == 0)
		{
			Bukkit.broadcastMessage(ChatColor.RED + "ERROR");
			Bukkit.broadcastMessage(ChatColor.RED + "ERROR");
			Bukkit.broadcastMessage(ChatColor.RED + "ERROR");
			Bukkit.broadcastMessage(ChatColor.RED + "ERROR");
			Bukkit.broadcastMessage(ChatColor.RED + "ERROR");
			Bukkit.broadcastMessage(ChatColor.RED + "Cant fetch a Winner, return all Players to Hub.");
		}
		
		
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e)
	{
		Player p = e.getPlayer();
		if(p.getDisplayName() == "*DEAD*")
		{
			new BukkitRunnable() {
				
				@Override
				public void run() {
					p.setGameMode(GameMode.SPECTATOR);
					p.sendMessage(ChatColor.RED + "Du bist ausgeschieden.");
					
				}
			}.runTaskLater(plugin, 10);
		}
	}
	

}
