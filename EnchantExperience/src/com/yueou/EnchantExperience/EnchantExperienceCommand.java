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
        		player.sendMessage(ChatColor.RED+ "�㻹û�и�ħ���ܣ��޷��鿴��ħ����");
        		return true;
        	}
        	
        	player.sendMessage(ChatColor.RED + "----[ " + ChatColor.WHITE+"��ĸ�ħ�ȼ���Ϣ " + ChatColor.RED +"]-----");
        	player.sendMessage(ChatColor.GREEN + " �ȼ�: "+ChatColor.WHITE+enchanter.getEnchantLevel()+ChatColor.GREEN+"/"+ChatColor.WHITE+plugin.getMaxLv());
        	player.sendMessage(ChatColor.GREEN + " �ܾ���: "+ChatColor.WHITE+enchanter.getExp());
        	
        	int nextexp = enchanter.getNextExp();
        	if(enchanter.isMaxLevel())
        		nextexp = 0;
        	player.sendMessage(ChatColor.GREEN + " ����һ������Ҫ����ֵ: "+ChatColor.WHITE+nextexp);
        	return true;
        }
        else{
        	if(args[0].equalsIgnoreCase("set")){
        		if(args.length<3){
        			player.sendMessage(ChatColor.GREEN+"/enchantlv set <�����> <�ȼ�> ���趨һ����ҵĸ�ħ�ȼ�");
        			return true;
        		}
        		
        		if(!player.isOp()){
        			player.sendMessage(ChatColor.RED+"Ȩ�޲���");
        			return true;
        		}
        		
        		Player targetplayer = plugin.getServer().getPlayer(args[1]);
        		if(targetplayer==null){
        			player.sendMessage(ChatColor.RED+"û���ҵ����");
        			return true;
        		}
        		
        		int len = args[2].length();
        		
        		for(int i=0;i<len;i++){
        			if(args[2].charAt(i)>'9'||args[2].charAt(i)<'0'){
        				player.sendMessage(ChatColor.GREEN+"/enchantlv set <�����> <�ȼ�> ���趨һ����ҵĸ�ħ�ȼ�");
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
        			player.sendMessage("�ı��� "+ChatColor.GREEN + targetplayer.getName() +ChatColor.WHITE+" �ĸ�ħ�ȼ�!");
        		
        		targetplayer.sendMessage(ChatColor.GREEN+"��ħ�ȼ���Ϊ "+level+ " ��!");
        		
        		return true;
        		
        	}
        	
        }
        
        
		return false;
	}
}