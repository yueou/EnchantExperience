package com.yueou.EnchantExperience;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginEnableEvent;

public class EnchantExperienceListener implements Listener{
	
	private EnchantExperience plugin;
	private int dirtexp;
	private int sandexp;
	private int stoneexp;
	private int woodexp;
	private int clayexp;
	private int coalexp;
	private int redstoneexp;
	private int ironexp;
	private int goldexp;
	private int lapisexp;
	private int diamondexp;
	
	public EnchantExperienceListener(EnchantExperience plugin){
		this.plugin = plugin;
		dirtexp = plugin.getConfig().getInt("experiences.dirt",1);
		sandexp = plugin.getConfig().getInt("experiences.sand",1);
		stoneexp = plugin.getConfig().getInt("experiences.stone",1);
		woodexp = plugin.getConfig().getInt("experiences.wood",1);
		clayexp = plugin.getConfig().getInt("experiences.clay",1);
		coalexp = plugin.getConfig().getInt("experiences.coalore",2);
		redstoneexp = plugin.getConfig().getInt("experiences.redstone",3);
		ironexp = plugin.getConfig().getInt("experiences.ironore",3);
		goldexp = plugin.getConfig().getInt("experiences.goldore",5);
		lapisexp = plugin.getConfig().getInt("experiences.lapisore",5);
		diamondexp = plugin.getConfig().getInt("experiences.diamondore",10);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)	
	public void onHeroesEnable(PluginEnableEvent pee){
		if(pee.getPlugin().getName().equalsIgnoreCase("Heroes")){
			plugin.onEnableConf();
		}
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)	
	public void onPlayerLogin(PlayerLoginEvent ple){
		Player player = ple.getPlayer();
		plugin.loadEnchanter(player);
		
		//plugin.saveAll();
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)	
	public void onPlayerQuit(PlayerQuitEvent pqe){
		Player player = pqe.getPlayer();
		String playername = player.getName();
		
		//plugin.getSaveFile().set(playername, plugin.getEnchanter(player).getExp());

		plugin.save(plugin.getEnchanter(player));
		plugin.removeEnchanter(player);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)	
	public void onBlockPlace(BlockPlaceEvent bpe){
		if(bpe.isCancelled())
			return;
		
		/*
		if(player.isOp()){
			return;
		}
		*/
		Block block = bpe.getBlock();
		Material type = block.getType();
		if(type==Material.CLAY||type==Material.COAL_ORE||type==Material.IRON_ORE||type==Material.DIAMOND_ORE||type==Material.LAPIS_ORE||type==Material.GOLD_ORE||type==Material.REDSTONE_ORE){
			plugin.getMap().addBlock(block);
		}
		
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)	
	public void onBlockBreak(BlockBreakEvent bbe){
		 

		Player player = bbe.getPlayer();
		if(bbe.isCancelled()){
			 return;
		}
		
		Enchanter enchanter = plugin.getEnchanter(player);
		
		if((!enchanter.canEnchant())&&(!enchanter.canRepair())){
			return;
		}
		
		if(enchanter.isMaxLevel()){
			return;
		}
		
		int oldlevel = enchanter.getEnchantLevel();
		
		Block block = bbe.getBlock();
		
		if(plugin.getMap().getBlock(block)==block.getTypeId()){
			//player.sendMessage(ChatColor.GRAY+"没有得到任何经验 - 物品放置的太频繁了");
			plugin.getMap().removeBlock(block);
			return;
		}
		
		if(block.getType()==Material.DIRT||block.getType()==Material.GRASS){
			enchanter.gainExp(dirtexp);
		}
		else if(block.getType()==Material.STONE){
			enchanter.gainExp(stoneexp);
		}
		else if(block.getType()==Material.SAND){
			enchanter.gainExp(sandexp);
		}
		else if(block.getType()==Material.LOG){
			enchanter.gainExp(woodexp);
		}		
		else if(block.getType()==Material.CLAY){
			enchanter.gainExp(clayexp);
		}
		else if(block.getType()==Material.COAL_ORE){
			enchanter.gainExp(coalexp);
		}
		else if(block.getType()==Material.IRON_ORE){
			enchanter.gainExp(ironexp);
		}
		else if(block.getType()==Material.REDSTONE_ORE){
			enchanter.gainExp(redstoneexp);
		}
		else if(block.getType()==Material.GOLD_ORE){
			enchanter.gainExp(goldexp);
		}
		else if(block.getType()==Material.LAPIS_ORE){
			enchanter.gainExp(lapisexp);
		}
		else if(block.getType()==Material.DIAMOND_ORE){
			enchanter.gainExp(diamondexp);
		}
		int newlevel = enchanter.getEnchantLevel();
		
		if(newlevel!=oldlevel){
			player.sendMessage("附魔等级提升到了 " + ChatColor.GREEN + newlevel + ChatColor.WHITE +" 级!");
			plugin.saveAll();
		}
	}

}
