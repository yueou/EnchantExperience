package com.yueou.EnchantExperience;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnchantExperienceCommand implements CommandExecutor{

	private EnchantExperience plugin;
	
	public EnchantExperienceCommand(EnchantExperience plugin){

		this.plugin=plugin;
	}


	@Override
	public boolean onCommand(CommandSender cmder, Command cmd, String str,
			String[] args) {
		
        Player player = null;
       	
        if(cmder instanceof Player)
        {
                player = (Player)cmder;
        }
        else return false;
        if(args.length == 0 ){
        	Enchanter enchanter = plugin.getEnchanter(player);
        	if((!enchanter.canEnchant())&&(!enchanter.canRepair())){
        		player.sendMessage(ChatColor.RED+ "你还没有附魔技能，无法查看附魔经验");
        		return true;
        	}
        	
        	player.sendMessage(ChatColor.RED + "----[ " + ChatColor.WHITE+"你的附魔等级信息 " + ChatColor.RED +"]-----");
        	player.sendMessage(ChatColor.GREEN + " 等级: "+ChatColor.WHITE+enchanter.getEnchantLevel()+ChatColor.GREEN+"/"+ChatColor.WHITE+plugin.getMaxLv());
        	player.sendMessage(ChatColor.GREEN + " 总经验: "+ChatColor.WHITE+enchanter.getExp());
        	
        	int nextexp = enchanter.getNextExp();
        	if(enchanter.isMaxLevel())
        		nextexp = 0;
        	player.sendMessage(ChatColor.GREEN + " 到下一级还需要经验值: "+ChatColor.WHITE+nextexp);
        	return true;
        }
        else{
        	if(args[0].equalsIgnoreCase("set")){
        		if(args.length<3){
        			player.sendMessage(ChatColor.GREEN+"/enchantlv set <玩家名> <等级> 来设定一个玩家的附魔等级");
        			return true;
        		}
        		
        		if(!player.isOp()){
        			player.sendMessage(ChatColor.RED+"权限不足");
        			return true;
        		}
        		
        		Player targetplayer = plugin.getServer().getPlayer(args[1]);
        		if(targetplayer==null){
        			player.sendMessage(ChatColor.RED+"没有找到玩家");
        			return true;
        		}
        		
        		int len = args[2].length();
        		
        		for(int i=0;i<len;i++){
        			if(args[2].charAt(i)>'9'||args[2].charAt(i)<'0'){
        				player.sendMessage(ChatColor.GREEN+"/enchantlv set <玩家名> <等级> 来设定一个玩家的附魔等级");
        				return true;
        			}
        		}
        		
        		int level = Integer.valueOf(args[2]);
        		
        		if(level<0)
        			level=0;
        		
        		if(level>plugin.getMaxLv())
        			level=plugin.getMaxLv();
        		
        		Enchanter enchanter = plugin.getEnchanter(targetplayer);
        		
        		enchanter.setEnchantLevel(level);
        		
        		if(player!=targetplayer)
        			player.sendMessage("改变了 "+ChatColor.GREEN + targetplayer.getName() +ChatColor.WHITE+" 的附魔等级!");
        		
        		targetplayer.sendMessage(ChatColor.GREEN+"附魔等级变为 "+level+ " 级!");
        		
        		return true;
        		
        	}
        	
        }
        
        
		return false;
	}
}