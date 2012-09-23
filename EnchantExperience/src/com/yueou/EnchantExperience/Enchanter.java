package com.yueou.EnchantExperience;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.Hero;

public class Enchanter {
	
	private int exp;
	private Player player;
	private Hero hero;
	private EnchantExperience plugin;
	private int level;
	
	public Enchanter(Player player,EnchantExperience plugin,int exp){
		
		this.plugin = plugin;
		this.player = player;
		hero = ((Heroes)plugin.getServer().getPluginManager().getPlugin("Heroes")).getCharacterManager().getHero(player);
		this.exp = exp;
		level = player.getLevel();
	}
	
	public Player getPlayer(){
		
		return player;
	}
	
	public Hero getHero(){
		
		return hero;
	}
	
	public int getExp(){
		
		return exp;
	}
	
	public void setExp(int newexp){
		
		exp = newexp;
	}
	
	public int getEnchantLevel(){
		int maxlv = plugin.getMaxLv();
		int k = ((plugin.getMaxExp()*2)/maxlv)/maxlv;
		int level = (int) Math.sqrt((double)(2*exp)/(double)k);
		return level;
	}
	
	public int getNextExp(){
		int maxlv = plugin.getMaxLv();
		int level = getEnchantLevel();
		level++;		
		int k = ((plugin.getMaxExp()*2)/maxlv)/maxlv;
		int nextexp = (k*level*level)/2;
		return nextexp - exp;
	}
	
	public void CostEnchantLevel(int level){

		int maxlv = plugin.getMaxLv();
		int k = ((plugin.getMaxExp()*2)/maxlv)/maxlv;
		int costexp = (k*level*level)/2;
		exp = exp - costexp;
		player.sendMessage(ChatColor.GREEN + "消耗了 " + costexp +" 点附魔经验, 附魔等级变为 " + getEnchantLevel() + " 级!");
		plugin.save();
	}
	
	public void setEnchantLevel(int level){
		int maxlv = plugin.getMaxLv();
		int k = ((plugin.getMaxExp()*2)/maxlv)/maxlv;
		int costexp = (k*level*level)/2;
		exp = costexp;
	}
	
	public boolean isMaxLevel(){
		if(exp>=plugin.getMaxExp()){
			return true;
		}
		return false;
	}
	
	public void gainExp(int addexp){
		
		exp += addexp;
	}
	
	public String getName(){
		
		return player.getName();
	}
	
	public int getLevel(){
		
		return level;
	}
	
	public void setLevel(int level){
		
		this.level = level;
	}
	
	public boolean canEnchant(){
		
		return hero.canUseSkill(plugin.getEnchantSkill());
	}
	
	public boolean canRepair(){
		
		return hero.canUseSkill(plugin.getRepairSkill());
	}

}
