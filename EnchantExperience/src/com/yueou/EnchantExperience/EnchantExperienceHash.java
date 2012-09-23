package com.yueou.EnchantExperience;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class EnchantExperienceHash {
	
	private HashMap<String,Enchanter> enchantermap;
	private EnchantExperience plugin;
	private HashMap<String,Integer> blockmap;
	
	public EnchantExperienceHash(EnchantExperience plugin){
		this.plugin = plugin;
		
		enchantermap = new HashMap<String,Enchanter>();
		blockmap = new HashMap<String,Integer>();
	}
	
	public int getBlock(Block block){
		Location l = block.getLocation();
		String ls = l.getX()+":"+l.getY()+":"+l.getZ();
		
		Integer id = blockmap.get(ls);
		if(id==null)
			return 0;
		
		return id;
	}
	
	public void addBlock(Block block){
		Location l = block.getLocation();
		String ls = l.getX()+":"+l.getY()+":"+l.getZ();
		
		blockmap.put(ls, block.getTypeId());
	}
	public void removeBlock(Block block){
		Location l = block.getLocation();
		String ls = l.getX()+":"+l.getY()+":"+l.getZ();
		
		blockmap.remove(ls);
	}
	
	public void addEnchanter(Player player,int exp){
		
		Enchanter enchanter = new Enchanter(player,plugin,exp);
		enchantermap.put(player.getName().toLowerCase(), enchanter);
		
	}
	
	public void removeEnchanter(Player player){
		enchantermap.remove(player.getName().toLowerCase());
	}
	
	public void clear(){
		
		enchantermap.clear();
	}
	
	public Enchanter getEnchanter(Player player){
		
		return enchantermap.get(player.getName().toLowerCase());
	}

}
